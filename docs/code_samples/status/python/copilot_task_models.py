from pydantic import BaseModel
from typing import Optional

# When a copilot task is updated (e.g. pending -> processing -> completed)
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

# When a new copilot task is created
class CopilotTaskCreatedMessage(BaseModel):
    class Meta(BaseModel):
        upload_session_id: str
        task_group_id: str

    class Data(BaseModel):
        task: TaskSchema

    type: str = 'COPILOT_TASK_CREATED'
    meta: Meta
    data: Data

# When a copilot task is deleted
class CopilotTaskDeletedMessage(BaseModel):
    class Meta(BaseModel):
        task_group_id: str

    class Data(BaseModel):
        task_id: str

    type: str = 'COPILOT_TASK_DELETED'
    meta: Meta
    data: Data
