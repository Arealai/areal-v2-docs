import requests
import time

BASE_URL = 'http://dev-api.v2.areal.ai/api/v2'

# 0. Login - details in Authentication section ... client is authenticated
# 1. Starting the processing ... we get a request_id (which is actually the document_id) -- details in Processing section
# 2. Polling the status of the processing

document_id = "your_document_id_here" 

def get_status(client: requests.Session, document_id: str) -> str:
    try:
        response = client.get(f"{BASE_URL}/documents/{document_id}/")
        document_details = response.json()
        return document_details["status"]
    except Exception as e:
        print(f"Error getting status: {e}")
        return "unknown"

status = get_status(client, document_id)
# NOTE: You can also inspect other fields in document_details as needed

timeout = 10
while status not in ["completed", "failed"]:
    time.sleep(1)
    timeout -= 1
    if timeout <= 0:
        print("Timeout reached")
        break
    status = get_status(client, document_id)
