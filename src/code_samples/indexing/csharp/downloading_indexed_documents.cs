using System;
using System.Net.Http;
using System.Threading.Tasks;
using System.Text.Json;
using System.Net;
using System.Collections.Generic;
using System.Linq;

var sessionId = "66878f5e-c609-4796-9fc2-ecc6ae377cac";
var baseUrl = "http://dev-api.v2.areal.ai/api/v2";

// Assume client is already authenticated
var handler = new HttpClientHandler
{
    UseCookies = true,
    CookieContainer = new CookieContainer()
};
var client = new HttpClient(handler);

// if you don't know your index_ids, fetch them from the session
var sessionsUrl = $"{baseUrl}/sessions/{sessionId}/?group_by=index_id";
var response = await client.GetAsync(sessionsUrl);
response.EnsureSuccessStatusCode();

var content = await response.Content.ReadAsStringAsync();
var jsonDoc = JsonDocument.Parse(content);
var objects = jsonDoc.RootElement.GetProperty("objects").EnumerateArray();

var indexIds = new HashSet<string>();
foreach (var obj in objects)
{
    indexIds.Add(obj.GetProperty("index_id").GetString());
}

// use them to filter documents by index_id
var indexToDocuments = new Dictionary<string, List<string>>();
foreach (var indexId in indexIds)
{
    var filterUrl = $"{baseUrl}/sessions/{sessionId}/?group_by=index_id&filter_by.index_id={indexId}";
    var filterResponse = await client.GetAsync(filterUrl);
    filterResponse.EnsureSuccessStatusCode();
    
    var filterContent = await filterResponse.Content.ReadAsStringAsync();
    var filterJson = JsonDocument.Parse(filterContent);
    var filterObjects = filterJson.RootElement.GetProperty("objects").EnumerateArray();
    
    var documentIds = new List<string>();
    foreach (var obj in filterObjects)
    {
        documentIds.Add(obj.GetProperty("id").GetString());
    }
    indexToDocuments[indexId] = documentIds;
}

// then download them
foreach (var (indexId, documentIds) in indexToDocuments)
{
    foreach (var documentId in documentIds)
    {
        var documentUrl = $"{baseUrl}/documents/{documentId}/";
        var docResponse = await client.GetAsync(documentUrl);
        docResponse.EnsureSuccessStatusCode();
        
        var docContent = await docResponse.Content.ReadAsStringAsync();
        var docJson = JsonDocument.Parse(docContent);
        var pdfUrl = docJson.RootElement.GetProperty("pdf_url").GetString();
        
        // actual download
        Console.WriteLine($"Downloading {pdfUrl}...");
        var pdfResponse = await client.GetAsync(pdfUrl);
        pdfResponse.EnsureSuccessStatusCode();
        Console.WriteLine("File downloaded successfully");
    }
}
