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

public class RunCopilotTask {
    public static void main(String[] args) throws Exception {
        String baseUrl = "http://dev-api.v2.areal.ai/api/v2";
        String sessionId = "your-loan-id";

        // 0. Login - details in Authentication section
        // See code_samples/auth/java/login.java for authentication
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        // 1. List Copilot agents (task groups) for this loan
        JSONObject agents = makeAuthenticatedRequest(
            baseUrl + "/copilot/task-group/?session_id=" + sessionId,
            "GET",
            null
        );
        JSONArray agentList = agents.getJSONArray("objects");
        String taskGroupId = null;
        for (int i = 0; i < agentList.length(); i++) {
            JSONObject agent = agentList.getJSONObject(i);
            taskGroupId = agent.getString("id");
            System.out.println(taskGroupId + " - " + agent.getString("name") + " - " + agent.getString("status"));
        }

        // 2. List tasks in an agent
        JSONObject tasks = makeAuthenticatedRequest(
            baseUrl + "/copilot/task-group/" + taskGroupId + "/tasks/?session_id=" + sessionId,
            "GET",
            null
        );
        JSONArray taskList = tasks.getJSONArray("objects");
        String taskId = null;
        for (int i = 0; i < taskList.length(); i++) {
            JSONObject task = taskList.getJSONObject(i);
            taskId = task.getString("id");
            String runStatus = task.getJSONObject("results").optString("status", "n/a");
            System.out.println(taskId + " - " + task.getString("name") + " - " + runStatus);
        }

        // 3. Run a task against this loan
        JSONObject executeBody = new JSONObject();
        executeBody.put("session_id", sessionId);
        JSONObject executeResponse = makeAuthenticatedRequest(
            baseUrl + "/copilot/task/" + taskId + "/execute/",
            "POST",
            executeBody.toString()
        );
        System.out.println(executeResponse);
    }

    private static JSONObject makeAuthenticatedRequest(String urlString, String method, String body) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoInput(true);

        if (body != null) {
            connection.setDoOutput(true);
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
