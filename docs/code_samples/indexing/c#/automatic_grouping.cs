using System;
using System.Net.Http;
using System.Threading.Tasks;
using System.IO;
using System.Net;

var sessionId = "66878f5e-c609-4796-9fc2-ecc6ae377cac";
var baseUrl = "http://dev-api.v2.areal.ai/api/v2";

// Assume client is already authenticated
var handler = new HttpClientHandler
{
    UseCookies = true,
    CookieContainer = new CookieContainer()
};
var client = new HttpClient(handler);

var downloadUrl = $"{baseUrl}/sessions/{sessionId}/download/?group_by=index_id";
var response = await client.PostAsync(downloadUrl, null);
response.EnsureSuccessStatusCode();

var content = await response.Content.ReadAsByteArrayAsync();
await File.WriteAllBytesAsync("documents.zip", content);

Console.WriteLine("✅ Zip file downloaded and saved as documents.zip");
