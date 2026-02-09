import requests

BASE_URL = 'http://dev-api.v2.areal.ai/api/v2'

# 0. Login - details in Authentication section
login_response = requests.post(f'{BASE_URL}/accounts/login/', {
    'username': 'test@areal.ai',
    'password': 'test123',
})
client = requests.Session()
client.cookies.update(
    {
        'access_token': login_response.cookies['access_token'],
        'refresh_token': login_response.cookies['refresh_token'],
    }
)
# this client is now authenticated for the duration of access_token
# after that you can refresh it using the /accounts/refresh endpoint
