using System;
using System.Text.Json.Serialization;

// When an existing documents status changes (e.g processing -> completed)
public class FileStatusUpdatedMessage
{
    public class Meta
    {
        [JsonPropertyName("upload_session_id")]
        public string UploadSessionId { get; set; }
        
        [JsonPropertyName("id")]
        public string Id { get; set; }
        
        [JsonPropertyName("user")]
        public User User { get; set; }
    }
    
    public class Data
    {
        [JsonPropertyName("status")]
        public string Status { get; set; }
    }
    
    [JsonPropertyName("type")]
    public string Type { get; set; } = "FILE_STATUS_UPDATED";
    
    [JsonPropertyName("meta")]
    public Meta Meta { get; set; }
    
    [JsonPropertyName("data")]
    public Data Data { get; set; }
}

// When a new document is created (e.g. preparing can generate multiple documents)
public class FileCreatedMessage
{
    public class Meta
    {
        [JsonPropertyName("upload_session_id")]
        public string UploadSessionId { get; set; }
        
        [JsonPropertyName("user")]
        public User User { get; set; }
    }
    
    [JsonPropertyName("type")]
    public string Type { get; set; } = "FILE_CREATED";
    
    [JsonPropertyName("meta")]
    public Meta Meta { get; set; }
    
    [JsonPropertyName("data")]
    public SessionDetailListItem Data { get; set; }
}

// When a document is removed
public class FileDeletedMessage
{
    public class Meta
    {
        [JsonPropertyName("upload_session_id")]
        public string UploadSessionId { get; set; }
        
        [JsonPropertyName("id")]
        public string Id { get; set; }
        
        [JsonPropertyName("user")]
        public User User { get; set; }
    }
    
    [JsonPropertyName("type")]
    public string Type { get; set; } = "FILE_DELETED";
    
    [JsonPropertyName("meta")]
    public Meta Meta { get; set; }
    
    [JsonPropertyName("data")]
    public object Data { get; set; } = null;
}

public class User
{
    [JsonPropertyName("id")]
    public string Id { get; set; }
}

// When a document is pushed to a gateway (e.g. Encompass DocPush)
public class DocPushFinishedMessage
{
    public class Meta
    {
        [JsonPropertyName("upload_session_id")]
        public string UploadSessionId { get; set; }
        
        [JsonPropertyName("id")]
        public string Id { get; set; }
        
        [JsonPropertyName("user")]
        public User User { get; set; }
    }
    
    public class Data
    {
        [JsonPropertyName("status")]
        public string Status { get; set; }
        
        [JsonPropertyName("error")]
        public string Error { get; set; }
    }
    
    [JsonPropertyName("type")]
    public string Type { get; set; } = "DOC_PUSH_FINISHED";
    
    [JsonPropertyName("meta")]
    public Meta Meta { get; set; }
    
    [JsonPropertyName("data")]
    public Data Data { get; set; }
}
