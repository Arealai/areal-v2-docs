using System;
using System.Text.Json.Serialization;

// When a copilot task is updated (e.g. pending -> processing -> completed)
public class CopilotTaskUpdatedMessage
{
    public class Meta
    {
        [JsonPropertyName("upload_session_id")]
        public string UploadSessionId { get; set; }
        
        [JsonPropertyName("task_group_id")]
        public string TaskGroupId { get; set; }
    }
    
    public class Data
    {
        [JsonPropertyName("task")]
        public TaskSchema Task { get; set; }
        
        [JsonPropertyName("error")]
        public string Error { get; set; }
    }
    
    [JsonPropertyName("type")]
    public string Type { get; set; } = "COPILOT_TASK_UPDATED";
    
    [JsonPropertyName("meta")]
    public Meta Meta { get; set; }
    
    [JsonPropertyName("data")]
    public Data Data { get; set; }
}

// When a new copilot task is created
public class CopilotTaskCreatedMessage
{
    public class Meta
    {
        [JsonPropertyName("upload_session_id")]
        public string UploadSessionId { get; set; }
        
        [JsonPropertyName("task_group_id")]
        public string TaskGroupId { get; set; }
    }
    
    public class Data
    {
        [JsonPropertyName("task")]
        public TaskSchema Task { get; set; }
    }
    
    [JsonPropertyName("type")]
    public string Type { get; set; } = "COPILOT_TASK_CREATED";
    
    [JsonPropertyName("meta")]
    public Meta Meta { get; set; }
    
    [JsonPropertyName("data")]
    public Data Data { get; set; }
}

// When a copilot task is deleted
public class CopilotTaskDeletedMessage
{
    public class Meta
    {
        [JsonPropertyName("task_group_id")]
        public string TaskGroupId { get; set; }
    }
    
    public class Data
    {
        [JsonPropertyName("task_id")]
        public string TaskId { get; set; }
    }
    
    [JsonPropertyName("type")]
    public string Type { get; set; } = "COPILOT_TASK_DELETED";
    
    [JsonPropertyName("meta")]
    public Meta Meta { get; set; }
    
    [JsonPropertyName("data")]
    public Data Data { get; set; }
}
