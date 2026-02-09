using System;
using System.Net.Http;
using System.Threading.Tasks;
using System.Text.Json;
using System.Collections.Generic;
using System.Linq;

var baseUrl = "https://dev-api.v2.areal.ai/api/v2";

// 0. Login - details in Authentication section
// See code_samples/auth/c#/login.cs for authentication
var handler = new HttpClientHandler
{
    UseCookies = true,
    CookieContainer = new CookieContainer()
};
var client = new HttpClient(handler);
// Assume client is already authenticated with cookies from login

// 1. Get Loan Infos for a session
var sessionId = "your-session-id";
var queryParams = new List<KeyValuePair<string, string>>
{
    new("session_id", sessionId),
    new("pageIndex", "0"),
    new("pageSize", "20"),
    new("sort", "updated_at.asc"),
    new("sort_order", "asc"),
    new("sort_by", "name"),
    new("search", "string") // Optional: search term
};

var queryString = string.Join("&", queryParams.Select(p => $"{Uri.EscapeDataString(p.Key)}={Uri.EscapeDataString(p.Value)}"));
var requestUrl = $"{baseUrl}/resources/loan_infos/?{queryString}";

var request = new HttpRequestMessage(HttpMethod.Get, requestUrl);
request.Headers.Add("Accept", "application/json");

var response = await client.SendAsync(request);
response.EnsureSuccessStatusCode();

var responseContent = await response.Content.ReadAsStringAsync();
var loanInfosData = JsonSerializer.Deserialize<JsonElement>(responseContent);

// Process the response
if (loanInfosData.TryGetProperty("meta", out var meta))
{
    if (meta.TryGetProperty("totalCount", out var totalCount))
        Console.WriteLine($"Total Count: {totalCount.GetInt32()}");
    if (meta.TryGetProperty("pageIndex", out var pageIndex))
        Console.WriteLine($"Page Index: {pageIndex.GetInt32()}");
    if (meta.TryGetProperty("pageSize", out var pageSize))
        Console.WriteLine($"Page Size: {pageSize.GetInt32()}");
}

// Process the loan infos
if (loanInfosData.TryGetProperty("objects", out var objects))
{
    foreach (var loanInfo in objects.EnumerateArray())
    {
        var id = loanInfo.TryGetProperty("id", out var idProp) ? idProp.GetString() : "";
        var arlaName = loanInfo.TryGetProperty("arla_name", out var arlaNameProp) ? arlaNameProp.GetString() : "";
        var value = loanInfo.TryGetProperty("value", out var valueProp) ? valueProp.GetString() : "";
        Console.WriteLine($"ID: {id}, ARLA Name: {arlaName}, Value: {value}");
    }
}
