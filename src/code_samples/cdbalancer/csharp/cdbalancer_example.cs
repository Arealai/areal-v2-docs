using System;
using System.Net.Http;
using System.Threading.Tasks;
using System.Text;
using System.Text.Json;
using System.Net;

var baseUrl = "http://dev-api.v2.areal.ai/api/v2";

// 0. Login - details in Authentication section
// See code_samples/auth/c#/login.cs for authentication

var handler = new HttpClientHandler
{
    UseCookies = true,
    CookieContainer = new CookieContainer()
};
var client = new HttpClient(handler);

// 1. Process Documents that you are interested in balancing
// -- For this, please refer to the Processing section
// See code_samples/processing/c#/start_processing.cs

// 2. Call CDBalancer API using existing processed documents
var cdbalancerRequest = new
{
    document_id_1 = "doc1_id",
    document_id_2 = "doc2_id"
};

var cdbalancerContent = new StringContent(
    JsonSerializer.Serialize(cdbalancerRequest),
    Encoding.UTF8,
    "application/json"
);

var cdbalancerResponse = await client.PostAsync($"{baseUrl}/cdbalancer/", cdbalancerContent);
cdbalancerResponse.EnsureSuccessStatusCode();

var cdbalancerResponseJson = await cdbalancerResponse.Content.ReadAsStringAsync();
var cdbalancerData = JsonSerializer.Deserialize<JsonElement>(cdbalancerResponseJson);
var requestId = cdbalancerData.GetProperty("request_id").GetString();
