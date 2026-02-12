# Email Notifications & Processing

We support both event-based email notifications and document upload over email.

## Email Notifications

We support email notifications for document processing.

You can configure email notifications for the following events:

- Document Processing
- CDBalancer
- Copilot Tasks (coming soon)

### Example Usage

=== "Python"

    ```py title="Configure Email Notifications" linenums="1"
    --8<-- "code_samples/email/python/configure_email_notifications.py"
    ```

=== "C#"

    ```csharp title="Configure Email Notifications" linenums="1"
    --8<-- "code_samples/email/c#/configure_email_notifications.cs"
    ```

=== "Java"

    ```java title="Configure Email Notifications" linenums="1"
    --8<-- "code_samples/email/java/configure_email_notifications.java"
    ```

## Email Processing

We support processing over email. You can send a PDF to our designated email address via attachments and we will process it as if you uploaded it via the API.

!!! info "Your users email must match the sender"
    For us to securely process your documents, we need to verify that the email is sent from your users. So please make sure that your Areal user's email matches the sender of the email.

![Email Processing](../assets/email-processing.png)

!!! warning "Attachments is all you need"
    We only care about the attachments in the email. Body will be ignored.
    Your email body will be ignored.

### Example Usage

=== "Python"

    ```py title="Processing over email" linenums="1"
    --8<-- "code_samples/email/python/processing_over_email.py"
    ```

=== "C#"

    ```csharp title="Processing over email" linenums="1"
    --8<-- "code_samples/email/c#/processing_over_email.cs"
    ```

=== "Java"

    ```java title="Processing over email" linenums="1"
    --8<-- "code_samples/email/java/processing_over_email.java"
    ```