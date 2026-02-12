# same as processing/upload/
# with additional loan_info field for the loan info

# 0. Get Pre-Signed URL's see Processing section for full details
...

# 1. Create LoanInfo
loan_info = {
    'parties': [
        {'name': 'Notary Test', 'role': 'notary', 'id': 'notary-test-id'},
        {'name': 'John Doe', 'role': 'signer', 'id': 'john-test-id'},
        {'name': 'Jane Smith', 'role': 'witness', 'id': 'jane-test-id'}
    ],
    'transaction': {'annotation_transaction_type': 'bulk'}
}

# 1. Start processing flow
process_response = client.post(
    f'{BASE_URL}/processing/upload/',
    json={
        'upload_session_id': presigned_url_response['upload_session_id'],
        'document_id': presigned_url['document_id'],
        'file_name': file_name,
        'loan_info': loan_info,
    },
)
