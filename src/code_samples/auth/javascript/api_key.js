const BASE_URL = 'https://dev-api.v2.areal.ai/api/v2';
const API_KEY = process.env.AREAL_API_KEY;

if (!API_KEY) {
  throw new Error('Set AREAL_API_KEY in the environment');
}

// Preferred: Authorization: Bearer <api_key>
const meResponse = await fetch(`${BASE_URL}/accounts/me/`, {
  headers: {
    Authorization: `Bearer ${API_KEY}`,
  },
});

if (!meResponse.ok) {
  throw new Error(`Auth failed: ${meResponse.status} ${await meResponse.text()}`);
}

const me = await meResponse.json();
console.log(me);

// Alternative: custom `key` header (ArealAPIKeyHeader)
const altResponse = await fetch(`${BASE_URL}/accounts/me/`, {
  headers: {
    key: API_KEY,
  },
});

console.log(await altResponse.json());
