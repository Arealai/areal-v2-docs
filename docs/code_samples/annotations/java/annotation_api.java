import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.CookieHandler;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import org.json.JSONArray;

public class AnnotationAPI {
    public static void main(String[] args) throws Exception {
        String baseUrl = "http://dev-api.v2.areal.ai/api/v2";
        
        // same as processing/upload/
        // with additional loan_info field for the loan info
        
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        
        // 0. Get Pre-Signed URL's see Processing section for full details
        // See code_samples/processing/java/start_processing.java for full details
        // Assume presigned_url_response and presigned_url are already obtained
        // from the processing section
        
        // 1. Create LoanInfo
        JSONObject loanInfo = new JSONObject();
        
        JSONArray parties = new JSONArray();
        
        JSONObject notary = new JSONObject();
        notary.put("name", "Notary Test");
        notary.put("role", "notary");
        notary.put("id", "notary-test-id");
        parties.put(notary);
        
        JSONObject signer = new JSONObject();
        signer.put("name", "John Doe");
        signer.put("role", "signer");
        signer.put("id", "john-test-id");
        parties.put(signer);
        
        JSONObject witness = new JSONObject();
        witness.put("name", "Jane Smith");
        witness.put("role", "witness");
        witness.put("id", "jane-test-id");
        parties.put(witness);
        
        loanInfo.put("parties", parties);
        
        JSONObject transaction = new JSONObject();
        transaction.put("annotation_transaction_type", "bulk");
        loanInfo.put("transaction", transaction);
        
        // 1. Start processing flow
        JSONObject processRequest = new JSONObject();
        processRequest.put("upload_session_id", presignedUrlResponse.getString("upload_session_id"));
        processRequest.put("document_id", presignedUrl.getString("document_id"));
        processRequest.put("file_name", fileName);
        processRequest.put("loan_info", loanInfo);
        
        JSONObject processResponse = makeAuthenticatedRequest(
            baseUrl + "/processing/upload/",
            "POST",
            processRequest.toString()
        );
    }
    
    private static JSONObject makeAuthenticatedRequest(String urlString, String method, String body) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        
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
