using System;
using System.Text.Json.Serialization;

// When the status of the CDBalancer changes (e.g. preparing -> processing -> completed)
public class CDBalancerStatusUpdatedMessage
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
    public string Type { get; set; } = "CDBALANCER_STATUS_UPDATED";
    
    [JsonPropertyName("meta")]
    public Meta Meta { get; set; }
    
    [JsonPropertyName("data")]
    public Data Data { get; set; }
}

// When a CDPull is started (e.g. Encompass CDPull)
public class CDPullStartedMessage
{
    public class Meta
    {
        [JsonPropertyName("upload_session_id")]
        public string UploadSessionId { get; set; }
        
        [JsonPropertyName("user")]
        public User User { get; set; }
    }
    
    public class Data
    {
        public class Document
        {
            [JsonPropertyName("id")]
            public string Id { get; set; }
            
            [JsonPropertyName("name")]
            public string Name { get; set; }
            
            [JsonPropertyName("status")]
            public string Status { get; set; }
        }
        
        [JsonPropertyName("document")]
        public Document Document { get; set; }
    }
    
    [JsonPropertyName("type")]
    public string Type { get; set; } = "CD_PULL_STARTED";
    
    [JsonPropertyName("meta")]
    public Meta Meta { get; set; }
    
    [JsonPropertyName("data")]
    public Data Data { get; set; }
}

// When a CDPull is finished (e.g. Encompass CDPull)
public class CDPullFinishedMessage
{
    public class Meta
    {
        [JsonPropertyName("upload_session_id")]
        public string UploadSessionId { get; set; }
        
        [JsonPropertyName("user")]
        public User User { get; set; }
    }
    
    public class Data
    {
        public class Document
        {
            [JsonPropertyName("id")]
            public string Id { get; set; }
            
            [JsonPropertyName("name")]
            public string Name { get; set; }
            
            [JsonPropertyName("status")]
            public string Status { get; set; }
        }
        
        [JsonPropertyName("document")]
        public Document Document { get; set; }
        
        [JsonPropertyName("status")]
        public string Status { get; set; }
        
        [JsonPropertyName("error")]
        public string Error { get; set; }
    }
    
    [JsonPropertyName("type")]
    public string Type { get; set; } = "CD_PULL_FINISHED";
    
    [JsonPropertyName("meta")]
    public Meta Meta { get; set; }
    
    [JsonPropertyName("data")]
    public Data Data { get; set; }
}

// When a CDPush is finished (e.g. Encompass CDPush)
public class CDPushFinishedMessage
{
    public class Meta
    {
        [JsonPropertyName("upload_session_id")]
        public string UploadSessionId { get; set; }
        
        [JsonPropertyName("cdbalancer_request_id")]
        public string CDBalancerRequestId { get; set; }
        
        [JsonPropertyName("gateway_request_id")]
        public string GatewayRequestId { get; set; }
        
        [JsonPropertyName("user")]
        public User User { get; set; }
    }
    
    public class Data
    {
        [JsonPropertyName("status")]
        public string Status { get; set; }
        
        [JsonPropertyName("error")]
        public string Error { get; set; }
        
        [JsonPropertyName("stats_message")]
        public string StatsMessage { get; set; }
        
        [JsonPropertyName("stats_status")]
        public string StatsStatus { get; set; }
    }
    
    [JsonPropertyName("type")]
    public string Type { get; set; } = "CD_PUSH_FINISHED";
    
    [JsonPropertyName("meta")]
    public Meta Meta { get; set; }
    
    [JsonPropertyName("data")]
    public Data Data { get; set; }
}
