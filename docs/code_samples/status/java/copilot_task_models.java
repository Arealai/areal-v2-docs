// When a copilot task is updated (e.g. pending -> processing -> completed)
class CopilotTaskUpdatedMessage {
    public static class Meta {
        public String upload_session_id;
        public String task_group_id;
    }
    
    public static class Data {
        public TaskSchema task;
        public String error;
    }
    
    public String type = "COPILOT_TASK_UPDATED";
    public Meta meta;
    public Data data;
}

// When a new copilot task is created
class CopilotTaskCreatedMessage {
    public static class Meta {
        public String upload_session_id;
        public String task_group_id;
    }
    
    public static class Data {
        public TaskSchema task;
    }
    
    public String type = "COPILOT_TASK_CREATED";
    public Meta meta;
    public Data data;
}

// When a copilot task is deleted
class CopilotTaskDeletedMessage {
    public static class Meta {
        public String task_group_id;
    }
    
    public static class Data {
        public String task_id;
    }
    
    public String type = "COPILOT_TASK_DELETED";
    public Meta meta;
    public Data data;
}
