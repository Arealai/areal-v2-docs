# Upload Sessions

When you upload a document to ArealAI for processing, we create an upload session, a grouping of documents related to your loan.

!!! warning "UploadSession one-to-one with Loan"
    We designed the UploadSession to be one-to-one with a Loan.
    So to get the most accurate results, you should create a new UploadSession for each loan.
    See [Processing](processing/processing.md) for more details.

## Example Views

### List View
![Example List View](assets/session-v2.png)

### Detail View
![Example Detail View](assets/session-v2-details.png)

## Example Usage

```py title="Get UploadSession details" linenums="1"
import requests

BASE_URL = 'http://dev-api.v2.areal.ai/api/v2'

# 0. Login -> 1. Create UploadSession 

# 2. List UploadSessions for your organization
list_sessions_response = client.get(
    url=f'{BASE_URL}/sessions/', 
    params={'sort_by': 'updated_at', 'sort_order': 'desc'}
).json()

for session in list_sessions_response['objects']:
    print(f"UploadSession: {session['id']} - {session['name']} - {session['updated_at']}")
    session_id = session['id']

# 3. Get documents in an UploadSession
session_details = client.get(f'{BASE_URL}/sessions/{session_id}').json()
for document in session_details['objects']:
    print(f'{document["id"]} - {document["name"]} - {document["status"]}')
```

For technical details see [UploadSession API](https://dev-api.v2.areal.ai/api/v2/docs#/sessions)
