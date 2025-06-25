# Authentication

Areal API uses a secure, modern authentication system to ensure that only authorized users can access sensitive resources. Our approach follows industry best practices, prioritizing both user experience and robust security.

## Key Principles

- **Secure Login:** Credentials are never stored or transmitted in plain text.
- **Token-Based Authentication:** Access and refresh tokens are used to manage sessions securely.
- **Cookie Usage:** Secure, HTTP-only cookies are used to store tokens, protecting them from XSS attacks.
- **Session Refresh:** Seamless token refresh ensures uninterrupted user experience.
- **Logout:** Users can securely terminate their sessions at any time.
- **Best-Practice Security:** All authentication flows use encryption, secure cookie flags, and protection against common web vulnerabilities.

---

## Authentication Flow

Below is a high-level sequence diagram illustrating the main phases of the authentication lifecycle:

```mermaid
sequenceDiagram
    participant User as 👤 User
    participant Frontend as 🖥️ Areal Dashboard
    participant API as 🖥️ Areal API

    %% Phase 1: Login
    rect rgb(220,255,220)
    User->>Frontend: Enter credentials
    Frontend->>API: POST /login (credentials)
    API-->>Frontend: Set secure cookies (access & refresh tokens)
    Frontend-->>User: Login success
    end

    %% Phase 2: Authenticated Request
    rect rgb(200,220,255)
    User->>Frontend: Perform action
    Frontend->>API: Authenticated request (cookies sent automatically)
    API-->>Frontend: Protected resource/data
    end

    %% Phase 3: Token Refresh
    rect rgb(255,240,200)
    Frontend->>API: Token expired, use refresh token (cookie)
    API-->>Frontend: Issue new access token (cookie)
    end

    %% Phase 4: Logout
    rect rgb(255,220,220)
    User->>Frontend: Click logout
    Frontend->>API: POST /logout
    API-->>Frontend: Clear cookies
    Frontend-->>User: Logged out
    end
```

---

## Video Walkthrough

> _[Insert your video here to demonstrate the authentication flow in action.]_

---

## Security Highlights

- **All tokens are stored in secure, HTTP-only cookies** to prevent client-side access.
- **Tokens are short-lived** and automatically refreshed to minimize risk.
- **Logout fully invalidates the session** and clears all authentication cookies.
- **All communication is encrypted** using HTTPS.
- **Industry-standard libraries and protocols** are used for authentication and session management.

---

For technical integration details see [Accounts](https://dev-api.v2.areal.ai/api/v2/docs#tag/accounts).