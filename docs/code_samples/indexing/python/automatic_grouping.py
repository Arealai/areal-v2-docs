import requests  # noqa

SESSION_ID = '66878f5e-c609-4796-9fc2-ecc6ae377cac'

response = client.post(
    f'{BASE_URL}/sessions/{SESSION_ID}/download/', params={'group_by': 'index_id'}
)
with open('documents.zip', 'wb') as f:
    f.write(response.content)

print('✅ Zip file downloaded and saved as documents.zip')
