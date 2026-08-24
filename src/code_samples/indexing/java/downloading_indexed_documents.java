import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.CookieHandler;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.json.JSONObject;
import org.json.JSONArray;

public class DownloadingIndexedDocuments {
    public static void main(String[] args) throws Exception {
        String sessionId = "66878f5e-c609-4796-9fc2-ecc6ae377cac";
        String baseUrl = "http://dev-api.v2.areal.ai/api/v2";
        
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        
        // Assume client is already authenticated
        
        // if you don't know your index_ids, fetch them from the session
        String sessionsUrl = baseUrl + "/sessions/" + sessionId + "/?group_by=index_id";
        URL url = new URL(sessionsUrl);
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
            
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray objects = jsonResponse.getJSONArray("objects");
            
            Set<String> indexIds = new HashSet<>();
            for (int i = 0; i < objects.length(); i++) {
                JSONObject obj = objects.getJSONObject(i);
                indexIds.add(obj.getString("index_id"));
            }
            
            // use them to filter documents by index_id
            Map<String, List<String>> indexToDocuments = new HashMap<>();
            for (String indexId : indexIds) {
                String filterUrl = baseUrl + "/sessions/" + sessionId + "/?group_by=index_id&filter_by.index_id=" + indexId;
                URL filterUrlObj = new URL(filterUrl);
                HttpURLConnection filterConnection = (HttpURLConnection) filterUrlObj.openConnection();
                filterConnection.setRequestMethod("GET");
                filterConnection.setDoInput(true);
                
                int filterResponseCode = filterConnection.getResponseCode();
                if (filterResponseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader filterReader = new BufferedReader(
                        new InputStreamReader(filterConnection.getInputStream(), StandardCharsets.UTF_8)
                    );
                    StringBuilder filterResponse = new StringBuilder();
                    while ((line = filterReader.readLine()) != null) {
                        filterResponse.append(line);
                    }
                    filterReader.close();
                    
                    JSONObject filterJson = new JSONObject(filterResponse.toString());
                    JSONArray filterObjects = filterJson.getJSONArray("objects");
                    
                    List<String> documentIds = new ArrayList<>();
                    for (int i = 0; i < filterObjects.length(); i++) {
                        JSONObject obj = filterObjects.getJSONObject(i);
                        documentIds.add(obj.getString("id"));
                    }
                    indexToDocuments.put(indexId, documentIds);
                }
                filterConnection.disconnect();
            }
            
            // then download them
            for (Map.Entry<String, List<String>> entry : indexToDocuments.entrySet()) {
                String indexId = entry.getKey();
                List<String> documentIds = entry.getValue();
                
                for (String documentId : documentIds) {
                    String documentUrl = baseUrl + "/documents/" + documentId + "/";
                    URL docUrlObj = new URL(documentUrl);
                    HttpURLConnection docConnection = (HttpURLConnection) docUrlObj.openConnection();
                    docConnection.setRequestMethod("GET");
                    docConnection.setDoInput(true);
                    
                    int docResponseCode = docConnection.getResponseCode();
                    if (docResponseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader docReader = new BufferedReader(
                            new InputStreamReader(docConnection.getInputStream(), StandardCharsets.UTF_8)
                        );
                        StringBuilder docResponse = new StringBuilder();
                        while ((line = docReader.readLine()) != null) {
                            docResponse.append(line);
                        }
                        docReader.close();
                        
                        JSONObject docJson = new JSONObject(docResponse.toString());
                        String pdfUrl = docJson.getString("pdf_url");
                        
                        // actual download
                        System.out.println("Downloading " + pdfUrl + "...");
                        URL pdfUrlObj = new URL(pdfUrl);
                        HttpURLConnection pdfConnection = (HttpURLConnection) pdfUrlObj.openConnection();
                        pdfConnection.setRequestMethod("GET");
                        pdfConnection.setDoInput(true);
                        
                        int pdfResponseCode = pdfConnection.getResponseCode();
                        if (pdfResponseCode == HttpURLConnection.HTTP_OK) {
                            // File download logic here - save to appropriate location
                            System.out.println("File downloaded successfully");
                        }
                        pdfConnection.disconnect();
                    }
                    docConnection.disconnect();
                }
            }
        }
        
        connection.disconnect();
    }
}
