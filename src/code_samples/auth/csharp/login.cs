using System;
using System.Net.Http;
using System.Threading.Tasks;
using System.Text;
using System.Text.Json;
using System.Net;

var baseUrl = "http://dev-api.v2.areal.ai/api/v2";

// 0. Login - details in Authentication section
var loginData = new
{
    username = "test@areal.ai",
    password = "test123"
};

var handler = new HttpClientHandler
{
    UseCookies = true,
    CookieContainer = new CookieContainer()
};
var client = new HttpClient(handler);

var loginContent = new StringContent(
    JsonSerializer.Serialize(loginData),
    Encoding.UTF8,
    "application/json"
);

var loginResponse = await client.PostAsync($"{baseUrl}/accounts/login/", loginContent);
loginResponse.EnsureSuccessStatusCode();

// Cookies (access_token and refresh_token) are automatically stored in handler.CookieContainer
// This client is now authenticated for the duration of access_token
// after that you can refresh it using the /accounts/refresh endpoint
