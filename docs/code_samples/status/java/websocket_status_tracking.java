import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;
import org.json.JSONObject;

public class WebSocketStatusTracking {
    public static void main(String[] args) throws Exception {
        String baseUrl = "ws://dev-api.v2.areal.ai/";
        String documentId = "your_document_id_here";
        
        HttpClient client = HttpClient.newHttpClient();
        WebSocket webSocket = client.newWebSocketBuilder()
            .buildAsync(URI.create(baseUrl + "status/"), new WebSocket.Listener() {
                @Override
                public void onOpen(WebSocket webSocket) {
                    System.out.println("WebSocket connection opened");
                    webSocket.request(1);
                    return WebSocket.Listener.super.onOpen(webSocket);
                }
                
                @Override
                public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                    try {
                        JSONObject message = new JSONObject(data.toString());
                        JSONObject meta = message.getJSONObject("meta");
                        
                        // skip if not the document we are interested in
                        if (!meta.getString("id").equals(documentId)) {
                            webSocket.request(1);
                            return null;
                        }
                        
                        JSONObject messageData = message.getJSONObject("data");
                        String status = messageData.getString("status");
                        System.out.println("Processing " + meta.getString("id") + " is " + status);
                        
                        // NOTE: no need to break since if this is background task
                        // and so you can keep listening for other documents
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    webSocket.request(1);
                    return null;
                }
                
                @Override
                public void onError(WebSocket webSocket, Throwable error) {
                    System.err.println("WebSocket error: " + error.getMessage());
                }
            })
            .join();
        
        // Keep the connection alive
        Thread.sleep(Long.MAX_VALUE);
    }
}
