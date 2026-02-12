import requests

BASE_URL = 'http://dev-api.v2.areal.ai/api/v2'

# 0. Login -> 1. Create UploadSession 

# 2. List UploadSessions for your organization
list_sessions_response = client.get(
    url=f'{BASE_URL}/sessions/', 
    params={'sort_by': 'updated_at', 'sort_order': 'desc'}
).json()

for session in list_sessions_response['objects']:
    print(f"UploadSession: {session['id']} - {session['name']} - {session['updated_at']}")
    session_id = session['id']

# 3. Get documents in an UploadSession
session_details = client.get(f'{BASE_URL}/sessions/{session_id}').json()
for document in session_details['objects']:
    print(f'{document["id"]} - {document["name"]} - {document["status"]}')
