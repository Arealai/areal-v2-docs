using System;
using System.Net.Http;
using System.Threading.Tasks;
using System.Text;
using System.Text.Json;
using System.IO;
using System.Collections.Generic;
using System.Linq;

var basePath = Directory.GetCurrentDirectory();
var baseUrl = "http://dev-api.v2.areal.ai/api/v2";

// 0. Login - details in Authentication section
// See code_samples/auth/c#/login.cs for authentication

var handler = new HttpClientHandler();
var client = new HttpClient(handler);
// Assume client is already authenticated with cookies from login

// 1. Get Pre-Signed URL's for a secure & fast upload channel
var fileNames = new[] { "sample.pdf" };
var presignedUrlRequest = new
{
    file_names = fileNames
};

var presignedUrlContent = new StringContent(
    JsonSerializer.Serialize(presignedUrlRequest),
    Encoding.UTF8,
    "application/json"
);

var presignedUrlResponse = await client.PostAsync($"{baseUrl}/processing/presigned_url/", presignedUrlContent);
presignedUrlResponse.EnsureSuccessStatusCode();

var presignedUrlResponseJson = await presignedUrlResponse.Content.ReadAsStringAsync();
var presignedUrlData = JsonSerializer.Deserialize<JsonElement>(presignedUrlResponseJson);

// 2. Upload to your PDF's to the PreSignedURL's
var presignedUrls = presignedUrlData.GetProperty("presigned_urls").EnumerateArray();
var uploadSessionId = presignedUrlData.GetProperty("upload_session_id").GetString();

foreach (var (fileName, presignedUrl) in fileNames.Zip(presignedUrls))
{
    var url = presignedUrl.GetProperty("url").GetString();
    var documentId = presignedUrl.GetProperty("document_id").GetString();
    var fields = presignedUrl.GetProperty("fields");
    
    using var uploadClient = new HttpClient();
    var formData = new MultipartFormDataContent();
    
    // Add fields from presigned URL
    foreach (var field in fields.EnumerateObject())
    {
        formData.Add(new StringContent(field.Value.GetString()), field.Name);
    }
    
    // Add file
    var fileBytes = await File.ReadAllBytesAsync(Path.Combine(basePath, fileName));
    formData.Add(new ByteArrayContent(fileBytes), "file", fileName);
    
    // 2. Upload to your PDF's to the PreSignedURL's
    var uploadResponse = await uploadClient.PostAsync(url, formData);
    uploadResponse.EnsureSuccessStatusCode();
    
    // 3. Start processing flow
    var processRequest = new
    {
        upload_session_id = uploadSessionId,
        document_id = documentId,
        file_name = fileName
    };
    
    var processContent = new StringContent(
        JsonSerializer.Serialize(processRequest),
        Encoding.UTF8,
        "application/json"
    );
    
    var processResponse = await client.PostAsync($"{baseUrl}/processing/upload/", processContent);
    processResponse.EnsureSuccessStatusCode();
}
