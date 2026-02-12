# Indexing API

We support Indexing API for grouping your documents in a custom manner in ArealAI.
If your organization is using this feature, we will group your processed documents into your desired index format.

## Example Views

### Manage View

In ManageView you can move around the final documents within or across the indexes.
You can also duplicate the documents for easier organization.

![Example Manage View](../assets/indexing.png)




## Document Duplicatation

When you click on the "Duplicate" button for each document, we will create a new document with the same content.
We will add an indicator to reference the original document.
And you will be able to move this newly created document to a new index

## Downloading Indexed Documents

### Automatic Grouping

We provide an easy to use API to download the indexed documents in a zip file.
When `group_by` is set to `index_id`, we will put the documents in the same index into a folder.

=== "Python"

    ```py title="Automatic Grouping" linenums="1"
    --8<-- "code_samples/indexing/python/automatic_grouping.py"
    ```

=== "C#"

    ```csharp title="Automatic Grouping" linenums="1"
    --8<-- "code_samples/indexing/c#/automatic_grouping.cs"
    ```

=== "Java"

    ```java title="Automatic Grouping" linenums="1"
    --8<-- "code_samples/indexing/java/automatic_grouping.java"
    ```

### Manual Grouping
You can easily download the indexed documents by filtering the documents by the index you want to download.

=== "Python"

    ```py title="Downloading Indexed Documents" linenums="1"
    --8<-- "code_samples/indexing/python/downloading_indexed_documents.py"
    ```

=== "C#"

    ```csharp title="Downloading Indexed Documents" linenums="1"
    --8<-- "code_samples/indexing/c#/downloading_indexed_documents.cs"
    ```

=== "Java"

    ```java title="Downloading Indexed Documents" linenums="1"
    --8<-- "code_samples/indexing/java/downloading_indexed_documents.java"
    ```
