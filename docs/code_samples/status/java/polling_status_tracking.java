import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.CookieHandler;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class PollingStatusTracking {
    public static void main(String[] args) throws Exception {
        String baseUrl = "http://dev-api.v2.areal.ai/api/v2";
        
        // 0. Login - details in Authentication section ... client is authenticated
        // 1. Starting the processing ... we get a request_id (which is actually the document_id) -- details in Processing section
        // 2. Polling the status of the processing
        
        String documentId = "your_document_id_here";
        
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        
        String status = getStatus(baseUrl, documentId);
        // NOTE: You can also inspect other fields in document_details as needed
        
        int timeout = 10;
        while (!status.equals("completed") && !status.equals("failed")) {
            Thread.sleep(1000);
            timeout--;
            if (timeout <= 0) {
                System.out.println("Timeout reached");
                break;
            }
            status = getStatus(baseUrl, documentId);
        }
    }
    
    private static String getStatus(String baseUrl, String documentId) {
        try {
            URL url = new URL(baseUrl + "/documents/" + documentId + "/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)
                );
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                JSONObject documentDetails = new JSONObject(response.toString());
                return documentDetails.getString("status");
            }
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("Error getting status: " + e.getMessage());
        }
        return "unknown";
    }
}
