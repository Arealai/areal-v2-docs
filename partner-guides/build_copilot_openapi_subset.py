"""Build the partner OpenAPI subset for the Copilot agents guide from the UAT spec.

Every endpoint the guide mentions must be in OPERATIONS, and nothing else.

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
    '/accounts/login/': ['post'],
    '/accounts/refresh/': ['post'],
    '/sessions/': ['get'],
    '/profiles/users/': ['get'],
    '/copilot/task-group/': ['get'],
    '/copilot/task-group/{task_group_id}/': ['get', 'patch'],
    '/copilot/task-group/{task_group_id}/tasks/': ['get'],
    '/copilot/task-group/{task_group_id}/execute/': ['post'],
    '/copilot/task/': ['post'],
    '/copilot/task/{task_id}/': ['get', 'patch', 'delete'],
    '/copilot/task/{task_id}/change-status/': ['post'],
    '/copilot/task/{task_id}/execute/': ['post'],
    '/copilot/{task_id}/suppress/': ['post'],
    '/copilot/task/{task_id}/assign_user/': ['post'],
    '/copilot/loan-contact-roles/': ['get'],
    '/resources/templates/': ['get'],
    '/resources/templates/{template_id}/components/': ['get'],
    '/resources/loan_infos/': ['get'],
    '/resources/encompass_fields/': ['get'],
    '/resources/byte_fields/': ['get'],
    '/resources/meridianlink_fields/': ['get'],
    '/resources/selectors/': ['get'],
    '/resources/docusign_templates/': ['get'],
    '/profiles/admin/organization/email_templates/': ['get'],
}
SECURITY_SCHEMES = ['ArealAPIKeyBearer', 'ArealAPIKeyHeader', 'JWTBearer']

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
    ('/resources/loan_infos/', 'get'): (
        'Lists loan-level attributes with their id and name, for loan_info '
        'mentions in prompts. With session_id, includes the value on that loan.'
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
    'servers': [{'url': 'https://uat-api.v2.areal.ai', 'description': 'UAT'}],
    'paths': paths,
    'components': {
        'schemas': out_schemas,
        'securitySchemes': {
            k: spec['components']['securitySchemes'][k] for k in SECURITY_SCHEMES
        },
    },
    'tags': [
        t for t in spec.get('tags', []) if t['name'] in {'accounts', 'sessions', 'profiles', 'copilot', 'resources'}
    ],
}

DST.write_text(json.dumps(subset, indent=2) + '\n')
print(f'{len(paths)} paths, {sum(len(v) for v in paths.values())} operations, {len(out_schemas)} schemas')
