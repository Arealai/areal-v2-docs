import asyncio
import websockets
from pydantic import BaseModel

BASE_URL = 'ws://dev-api.v2.areal.ai/'

async def get_status(client: websockets.WebSocketClientProtocol, document_id: str) -> str:
    async with websockets.connect(f"{BASE_URL}/status/") as ws:
        while True:
            message = await ws.recv()
            msg = FileStatusUpdatedMessage.model_validate_json(message)

            # skip if not the document we are interested in
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
