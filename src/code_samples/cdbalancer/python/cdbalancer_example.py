import requests

BASE_URL = "http://dev-api.v2.areal.ai/api/v2"

# 0. Login - details in Authentication section

# 1. Process Documents that you are interested in balancing
# -- For this, please refer to the Processing section

# 2. Call CDBalancer API using existing processed documents
cdbalancer_response = client.post(
    url=f"{BASE_URL}/cdbalancer/",
    json={"document_id_1": "doc1_id", "document_id_2": "doc2_id"},
    headers={"Content-Type": "application/json"},
)
request_id = cdbalancer_response.json()["request_id"]
