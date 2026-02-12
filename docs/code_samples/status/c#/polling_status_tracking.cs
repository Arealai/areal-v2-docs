using System;
using System.Net.Http;
using System.Threading.Tasks;
using System.Text.Json;
using System.Net;
using System.Threading;

var baseUrl = "http://dev-api.v2.areal.ai/api/v2";

// 0. Login - details in Authentication section ... client is authenticated
// 1. Starting the processing ... we get a request_id (which is actually the document_id) -- details in Processing section
// 2. Polling the status of the processing

var documentId = "your_document_id_here";

var handler = new HttpClientHandler
{
    UseCookies = true,
    CookieContainer = new CookieContainer()
};
var client = new HttpClient(handler);

string status = await GetStatus(client, baseUrl, documentId);
// NOTE: You can also inspect other fields in document_details as needed

int timeout = 10;
while (status != "completed" && status != "failed")
{
    await Task.Delay(1000);
    timeout--;
    if (timeout <= 0)
    {
        Console.WriteLine("Timeout reached");
        break;
    }
    status = await GetStatus(client, baseUrl, documentId);
}

static async Task<string> GetStatus(HttpClient client, string baseUrl, string documentId)
{
    try
    {
        var response = await client.GetAsync($"{baseUrl}/documents/{documentId}/");
        response.EnsureSuccessStatusCode();
        
        var content = await response.Content.ReadAsStringAsync();
        var jsonDoc = JsonDocument.Parse(content);
        return jsonDoc.RootElement.GetProperty("status").GetString();
    }
    catch (Exception e)
    {
        Console.WriteLine($"Error getting status: {e.Message}");
        return "unknown";
    }
}
