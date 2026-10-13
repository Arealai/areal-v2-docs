"""Build the partner OpenAPI subset for the Copilot agents guide from the UAT spec.

Every endpoint the guide mentions must be in OPERATIONS, and nothing else.

Writes the JSON and, next to it, a Scalar reference page (same name, .html)
with the spec embedded, so the two are always built together.

Usage:
    curl -sS -o /tmp/openapi-uat.json https://uat-api.v2.areal.ai/api/v2/openapi.json
    python3 partner-guides/build_copilot_openapi_subset.py /tmp/openapi-uat.json \
        partner-guides/copilot-agents-openapi.json
"""

import copy
import json
import re
import sys
from pathlib import Path

SRC = Path(sys.argv[1])
DST = Path(sys.argv[2])

PREFIX = '/api/v2'
OPERATIONS = {
    '/sessions/': ['get'],
    '/profiles/users/': ['get'],
    '/copilot/task-group/': ['get'],
    '/copilot/task-group/{task_group_id}/': ['get', 'patch'],
    '/copilot/task-group/{task_group_id}/tasks/': ['get'],
    '/copilot/task-group/{task_group_id}/execute/': ['post'],
    '/copilot/task/{task_id}/': ['get', 'patch', 'delete'],
    '/copilot/task/{task_id}/change-status/': ['post'],
    '/copilot/task/{task_id}/execute/': ['post'],
    '/copilot/{task_id}/suppress/': ['post'],
    '/copilot/task/{task_id}/assign_user/': ['post'],
}
SECURITY_SCHEMES = ['ArealAPIKeyBearer', 'ArealAPIKeyHeader']

spec = json.loads(SRC.read_text())
schemas = spec['components']['schemas']

paths = {}
for path, methods in OPERATIONS.items():
    full = PREFIX + path
    source = spec['paths'][full]
    paths[full] = {}
    for method in methods:
        op = copy.deepcopy(source[method])
        if 'security' in op:
            op['security'] = [
                s for s in op['security'] if next(iter(s)) in SECURITY_SCHEMES
            ]
        paths[full][method] = op

# Without session_id this endpoint lists the caller's organization users only;
# that is the only form partners get.
users_op = paths[PREFIX + '/profiles/users/']['get']
users_op['parameters'] = [
    p for p in users_op.get('parameters', []) if p['name'] != 'session_id'
]

DESCRIPTIONS = {
    ('/copilot/{task_id}/suppress/', 'post'): (
        "Override a failed task result on one loan as 'success' or 'n/a', or "
        'remove an existing override. The caller becomes the assigned user.'
    ),
    ('/copilot/task/{task_id}/', 'patch'): (
        "Rename a task. Doesn't change its plan or results."
    ),
}
for (path, method), text in DESCRIPTIONS.items():
    paths[PREFIX + path][method]['description'] = text

ref_re = re.compile(r'#/components/schemas/([^"]+)')
needed: set[str] = set()
todo = ref_re.findall(json.dumps(paths))
while todo:
    name = todo.pop()
    if name in needed:
        continue
    needed.add(name)
    todo.extend(ref_re.findall(json.dumps(schemas[name])))

out_schemas = {name: copy.deepcopy(schemas[name]) for name in sorted(needed)}
# Partners run and rename the tasks they have; they don't write prompts.
edit_task = out_schemas['EditTaskSchema']
edit_task['properties'] = {'name': {'type': 'string', 'title': 'Name'}}
edit_task['required'] = ['name']
# The partner integrates through Byte only.
mention_type = out_schemas['AttrsSchema']['properties']['type']
mention_type['enum'] = [
    t for t in mention_type['enum'] if t not in {'encompass', 'meridianlink'}
]
plan_result = out_schemas['ExecutePlanResponseSchema']
for field in ('is_encompass_update_task', 'is_encompass_update_accepted'):
    plan_result['properties'].pop(field, None)
    if field in plan_result.get('required', []):
        plan_result['required'].remove(field)

user_list = out_schemas['UserInOrganization']
user_list['properties'].pop('external_users', None)
if 'required' in user_list:
    user_list['required'] = [r for r in user_list['required'] if r != 'external_users']

subset = {
    'openapi': spec['openapi'],
    'info': {
        'title': 'Areal Copilot Agents API',
        'version': spec['info']['version'],
        'description': (
            'Endpoints used to list Copilot agents, read their status, read '
            'their tasks and change those tasks. Companion to the Areal '
            'Copilot Agents API integration guide.'
        ),
    },
    'servers': [
        {'url': 'https://prod-api.v2.areal.ai', 'description': 'Production'},
        {'url': 'https://uat-api.v2.areal.ai', 'description': 'UAT'},
    ],
    'paths': paths,
    'components': {
        'schemas': out_schemas,
        'securitySchemes': {
            k: spec['components']['securitySchemes'][k] for k in SECURITY_SCHEMES
        },
    },
    'tags': [
        t for t in spec.get('tags', []) if t['name'] in {'sessions', 'profiles', 'copilot'}
    ],
}

spec_json = json.dumps(subset, indent=2)
DST.write_text(spec_json + '\n')

# The spec sits inside a <script> tag, so it must not close it early.
embedded = spec_json.replace('</', '<\\/')
DST.with_suffix('.html').write_text(
    '<!doctype html>\n'
    '<html>\n'
    '<head><meta charset="utf-8"><title>Areal Copilot Agents API</title></head>\n'
    '<body>\n'
    f'<script id="api-reference" type="application/json">{embedded}</script>\n'
    '<script src="https://cdn.jsdelivr.net/npm/@scalar/api-reference"></script>\n'
    '</body>\n'
    '</html>\n'
)
print(f'{len(paths)} paths, {sum(len(v) for v in paths.values())} operations, {len(out_schemas)} schemas')
