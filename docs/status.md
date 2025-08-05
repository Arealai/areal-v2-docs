# Status Tracking

**Document processing** is an asynchronous process. Therefore, we need a way to track the status of the processing.
There are 2 ways to track the status of the processing:

- **WebSockets** (recommended)
- **Polling** (alternative)

## WebSockets

WebSockets are a more efficient way to track the status of the processing.
It is a long-lived connection that allows you to receive updates as they happen.

```py title="WebSockets Example" linenums="1"
import websockets
from pydantic import BaseModel

BASE_URL = 'ws://dev-api.v2.areal.ai/'

async def get_status(client: websockets.WebSocketClientProtocol, document_id: str) -> str:
    async with websockets.connect(f"{BASE_URL}/status/") as ws:
        while True:
            message = await ws.recv()
            msg = FileStatusUpdatedMessage.model_validate_json(message)

            # skip if not the document we are interested in
            if msg.meta.id != document_id:
                continue
            
            print(f"Processing {msg.meta.id} is {msg.data.status}")

            # NOTE: no need to break since if this is background task
            # and so you can keep listening for other documents

asyncio.run(get_status(document_id))

class FileStatusUpdatedMessage(BaseModel):
    class Meta(BaseModel):
        upload_session_id: str
        id: str
        user: User

    class Data(BaseModel):
        status: Status

    type: str = 'FILE_STATUS_UPDATED'
    meta: Meta
    data: Data

```

For other WebSocket notification types see [WebSocket API](websocket.md)


## Polling

Continuously poll the status of the processing until it is completed or failed.
You can also implement a custom timeout to avoid waiting forever.

```py title="Long Polling Example" linenums="1"
import requests

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
```

## Meaning of the status

- **pending**: We received your request, but processing has not started yet.
- **preparing**: The document is being classified.
- **processing**: Data extraction is in progress.
- **completed**: Processing is complete and successful.
- **failed**: Processing failed. Please check the error details or try again.


## WebSocket API

### Document Processing & DocPush

```py title="Document Status Tracking" linenums="1"

# When an existing documents status changes (e.g processing -> completed)
class FileStatusUpdatedMessage(BaseModel):
    class Meta(BaseModel):
        upload_session_id: str
        id: str
        user: User

    class Data(BaseModel):
        status: Status

    type: str = 'FILE_STATUS_UPDATED'
    meta: Meta
    data: Data

# When a new document is created (e.g. preparing can generate multiple documents)
class FileCreatedMessage(BaseModel):
    class Meta(BaseModel):
        upload_session_id: str
        user: User

    type: str = 'FILE_CREATED'
    meta: Meta
    data: SessionDetailListItem

# When a document is removed 
class FileDeletedMessage(BaseModel):
    class Meta(BaseModel):
        upload_session_id: str
        id: str
        user: User

    type: str = 'FILE_DELETED'
    meta: Meta
    data: None = None

class User(BaseModel):
    id: str

# When a document is pushed to a gateway (e.g. Encompass DocPush)
class DocPushFinishedMessage(BaseModel):
    class Meta(BaseModel):
        upload_session_id: str
        id: str
        user: User

    class Data(BaseModel):
        status: Status
        error: Optional[str]

    type: str = 'DOC_PUSH_FINISHED'
    meta: Meta
    data: Data

```

---

### CDBalancer & CDPush/Pull

```py title="CDBalancer Status Tracking" linenums="1"

# When the status of the CDBalancer changes (e.g. preparing -> processing -> completed)
class CDBalancerStatusUpdatedMessage(BaseModel):
    class Meta(BaseModel):
        upload_session_id: str
        id: str
        user: User

    class Data(BaseModel):
        status: Status

    type: str = 'CDBALANCER_STATUS_UPDATED'
    meta: Meta
    data: Data

# When a CDPull is started (e.g. Encompass CDPull)
class CDPullStartedMessage(BaseModel):
    class Meta(BaseModel):
        upload_session_id: str
        user: User

    class Data(BaseModel):
        class Document(BaseModel):
            id: str
            name: str
            status: Status

        document: Document

    type: str = 'CD_PULL_STARTED'
    meta: Meta
    data: Data

# When a CDPull is finished (e.g. Encompass CDPull)
class CDPullFinishedMessage(BaseModel):
    class Meta(BaseModel):
        upload_session_id: str
        user: User

    class Data(BaseModel):
        class Document(BaseModel):
            id: str
            name: str
            status: Status

        document: Optional[Document]
        status: Status
        error: Optional[str]

    type: str = 'CD_PULL_FINISHED'
    meta: Meta
    data: Data

# When a CDPush is finished (e.g. Encompass CDPush)
class CDPushFinishedMessage(BaseModel):
    class Meta(BaseModel):
        upload_session_id: str
        cdbalancer_request_id: str
        gateway_request_id: Optional[str] = None
        user: User

    class Data(BaseModel):
        status: Status
        error: Optional[str]
        stats_message: Optional[str] = None
        stats_status: Optional[str] = None

    type: str = 'CD_PUSH_FINISHED'
    meta: Meta
    data: Data

```

---

### Copilot Tasks

```py title="CDBalancer Status Tracking" linenums="1"

# When a copilot task is updated (e.g. pending -> processing -> completed)
class CopilotTaskUpdatedMessage(BaseModel):
    class Meta(BaseModel):
        upload_session_id: str
        task_group_id: str

    class Data(BaseModel):
        task: TaskSchema
        error: Optional[str]

    type: str = 'COPILOT_TASK_UPDATED'
    meta: Meta
    data: Data

# When a new copilot task is created
class CopilotTaskCreatedMessage(BaseModel):
    class Meta(BaseModel):
        upload_session_id: str
        task_group_id: str

    class Data(BaseModel):
        task: TaskSchema

    type: str = 'COPILOT_TASK_CREATED'
    meta: Meta
    data: Data

# When a copilot task is deleted
class CopilotTaskDeletedMessage(BaseModel):
    class Meta(BaseModel):
        task_group_id: str

    class Data(BaseModel):
        task_id: str

    type: str = 'COPILOT_TASK_DELETED'
    meta: Meta
    data: Data
```