# Status Tracking

**Document processing** is an asynchronous process. Therefore, we need a way to track the status of the processing.
There are 2 ways to track the status of the processing:

- **WebSockets** (recommended)
- **Polling** (alternative)

## WebSockets

WebSockets are a more efficient way to track the status of the processing.
It is a long-lived connection that allows you to receive updates as they happen.

=== "Python"

    ```py title="WebSockets Example" linenums="1"
    --8<-- "code_samples/status/python/websocket_status_tracking.py"
    ```

=== "C#"

    ```csharp title="WebSockets Example" linenums="1"
    --8<-- "code_samples/status/c#/websocket_status_tracking.cs"
    ```

=== "Java"

    ```java title="WebSockets Example" linenums="1"
    --8<-- "code_samples/status/java/websocket_status_tracking.java"
    ```

For other WebSocket notification types see [WebSocket API](websocket.md)


## Polling

Continuously poll the status of the processing until it is completed or failed.
You can also implement a custom timeout to avoid waiting forever.

=== "Python"

    ```py title="Long Polling Example" linenums="1"
    --8<-- "code_samples/status/python/polling_status_tracking.py"
    ```

=== "C#"

    ```csharp title="Long Polling Example" linenums="1"
    --8<-- "code_samples/status/c#/polling_status_tracking.cs"
    ```

=== "Java"

    ```java title="Long Polling Example" linenums="1"
    --8<-- "code_samples/status/java/polling_status_tracking.java"
    ```

## Meaning of the status

- **pending**: We received your request, but processing has not started yet.
- **preparing**: The document is being classified.
- **processing**: Data extraction is in progress.
- **completed**: Processing is complete and successful.
- **failed**: Processing failed. Please check the error details or try again.


## WebSocket API

### Document Processing & DocPush

=== "Python"

    ```py title="Document Status Tracking" linenums="1"
    --8<-- "code_samples/status/python/document_status_models.py"
    ```

=== "C#"

    ```csharp title="Document Status Tracking" linenums="1"
    --8<-- "code_samples/status/c#/document_status_models.cs"
    ```

=== "Java"

    ```java title="Document Status Tracking" linenums="1"
    --8<-- "code_samples/status/java/document_status_models.java"
    ```

---

### CDBalancer & CDPush/Pull

=== "Python"

    ```py title="CDBalancer Status Tracking" linenums="1"
    --8<-- "code_samples/status/python/cdbalancer_status_models.py"
    ```

=== "C#"

    ```csharp title="CDBalancer Status Tracking" linenums="1"
    --8<-- "code_samples/status/c#/cdbalancer_status_models.cs"
    ```

=== "Java"

    ```java title="CDBalancer Status Tracking" linenums="1"
    --8<-- "code_samples/status/java/cdbalancer_status_models.java"
    ```

---

### Copilot Tasks

=== "Python"

    ```py title="Copilot Tasks Status Tracking" linenums="1"
    --8<-- "code_samples/status/python/copilot_task_models.py"
    ```

=== "C#"

    ```csharp title="Copilot Tasks Status Tracking" linenums="1"
    --8<-- "code_samples/status/c#/copilot_task_models.cs"
    ```

=== "Java"

    ```java title="Copilot Tasks Status Tracking" linenums="1"
    --8<-- "code_samples/status/java/copilot_task_models.java"
    ```