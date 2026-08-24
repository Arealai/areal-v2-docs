using System;
using System.Net.Http;
using System.Threading.Tasks;
using System.Text;
using System.Text.Json;
using System.Net;
using System.Collections.Generic;

var baseUrl = "http://dev-api.v2.areal.ai/api/v2";

// same as processing/upload/
// with additional loan_info field for the loan info

var handler = new HttpClientHandler
{
    UseCookies = true,
    CookieContainer = new CookieContainer()
};
var client = new HttpClient(handler);

// 0. Get Pre-Signed URL's see Processing section for full details
// See code_samples/processing/c#/start_processing.cs for full details
// Assume presigned_url_response and presigned_url are already obtained
// from the processing section

// 1. Create LoanInfo
var loanInfo = new
{
    parties = new[]
    {
        new { name = "Notary Test", role = "notary", id = "notary-test-id" },
        new { name = "John Doe", role = "signer", id = "john-test-id" },
        new { name = "Jane Smith", role = "witness", id = "jane-test-id" }
    },
    transaction = new { annotation_transaction_type = "bulk" }
};

// 1. Start processing flow
var processRequest = new
{
    upload_session_id = presignedUrlResponse["upload_session_id"],
    document_id = presignedUrl["document_id"],
    file_name = fileName,
    loan_info = loanInfo
};

var processContent = new StringContent(
    JsonSerializer.Serialize(processRequest),
    Encoding.UTF8,
    "application/json"
);

var processResponse = await client.PostAsync($"{baseUrl}/processing/upload/", processContent);
processResponse.EnsureSuccessStatusCode();
