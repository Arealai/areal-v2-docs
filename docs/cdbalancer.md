# Closing Disclosure Balancing

CDBalancer takes 2 processed Closing Disclosure documents and *balances* them.

!!! warning "Asynchronous CDBalancer"
    The entire flow is asynchronous, meaning that when you start the cdbalancer we will respond with a __request_id__ which you can use to track of the status of the cdbalancer request. 
    
    While users of Areal Dashboard can easily see the live status of their cdbalancer requests.
    
    So if you are planning to integrate our API, you can use our WebSocket API or manually poll the status of the cdbalancer request.
    
    See [Status Tracking](processing/status.md) for more details.

## Example Usage

=== "Python"

    ```py title="CDBalancer Example Usage" linenums="1"
    --8<-- "code_samples/cdbalancer/python/cdbalancer_example.py"
    ```

=== "C#"

    ```csharp title="CDBalancer Example Usage" linenums="1"
    --8<-- "code_samples/cdbalancer/c#/cdbalancer_example.cs"
    ```

=== "Java"

    ```java title="CDBalancer Example Usage" linenums="1"
    --8<-- "code_samples/cdbalancer/java/cdbalancer_example.java"
    ```
