import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.CookieHandler;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import org.json.JSONArray;

public class GetLoanInfos {
    public static void main(String[] args) throws Exception {
        String baseUrl = "https://dev-api.v2.areal.ai/api/v2";
        
        // 0. Login - details in Authentication section
        // See code_samples/auth/java/login.java for authentication
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        
        // Assume client is already authenticated with cookies from login
        
        // 1. Get Loan Infos for a session
        String sessionId = "your-session-id";
        String queryParams = String.format(
            "session_id=%s&pageIndex=%d&pageSize=%d&sort=%s&sort_order=%s&sort_by=%s&search=%s",
            java.net.URLEncoder.encode(sessionId, StandardCharsets.UTF_8),
            0,
            20,
            java.net.URLEncoder.encode("updated_at.asc", StandardCharsets.UTF_8),
            java.net.URLEncoder.encode("asc", StandardCharsets.UTF_8),
            java.net.URLEncoder.encode("name", StandardCharsets.UTF_8),
            java.net.URLEncoder.encode("string", StandardCharsets.UTF_8) // Optional: search term
        );
        
        String requestUrl = baseUrl + "/resources/loan_infos/?" + queryParams;
        
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        
        // Cookies are automatically handled by CookieManager
        
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                
                JSONObject loanInfosResponse = new JSONObject(response.toString());
                
                // Process the response
                if (loanInfosResponse.has("meta")) {
                    JSONObject meta = loanInfosResponse.getJSONObject("meta");
                    if (meta.has("totalCount")) {
                        System.out.println("Total Count: " + meta.getInt("totalCount"));
                    }
                    if (meta.has("pageIndex")) {
                        System.out.println("Page Index: " + meta.getInt("pageIndex"));
                    }
                    if (meta.has("pageSize")) {
                        System.out.println("Page Size: " + meta.getInt("pageSize"));
                    }
                }
                
                // Process the loan infos
                if (loanInfosResponse.has("objects")) {
                    JSONArray objects = loanInfosResponse.getJSONArray("objects");
                    for (int i = 0; i < objects.length(); i++) {
                        JSONObject loanInfo = objects.getJSONObject(i);
                        String id = loanInfo.has("id") ? loanInfo.getString("id") : "";
                        String arlaName = loanInfo.has("arla_name") ? loanInfo.getString("arla_name") : "";
                        String value = loanInfo.has("value") ? loanInfo.getString("value") : "";
                        System.out.println("ID: " + id + ", ARLA Name: " + arlaName + ", Value: " + value);
                    }
                }
            }
        }
        
        connection.disconnect();
    }
}
