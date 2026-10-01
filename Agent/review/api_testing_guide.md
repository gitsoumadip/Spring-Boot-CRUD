# API Testing Guide — Postman Request & Response

> [!NOTE]
> **CURRENT MODE: TOKEN AUTHENTICATION IS DISABLED**
> All APIs below can be called directly **without any `Authorization` header or token**.
> The Keycloak JWT instructions (Step 0) are saved for when you set up Keycloak in the future.

## Step 0: Get JWT Token from Keycloak (Optional / Future)

### Request — Get ADMIN Token
```
POST http://localhost:8090/realms/myrealm/protocol/openid-connect/token
```
| Header | Value |
|---|---|
| Content-Type | `application/x-www-form-urlencoded` |

| Body (form-data) | Value |
|---|---|
| grant_type | `password` |
| client_id | `your-client-id` |
| username | `admin-user` |
| password | `admin-password` |

### Response — `200 OK`
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "scope": "openid profile email"
}
```

> [!TIP]
> In Postman, copy the `access_token` value. For all subsequent requests, go to **Authorization** tab → Type: **Bearer Token** → paste the token.

---

## 1. Public Endpoints (No Token Needed)

### 1.1 GET `/actuator/health`

**Request:**
```
GET http://localhost:8090/actuator/health
```
| Header | Value |
|---|---|
| *(none required)* | |

**Response — `200 OK`**
```json
{
  "status": "UP"
}
```

---

### 1.2 GET `/api/auth/test` (any path under `/api/auth/**`)

**Request:**
```
GET http://localhost:8090/api/auth/test
```
| Header | Value |
|---|---|
| *(none required)* | |

**Response — `404 Not Found`** *(no controller mapped, but NOT 401 — proves auth is skipped)*
```json
{
  "timestamp": "2026-10-01T09:00:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "path": "/api/auth/test"
}
```

---

## 2. Unauthenticated Requests (No Token → 401)

### 2.1 GET `/api/users/hello` — No Token

**Request:**
```
GET http://localhost:8090/api/users/hello
```
| Header | Value |
|---|---|
| *(no Authorization header)* | |

**Response — `401 Unauthorized`**
```json
{
  "timestamp": "2026-10-01T09:00:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "path": "/api/users/hello"
}
```

---

### 2.2 GET `/api/users/all` — No Token

**Request:**
```
GET http://localhost:8090/api/users/all
```
| Header | Value |
|---|---|
| *(no Authorization header)* | |

**Response — `401 Unauthorized`**
```json
{
  "timestamp": "2026-10-01T09:00:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "path": "/api/users/all"
}
```

---

### 2.3 GET `/db-test` — No Token

**Request:**
```
GET http://localhost:8090/db-test
```
| Header | Value |
|---|---|
| *(no Authorization header)* | |

**Response — `401 Unauthorized`**
```json
{
  "timestamp": "2026-10-01T09:00:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "path": "/db-test"
}
```

---

## 3. ADMIN Role — Full Access

> [!IMPORTANT]
> All requests below require: `Authorization: Bearer <ADMIN_TOKEN>`

### 3.1 GET `/api/users/hello`

**Request:**
```
GET http://localhost:8090/api/users/hello
```
| Header | Value |
|---|---|
| Authorization | `Bearer eyJhbGciOi...` |

**Response — `200 OK`**
```
Hello, World!
```

---

### 3.2 GET `/api/users/login`

**Request:**
```
GET http://localhost:8090/api/users/login
```
| Header | Value |
|---|---|
| Authorization | `Bearer <ADMIN_TOKEN>` |

**Response — `200 OK`**
```
User Login
```

---

### 3.3 GET `/api/users/uid/42`

**Request:**
```
GET http://localhost:8090/api/users/uid/42
```
| Header | Value |
|---|---|
| Authorization | `Bearer <ADMIN_TOKEN>` |

**Response — `200 OK`**
```
User ID: 42
```

---

### 3.4 GET `/api/users/all`

**Request:**
```
GET http://localhost:8090/api/users/all
```
| Header | Value |
|---|---|
| Authorization | `Bearer <ADMIN_TOKEN>` |

**Response — `200 OK`**
```
All User IDs
```

---

### 3.5 POST `/api/users/create`

**Request:**
```
POST http://localhost:8090/api/users/create
```
| Header | Value |
|---|---|
| Authorization | `Bearer <ADMIN_TOKEN>` |
| Content-Type | `application/json` |

**Response — `404 Not Found`** *(No POST handler exists yet, but NOT 403 — authorization passed ✅)*
```json
{
  "timestamp": "2026-10-01T09:00:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "path": "/api/users/create"
}
```

---

### 3.6 PUT `/api/users/1`

**Request:**
```
PUT http://localhost:8090/api/users/1
```
| Header | Value |
|---|---|
| Authorization | `Bearer <ADMIN_TOKEN>` |
| Content-Type | `application/json` |

**Response — `404 Not Found`** *(No PUT handler yet, but NOT 403 ✅)*
```json
{
  "timestamp": "2026-10-01T09:00:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "path": "/api/users/1"
}
```

---

### 3.7 DELETE `/api/users/1`

**Request:**
```
DELETE http://localhost:8090/api/users/1
```
| Header | Value |
|---|---|
| Authorization | `Bearer <ADMIN_TOKEN>` |

**Response — `404 Not Found`** *(No DELETE handler yet, but NOT 403 ✅)*
```json
{
  "timestamp": "2026-10-01T09:00:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "path": "/api/users/1"
}
```

---

### 3.8 GET `/db-test`

**Request:**
```
GET http://localhost:8090/db-test
```
| Header | Value |
|---|---|
| Authorization | `Bearer <ADMIN_TOKEN>` |

**Response — `200 OK`**
```
PostgreSQL connected successfully! Database: forest_budget
```

---

## 4. OFFICER Role — Read-Only `/api/users/**`

> [!IMPORTANT]
> All requests below require: `Authorization: Bearer <OFFICER_TOKEN>`

### 4.1 GET `/api/users/hello` ✅

**Request:**
```
GET http://localhost:8090/api/users/hello
```
| Header | Value |
|---|---|
| Authorization | `Bearer <OFFICER_TOKEN>` |

**Response — `200 OK`**
```
Hello, World!
```

---

### 4.2 GET `/api/users/all` ✅

**Request:**
```
GET http://localhost:8090/api/users/all
```
| Header | Value |
|---|---|
| Authorization | `Bearer <OFFICER_TOKEN>` |

**Response — `200 OK`**
```
All User IDs
```

---

### 4.3 GET `/api/users/uid/1` ✅

**Request:**
```
GET http://localhost:8090/api/users/uid/1
```
| Header | Value |
|---|---|
| Authorization | `Bearer <OFFICER_TOKEN>` |

**Response — `200 OK`**
```
User ID: 1
```

---

### 4.4 POST `/api/users/create` ❌

**Request:**
```
POST http://localhost:8090/api/users/create
```
| Header | Value |
|---|---|
| Authorization | `Bearer <OFFICER_TOKEN>` |
| Content-Type | `application/json` |

**Response — `403 Forbidden`**
```json
{
  "timestamp": "2026-10-01T09:00:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "path": "/api/users/create"
}
```

---

### 4.5 PUT `/api/users/1` ❌

**Request:**
```
PUT http://localhost:8090/api/users/1
```
| Header | Value |
|---|---|
| Authorization | `Bearer <OFFICER_TOKEN>` |
| Content-Type | `application/json` |

**Response — `403 Forbidden`**
```json
{
  "timestamp": "2026-10-01T09:00:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "path": "/api/users/1"
}
```

---

### 4.6 DELETE `/api/users/1` ❌

**Request:**
```
DELETE http://localhost:8090/api/users/1
```
| Header | Value |
|---|---|
| Authorization | `Bearer <OFFICER_TOKEN>` |

**Response — `403 Forbidden`**
```json
{
  "timestamp": "2026-10-01T09:00:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "path": "/api/users/1"
}
```

---

### 4.7 GET `/db-test` ✅

**Request:**
```
GET http://localhost:8090/db-test
```
| Header | Value |
|---|---|
| Authorization | `Bearer <OFFICER_TOKEN>` |

**Response — `200 OK`**
```
PostgreSQL connected successfully! Database: forest_budget
```

---

## 5. Regular User (No ADMIN/OFFICER Role) — Minimal Access

> [!IMPORTANT]
> All requests below require: `Authorization: Bearer <USER_TOKEN>` (user with no ADMIN/OFFICER realm role)

### 5.1 GET `/api/users/hello` ❌

**Request:**
```
GET http://localhost:8090/api/users/hello
```
| Header | Value |
|---|---|
| Authorization | `Bearer <USER_TOKEN>` |

**Response — `403 Forbidden`**
```json
{
  "timestamp": "2026-10-01T09:00:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "path": "/api/users/hello"
}
```

---

### 5.2 POST `/api/users/create` ❌

**Request:**
```
POST http://localhost:8090/api/users/create
```
| Header | Value |
|---|---|
| Authorization | `Bearer <USER_TOKEN>` |
| Content-Type | `application/json` |

**Response — `403 Forbidden`**
```json
{
  "timestamp": "2026-10-01T09:00:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "path": "/api/users/create"
}
```

---

### 5.3 GET `/db-test` ✅

**Request:**
```
GET http://localhost:8090/db-test
```
| Header | Value |
|---|---|
| Authorization | `Bearer <USER_TOKEN>` |

**Response — `200 OK`**
```
PostgreSQL connected successfully! Database: forest_budget
```

---

## 6. Edge Cases — Invalid Tokens

### 6.1 Invalid Token

**Request:**
```
GET http://localhost:8090/api/users/hello
```
| Header | Value |
|---|---|
| Authorization | `Bearer invalid.token.value` |

**Response — `401 Unauthorized`**
```json
{
  "timestamp": "2026-10-01T09:00:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "path": "/api/users/hello"
}
```

---

### 6.2 Expired Token

**Request:**
```
GET http://localhost:8090/api/users/hello
```
| Header | Value |
|---|---|
| Authorization | `Bearer <EXPIRED_TOKEN>` |

**Response — `401 Unauthorized`**
```json
{
  "timestamp": "2026-10-01T09:00:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "An error occurred while attempting to decode the Jwt: Jwt expired",
  "path": "/api/users/hello"
}
```

---

## Postman Setup Cheat Sheet

### Postman Environment Variables
Set these in your Postman environment for reuse:

| Variable | Value |
|---|---|
| `base_url` | `http://localhost:8090` |
| `keycloak_url` | `http://localhost:8090/realms/myrealm` |
| `client_id` | `your-client-id` |
| `admin_token` | *(auto-set from token request)* |
| `officer_token` | *(auto-set from token request)* |
| `user_token` | *(auto-set from token request)* |

### Auto-Save Token in Postman
Add this **Tests** script to your token request to auto-save the token:

```javascript
var jsonData = pm.response.json();
pm.environment.set("admin_token", jsonData.access_token);
```

Then use `{{admin_token}}` in the Authorization tab of subsequent requests.

---

## Quick Reference Matrix

| # | Endpoint | Method | No Token | Regular User | OFFICER | ADMIN |
|---|---|---|:---:|:---:|:---:|:---:|
| 1 | `/actuator/health` | GET | ✅ 200 | ✅ 200 | ✅ 200 | ✅ 200 |
| 2 | `/api/auth/**` | ANY | ✅ 200/404 | ✅ 200/404 | ✅ 200/404 | ✅ 200/404 |
| 3 | `/api/users/hello` | GET | ❌ 401 | ❌ 403 | ✅ 200 | ✅ 200 |
| 4 | `/api/users/login` | GET | ❌ 401 | ❌ 403 | ✅ 200 | ✅ 200 |
| 5 | `/api/users/uid/{id}` | GET | ❌ 401 | ❌ 403 | ✅ 200 | ✅ 200 |
| 6 | `/api/users/all` | GET | ❌ 401 | ❌ 403 | ✅ 200 | ✅ 200 |
| 7 | `/api/users/**` | POST | ❌ 401 | ❌ 403 | ❌ 403 | ✅ 200/404 |
| 8 | `/api/users/**` | PUT | ❌ 401 | ❌ 403 | ❌ 403 | ✅ 200/404 |
| 9 | `/api/users/**` | DELETE | ❌ 401 | ❌ 403 | ❌ 403 | ✅ 200/404 |
| 10 | `/db-test` | GET | ❌ 401 | ✅ 200 | ✅ 200 | ✅ 200 |
