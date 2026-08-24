import java.net.HttpURLConnection;
import java.net.URL;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.CookieHandler;
import java.nio.charset.StandardCharsets;

public class AutomaticGrouping {
    public static void main(String[] args) throws Exception {
        String sessionId = "66878f5e-c609-4796-9fc2-ecc6ae377cac";
        String baseUrl = "http://dev-api.v2.areal.ai/api/v2";
        
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        
        // Assume client is already authenticated
        String downloadUrl = baseUrl + "/sessions/" + sessionId + "/download/?group_by=index_id";
        URL url = new URL(downloadUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (InputStream inputStream = connection.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream("documents.zip")) {
                
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            System.out.println("✅ Zip file downloaded and saved as documents.zip");
        }
        
        connection.disconnect();
    }
}
