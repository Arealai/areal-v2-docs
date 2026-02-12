import requests
from pathlib import Path

BASE_PATH = Path(__file__).parent
BASE_URL = 'http://dev-api.v2.areal.ai/api/v2'

# 0. Login - details in Authentication section
...

# 1. Get Pre-Signed URL's for a secure & fast upload channel
file_names = ['sample.pdf']
presigned_url_response = client.post(
    f'{BASE_URL}/processing/presigned_url/',
    # NOTE: you can upload to an existing session by passing the upload_session_id
    # but we recommend creating a new session for each loan
    # params={'upload_session_id': upload_session_id},
    json={'file_names': file_names},
).json()

# 2. Upload to your PDF's to the PreSignedURL's
for file_name, presigned_url in zip(
    file_names, presigned_url_response['presigned_urls']
):
    upload_response = requests.post(
        presigned_url['url'],
        data=presigned_url['fields'],
        files={'file': (BASE_PATH / file_name).read_bytes()},
    )

    # 3. Start processing flow
    process_response = client.post(
        f'{BASE_URL}/processing/upload/',
        json={
            'upload_session_id': presigned_url_response['upload_session_id'],
            'document_id': presigned_url['document_id'],
            'file_name': file_name,
        },
    )
