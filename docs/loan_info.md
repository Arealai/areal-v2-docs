# LoanInfo API

The LoanInfo API allows you to get a consolidated view of a mortgage loan.

After you processed your document with our API, you can both inspect the extracted data for each document
or you can get `LoanInfo` for the entire upload session.

!!! info "Triangulation of LoanInfo"
    We automatically triangulate the LoanInfo from each document in the session, so you don't need to call an API to get the LoanInfo.

## Example Usage

=== "Python"

    ```py title="Get Loan Infos" linenums="1"
    --8<-- "code_samples/loan_info/python/get_loan_infos.py"
    ```

=== "C#"

    ```csharp title="Get Loan Infos" linenums="1"
    --8<-- "code_samples/loan_info/c#/get_loan_infos.cs"
    ```

=== "Java"

    ```java title="Get Loan Infos" linenums="1"
    --8<-- "code_samples/loan_info/java/get_loan_infos.java"
    ```