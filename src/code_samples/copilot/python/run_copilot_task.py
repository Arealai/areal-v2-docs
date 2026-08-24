import requests

BASE_URL = 'http://dev-api.v2.areal.ai/api/v2'
SESSION_ID = 'your-loan-id'

# 0. Login - details in Authentication section

# 1. List Copilot agents (task groups) for this loan
agents = client.get(
    url=f'{BASE_URL}/copilot/task-group/',
    params={'session_id': SESSION_ID},
).json()

for agent in agents['objects']:
    print(f"{agent['id']} - {agent['name']} - {agent['status']}")
    task_group_id = agent['id']

# 2. List tasks in an agent
tasks = client.get(
    url=f'{BASE_URL}/copilot/task-group/{task_group_id}/tasks/',
    params={'session_id': SESSION_ID},
).json()

for task in tasks['objects']:
    print(f"{task['id']} - {task['name']} - {task['results']['status']}")
    task_id = task['id']

# 3. Run a task against this loan
run_response = client.post(
    url=f'{BASE_URL}/copilot/task/{task_id}/execute/',
    json={'session_id': SESSION_ID},
    headers={'Content-Type': 'application/json'},
)
print(run_response.json())
