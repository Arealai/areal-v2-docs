import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.CookieManager;
import java.net.CookieHandler;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import org.json.JSONArray;

public class StartProcessing {
    public static void main(String[] args) throws Exception {
        String basePath = System.getProperty("user.dir");
        String baseUrl = "http://dev-api.v2.areal.ai/api/v2";
        
        // 0. Login - details in Authentication section
        // See code_samples/auth/java/login.java for authentication
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        
        // Assume client is already authenticated with cookies from login
        // For this example, we'll create a helper method to make authenticated requests
        
        // 1. Get Pre-Signed URL's for a secure & fast upload channel
        String[] fileNames = {"sample.pdf"};
        
        JSONObject presignedUrlRequest = new JSONObject();
        JSONArray fileNamesArray = new JSONArray();
        for (String fileName : fileNames) {
            fileNamesArray.put(fileName);
        }
        presignedUrlRequest.put("file_names", fileNamesArray);
        
        JSONObject presignedUrlResponse = makeAuthenticatedRequest(
            baseUrl + "/processing/presigned_url/",
            "POST",
            presignedUrlRequest.toString()
        );
        
        String uploadSessionId = presignedUrlResponse.getString("upload_session_id");
        JSONArray presignedUrls = presignedUrlResponse.getJSONArray("presigned_urls");
        
        for (int i = 0; i < fileNames.length; i++) {
            String fileName = fileNames[i];
            JSONObject presignedUrl = presignedUrls.getJSONObject(i);
            
            String url = presignedUrl.getString("url");
            String documentId = presignedUrl.getString("document_id");
            JSONObject fields = presignedUrl.getJSONObject("fields");
            
            // 2. Upload to your PDF's to the PreSignedURL's
            uploadToPresignedUrl(url, fields, Paths.get(basePath, fileName).toString());
            
            // 3. Start processing flow
            JSONObject processRequest = new JSONObject();
            processRequest.put("upload_session_id", uploadSessionId);
            processRequest.put("document_id", documentId);
            processRequest.put("file_name", fileName);
            
            JSONObject processResponse = makeAuthenticatedRequest(
                baseUrl + "/processing/upload/",
                "POST",
                processRequest.toString()
            );
        }
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
    
    private static void uploadToPresignedUrl(String urlString, JSONObject fields, String filePath) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        
        String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        
        try (OutputStream os = connection.getOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8), true)) {
            
            // Add fields
            for (String key : fields.keySet()) {
                writer.append("--").append(boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"").append(key).append("\"\r\n");
                writer.append("\r\n");
                writer.append(fields.getString(key)).append("\r\n");
            }
            
            // Add file
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(Paths.get(filePath).getFileName()).append("\"\r\n");
            writer.append("Content-Type: application/pdf\r\n");
            writer.append("\r\n");
            writer.flush();
            
            Files.copy(Paths.get(filePath), os);
            
            writer.append("\r\n");
            writer.append("--").append(boundary).append("--\r\n");
        }
        
        int responseCode = connection.getResponseCode();
        connection.disconnect();
    }
}
