import requests  # noqa

SESSION_ID = '66878f5e-c609-4796-9fc2-ecc6ae377cac'

# if you don't know your index_ids, fetch them from the session
response = client.get(
    f'{BASE_URL}/sessions/{SESSION_ID}/', 
    params={'group_by': 'index_id'}
)
response.raise_for_status()
index_ids = list(set(o['index_id'] for o in response.json()['objects']))

# use them to filter documents by index_id
index_to_documents: dict[str, list[str]] = {}
for index_id in index_ids:
    response = client.get(
        f'{BASE_URL}/sessions/{SESSION_ID}/', 
        params={'group_by': 'index_id', 'filter_by.index_id': index_id}
    )
    response.raise_for_status()
    document_ids = [o['id'] for o in response.json()['objects']]
    index_to_documents[index_id] = document_ids

# then download them
for index_id, document_ids in index_to_documents.items():
    pdf_urls = []
    for document_id in document_ids:
        response = client.get(f'{BASE_URL}/documents/{document_id}/')
        response.raise_for_status()
        pdf_url = response.json()['pdf_url']
        
        # actual download
        print(f'Downloading {pdf_url}...')
        response = requests.get(pdf_url)
        response.raise_for_status()
        print('File downloaded successfully')
