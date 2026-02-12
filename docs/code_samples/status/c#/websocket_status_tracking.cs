using System;
using System.Net.WebSockets;
using System.Text;
using System.Text.Json;
using System.Threading;
using System.Threading.Tasks;

var baseUrl = "ws://dev-api.v2.areal.ai/";
var documentId = "your_document_id_here";

using var client = new ClientWebSocket();
await client.ConnectAsync(new Uri(baseUrl + "status/"), CancellationToken.None);

var buffer = new byte[1024 * 4];

while (client.State == WebSocketState.Open)
{
    var result = await client.ReceiveAsync(new ArraySegment<byte>(buffer), CancellationToken.None);
    
    if (result.MessageType == WebSocketMessageType.Text)
    {
        var message = Encoding.UTF8.GetString(buffer, 0, result.Count);
        var jsonDoc = JsonDocument.Parse(message);
        var root = jsonDoc.RootElement;
        
        var meta = root.GetProperty("meta");
        
        // skip if not the document we are interested in
        if (meta.GetProperty("id").GetString() != documentId)
        {
            continue;
        }
        
        var data = root.GetProperty("data");
        var status = data.GetProperty("status").GetString();
        Console.WriteLine($"Processing {meta.GetProperty("id").GetString()} is {status}");
        
        // NOTE: no need to break since if this is background task
        // and so you can keep listening for other documents
    }
    else if (result.MessageType == WebSocketMessageType.Close)
    {
        await client.CloseAsync(WebSocketCloseStatus.NormalClosure, "", CancellationToken.None);
    }
}
