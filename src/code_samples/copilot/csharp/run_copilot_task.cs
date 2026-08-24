using System;
using System.Net.Http;
using System.Threading.Tasks;
using System.Text;
using System.Text.Json;
using System.Net;

var baseUrl = "http://dev-api.v2.areal.ai/api/v2";
var sessionId = "your-loan-id";

// 0. Login - details in Authentication section
// See code_samples/auth/c#/login.cs for authentication

var handler = new HttpClientHandler
{
    UseCookies = true,
    CookieContainer = new CookieContainer()
};
var client = new HttpClient(handler);

// 1. List Copilot agents (task groups) for this loan
var agentsUrl = $"{baseUrl}/copilot/task-group/?session_id={sessionId}";
var agentsResponse = await client.GetAsync(agentsUrl);
agentsResponse.EnsureSuccessStatusCode();

var agentsJson = JsonDocument.Parse(await agentsResponse.Content.ReadAsStringAsync());
string? taskGroupId = null;
foreach (var agent in agentsJson.RootElement.GetProperty("objects").EnumerateArray())
{
    taskGroupId = agent.GetProperty("id").GetString();
    var name = agent.GetProperty("name").GetString();
    var status = agent.GetProperty("status").GetString();
    Console.WriteLine($"{taskGroupId} - {name} - {status}");
}

// 2. List tasks in an agent
if (taskGroupId is null)
{
    return;
}

var tasksUrl = $"{baseUrl}/copilot/task-group/{taskGroupId}/tasks/?session_id={sessionId}";
var tasksResponse = await client.GetAsync(tasksUrl);
tasksResponse.EnsureSuccessStatusCode();

var tasksJson = JsonDocument.Parse(await tasksResponse.Content.ReadAsStringAsync());
string? taskId = null;
foreach (var task in tasksJson.RootElement.GetProperty("objects").EnumerateArray())
{
    taskId = task.GetProperty("id").GetString();
    var name = task.GetProperty("name").GetString();
    var runStatus = task.GetProperty("results").GetProperty("status").GetString();
    Console.WriteLine($"{taskId} - {name} - {runStatus}");
}

// 3. Run a task against this loan
if (taskId is null)
{
    return;
}

var executeContent = new StringContent(
    JsonSerializer.Serialize(new { session_id = sessionId }),
    Encoding.UTF8,
    "application/json"
);
var executeResponse = await client.PostAsync($"{baseUrl}/copilot/task/{taskId}/execute/", executeContent);
executeResponse.EnsureSuccessStatusCode();
Console.WriteLine(await executeResponse.Content.ReadAsStringAsync());
