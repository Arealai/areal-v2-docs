# Code Examples for Migration

## Authentication

```python
import requests

base_url = "https://dev-api.v2.areal.ai/api/v2"
session = requests.Session()

# 1. Login
login_payload = {
    "email": "your@email.com",
    "password": "your_password"
}
login_response = session.post(f"{base_url}/accounts/login/", json=login_payload)
login_response.raise_for_status()
# Tokens are set in secure HTTP-only cookies automatically
print("Login successful!", login_response.cookies)

# 2. Get Current User (Me)
me_response = session.get(f"{base_url}/accounts/me/")
me_response.raise_for_status()
print("Current user info:", me_response.json())

# 3. Refresh Session
refresh_response = session.post(f"{base_url}/accounts/refresh/")
refresh_response.raise_for_status()
print("Session refreshed!", refresh_response.cookies)

# 4. Logout
logout_response = session.post(f"{base_url}/accounts/logout/")
logout_response.raise_for_status()
print("Logged out.")
```

## Document Processing

```python


```