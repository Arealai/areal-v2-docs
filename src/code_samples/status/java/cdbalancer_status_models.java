// When the status of the CDBalancer changes (e.g. preparing -> processing -> completed)
class CDBalancerStatusUpdatedMessage {
    public static class Meta {
        public String upload_session_id;
        public String id;
        public User user;
    }
    
    public static class Data {
        public String status;
    }
    
    public String type = "CDBALANCER_STATUS_UPDATED";
    public Meta meta;
    public Data data;
}

// When a CDPull is started (e.g. Encompass CDPull)
class CDPullStartedMessage {
    public static class Meta {
        public String upload_session_id;
        public User user;
    }
    
    public static class Data {
        public static class Document {
            public String id;
            public String name;
            public String status;
        }
        
        public Document document;
    }
    
    public String type = "CD_PULL_STARTED";
    public Meta meta;
    public Data data;
}

// When a CDPull is finished (e.g. Encompass CDPull)
class CDPullFinishedMessage {
    public static class Meta {
        public String upload_session_id;
        public User user;
    }
    
    public static class Data {
        public static class Document {
            public String id;
            public String name;
            public String status;
        }
        
        public Document document;
        public String status;
        public String error;
    }
    
    public String type = "CD_PULL_FINISHED";
    public Meta meta;
    public Data data;
}

// When a CDPush is finished (e.g. Encompass CDPush)
class CDPushFinishedMessage {
    public static class Meta {
        public String upload_session_id;
        public String cdbalancer_request_id;
        public String gateway_request_id;
        public User user;
    }
    
    public static class Data {
        public String status;
        public String error;
        public String stats_message;
        public String stats_status;
    }
    
    public String type = "CD_PUSH_FINISHED";
    public Meta meta;
    public Data data;
}
