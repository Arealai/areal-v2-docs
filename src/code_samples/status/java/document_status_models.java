import org.json.JSONObject;

// When an existing documents status changes (e.g processing -> completed)
class FileStatusUpdatedMessage {
    public static class Meta {
        public String upload_session_id;
        public String id;
        public User user;
    }
    
    public static class Data {
        public String status;
    }
    
    public String type = "FILE_STATUS_UPDATED";
    public Meta meta;
    public Data data;
}

// When a new document is created (e.g. preparing can generate multiple documents)
class FileCreatedMessage {
    public static class Meta {
        public String upload_session_id;
        public User user;
    }
    
    public String type = "FILE_CREATED";
    public Meta meta;
    public SessionDetailListItem data;
}

// When a document is removed
class FileDeletedMessage {
    public static class Meta {
        public String upload_session_id;
        public String id;
        public User user;
    }
    
    public String type = "FILE_DELETED";
    public Meta meta;
    public Object data = null;
}

class User {
    public String id;
}

// When a document is pushed to a gateway (e.g. Encompass DocPush)
class DocPushFinishedMessage {
    public static class Meta {
        public String upload_session_id;
        public String id;
        public User user;
    }
    
    public static class Data {
        public String status;
        public String error;
    }
    
    public String type = "DOC_PUSH_FINISHED";
    public Meta meta;
    public Data data;
}
