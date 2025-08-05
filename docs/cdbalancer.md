# Closing Disclosure Balancing

CDBalancer takes 2 processed Closing Disclosure documents and *balances* them.

!!! warning "Asynchronous CDBalancer"
    The entire flow is asynchronous, meaning that when you start the cdbalancer we will respond with a __request_id__ which you can use to track of the status of the cdbalancer request. 
    
    While users of Areal Dashboard can easily see the live status of their cdbalancer requests.
    
    So if you are planning to integrate our API, you can use our WebSocket API or manually poll the status of the cdbalancer request.
    
    See [Status Tracking](processing/status.md) for more details.

## Example Usage

```py title="CDBalancer Example Usage" linenums="1"
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
```
