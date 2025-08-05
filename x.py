import requests
from pathlib import Path

BASE_PATH = Path(__file__).parent
BASE_URL = "http://dev-api.v2.areal.ai/api/v2"

# 0. Login - details in Authentication section
login_response = requests.post(f"{BASE_URL}/accounts/login/")
client = requests.Session()
client.cookies.update(  # (1)
    {
        "access_token": login_response.cookies["access_token"],
        "refresh_token": login_response.cookies["refresh_token"],
    }
)

# this client is now authenticated for the duration of access_token
# after that you can refresh it using the /accounts/refresh endpoint

# 1. Process Documents that you are interested in balancing
# -- For this, please refer to the Processing section

# 2. Call CDBalancer API using existing processed documents
cdbalancer_response = client.post(
    url=f"{BASE_URL}/cdbalancer/",
    json={"document_id_1": "doc1_id", "document_id_2": "doc2_id"},
    headers={"Content-Type": "application/json"},
)
request_id = cdbalancer_response.json()["request_id"]

