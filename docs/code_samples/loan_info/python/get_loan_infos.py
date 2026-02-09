import requests

BASE_URL = 'https://dev-api.v2.areal.ai/api/v2'

# 0. Login - details in Authentication section
# See code_samples/auth/python/login.py for authentication

# 1. Get Loan Infos for a session
session_id = 'your-session-id'
loan_infos_response = client.get(
    f'{BASE_URL}/resources/loan_infos/',
    params={
        'session_id': session_id,
        'pageIndex': 0,
        'pageSize': 20,
        'sort': 'updated_at.asc',
        'sort_order': 'asc',
        'sort_by': 'name',
        'search': 'string',  # Optional: search term
    },
    headers={
        'Accept': 'application/json',
    }
).json()

# Process the response
meta = loan_infos_response.get('meta', {})
print(f"Total Count: {meta.get('totalCount')}")
print(f"Page Index: {meta.get('pageIndex')}")
print(f"Page Size: {meta.get('pageSize')}")

# Process the loan infos
for loan_info in loan_infos_response.get('objects', []):
    print(f"ID: {loan_info.get('id')}, ARLA Name: {loan_info.get('arla_name')}, Value: {loan_info.get('value')}")
