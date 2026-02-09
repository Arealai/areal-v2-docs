import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.CookieHandler;
import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;

public class Login {
    public static void main(String[] args) throws Exception {
        String baseUrl = "http://dev-api.v2.areal.ai/api/v2";
        
        // 0. Login - details in Authentication section
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        
        URL url = new URL(baseUrl + "/accounts/login/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        
        String loginData = "{\"username\":\"test@areal.ai\",\"password\":\"test123\"}";
        
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = loginData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Cookies are automatically stored in the CookieManager
            // this client is now authenticated for the duration of access_token
            // after that you can refresh it using the /accounts/refresh endpoint
        }
        
        connection.disconnect();
    }
}
