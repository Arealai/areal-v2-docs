import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.CookieHandler;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class CDBalancerExample {
    public static void main(String[] args) throws Exception {
        String baseUrl = "http://dev-api.v2.areal.ai/api/v2";
        
        // 0. Login - details in Authentication section
        // See code_samples/auth/java/login.java for authentication
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        
        // 1. Process Documents that you are interested in balancing
        // -- For this, please refer to the Processing section
        // See code_samples/processing/java/start_processing.java
        
        // 2. Call CDBalancer API using existing processed documents
        JSONObject cdbalancerRequest = new JSONObject();
        cdbalancerRequest.put("document_id_1", "doc1_id");
        cdbalancerRequest.put("document_id_2", "doc2_id");
        
        JSONObject cdbalancerResponse = makeAuthenticatedRequest(
            baseUrl + "/cdbalancer/",
            "POST",
            cdbalancerRequest.toString()
        );
        
        String requestId = cdbalancerResponse.getString("request_id");
    }
    
    private static JSONObject makeAuthenticatedRequest(String urlString, String method, String body) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        
        // Cookies are automatically handled by CookieManager
        if (body != null) {
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }
        
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return new JSONObject(response.toString());
            }
        }
        
        connection.disconnect();
        return null;
    }
}
