from pydantic import BaseModel
from typing import Optional

# When the status of the CDBalancer changes (e.g. preparing -> processing -> completed)
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

# When a CDPull is started (e.g. Encompass CDPull)
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

# When a CDPull is finished (e.g. Encompass CDPull)
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

# When a CDPush is finished (e.g. Encompass CDPush)
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
