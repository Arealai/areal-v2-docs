from pydantic import BaseModel
from typing import Optional

# When an existing documents status changes (e.g processing -> completed)
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

# When a new document is created (e.g. preparing can generate multiple documents)
class FileCreatedMessage(BaseModel):
    class Meta(BaseModel):
        upload_session_id: str
        user: User

    type: str = 'FILE_CREATED'
    meta: Meta
    data: SessionDetailListItem

# When a document is removed 
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

# When a document is pushed to a gateway (e.g. Encompass DocPush)
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
