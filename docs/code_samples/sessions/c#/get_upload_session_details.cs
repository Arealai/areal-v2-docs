using System;
using System.Net.Http;
using System.Threading.Tasks;
using System.Text.Json;
using System.Net;
using System.Collections.Generic;

var baseUrl = "http://dev-api.v2.areal.ai/api/v2";

// 0. Login -> 1. Create UploadSession 
// See code_samples/auth/c#/login.cs for authentication

var handler = new HttpClientHandler
{
    UseCookies = true,
    CookieContainer = new CookieContainer()
};
var client = new HttpClient(handler);

// 2. List UploadSessions for your organization
var sessionsUrl = $"{baseUrl}/sessions/?sort_by=updated_at&sort_order=desc";
var listSessionsResponse = await client.GetAsync(sessionsUrl);
listSessionsResponse.EnsureSuccessStatusCode();

var listSessionsContent = await listSessionsResponse.Content.ReadAsStringAsync();
var listSessionsJson = JsonDocument.Parse(listSessionsContent);
var sessions = listSessionsJson.RootElement.GetProperty("objects").EnumerateArray();

string? sessionId = null;
foreach (var session in sessions)
{
    sessionId = session.GetProperty("id").GetString();
    var name = session.GetProperty("name").GetString();
    var updatedAt = session.GetProperty("updated_at").GetString();
    Console.WriteLine($"UploadSession: {sessionId} - {name} - {updatedAt}");
}

// 3. Get documents in an UploadSession
if (sessionId != null)
{
    var sessionDetailsUrl = $"{baseUrl}/sessions/{sessionId}";
    var sessionDetailsResponse = await client.GetAsync(sessionDetailsUrl);
    sessionDetailsResponse.EnsureSuccessStatusCode();
    
    var sessionDetailsContent = await sessionDetailsResponse.Content.ReadAsStringAsync();
    var sessionDetailsJson = JsonDocument.Parse(sessionDetailsContent);
    var documents = sessionDetailsJson.RootElement.GetProperty("objects").EnumerateArray();
    
    foreach (var document in documents)
    {
        var docId = document.GetProperty("id").GetString();
        var docName = document.GetProperty("name").GetString();
        var docStatus = document.GetProperty("status").GetString();
        Console.WriteLine($"{docId} - {docName} - {docStatus}");
    }
}
