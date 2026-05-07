# Game Store API - Digital Game Catalog

This is a professional backend system for a digital game store, implemented using the Spring Boot framework. The application provides a robust architecture for managing games, developers, and categories, featuring secure authentication and advanced search capabilities.

## System Requirements

To build and run the application, ensure your environment meets the following specifications:

* **Java Development Kit (JDK):** Version 21 (LTS).
* **Build Tool:** Maven (Wrapper included in the project).
* **Lombok:** This project uses Project Lombok. Ensure your IDE has the Lombok plugin installed and "Annotation Processing" is enabled in the compiler settings.
* **Database:** H2 (In-memory), no external database installation is required for development.

## Getting Started

### Installation and Execution

1. Clone the repository:
   ```bash
   git clone [repository-url]
   ```

2. Navigate to the project root:
   ```bash
   cd game-store-api
   ```

3. Run the application using the Maven Wrapper:
   * **Linux/macOS:** `./mvnw spring-boot:run`
   * **Windows:** `mvnw.cmd spring-boot:run`

The server will start on port `8081`.

## Authentication and Security

The API uses stateless JWT (JSON Web Token) authentication.

### Default Credentials
Upon initial startup, the system seeds a default administrator account:
* **Username:** admin
* **Password:** admin

### Using the Token
To access protected endpoints (POST, PUT, DELETE), you must include the JWT token in the HTTP request header:
`Authorization: Bearer <your_jwt_token>`

## API Reference

### 1. Authentication and Accounts

| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| POST | `/api/auth/login` | Public | Authenticates credentials and returns a JWT token. |
| POST | `/api/accounts/register` | Public | Registers a new user account with `ROLE_USER`. |

### 2. Games Management

| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| GET | `/api/games` | Public | Returns a list of games. Supports filtering via query params: `title`, `minPrice`, `maxPrice`, `developerId`. |
| POST | `/api/games` | Admin | Creates a new game. Requires a full `GameRequestDto` body. |
| PUT | `/api/games/{id}` | Admin | Updates an existing game by ID. |
| DELETE | `/api/games/{id}` | Admin | Removes a game from the catalog. |

### 3. Developers (Producers)

| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| GET | `/api/developers` | Public | Lists all game developers registered in the system. |
| POST | `/api/developers` | Admin | Adds a new developer profile (Name, Country, Website, etc.). |
| PUT | `/api/developers/{id}` | Admin | Updates developer information. |
| DELETE | `/api/developers/{id}` | Admin | Deletes a developer profile. |

### 4. Categories

| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| GET | `/api/categories` | Public | Returns all available game categories (e.g., RPG, FPS). |
| POST | `/api/categories` | Admin | Adds a new category to the dictionary. |
| PUT | `/api/categories/{id}` | Admin | Renames an existing category. |
| DELETE | `/api/categories/{id}` | Admin | Removes a category. |

## Data Models

### GameRequestDto Structure
To create or update a game, send a JSON object with the following fields:
```json
{
  "title": "String",
  "description": "String",
  "price": "BigDecimal",
  "releaseDate": "YYYY-MM-DD",
  "developerId": "Long",
  "categoryIds": ["Long", "Long"]
}
```

## Technical Details

### Architecture
The project follows **Domain-Driven Design (DDD)** principles and **SOLID** patterns:
* **Controller Layer:** Handles REST requests and DTO mapping.
* **Service Layer:** Contains business logic and transactional boundaries.
* **Repository Layer:** Manages data persistence using Spring Data JPA.
* **Security:** Custom JWT Filter integrated into the Spring Security Filter Chain.

### Testing
Unit and integration tests are located in `src/test/java`.
To execute the test suite:
```bash
./mvnw test
```
The project aims for 100% logic coverage, testing both success paths and error handling (404 Not Found, 401 Unauthorized, 400 Bad Request).

### Database Console
The H2 Console for inspecting the in-memory database is accessible at:
`http://localhost:8081/h2-console`
* **JDBC URL:** `jdbc:h2:mem:gamedb`
* **User:** `sa`
* **Password:** (leave empty)


# API Testing Guide

A step-by-step walkthrough for testing the API using **Postman** or **Insomnia**. Follow the steps in order so the database relations are created correctly (dictionaries first, then the game).

---

## Authentication Header

For all protected endpoints, add the following header:

| Key | Value |
|-----|-------|
| `Authorization` | `Bearer <YOUR_TOKEN_HERE>` |

---

## Step 1 — Get Token (Login as Admin)

**POST** `http://localhost:8081/api/auth/login`

> Public endpoint — no token required.

```json
{
  "login": "admin",
  "password": "admin"
}
```

Copy the returned token and use it in the `Authorization` header for all protected steps below.

---

## Step 2 — Add Categories *(requires token)*

**POST** `http://localhost:8081/api/categories`

Run this request **twice** to create two categories (they will receive IDs `1` and `2`).

**First request:**
```json
{
  "name": "RPG"
}
```

**Second request:**
```json
{
  "name": "Action"
}
```

---

## Step 3 — Add Developer *(requires token)*

**POST** `http://localhost:8081/api/developers`

```json
{
  "name": "CD Projekt Red",
  "description": "Polska firma tworzaca gry",
  "websiteUrl": "https://cdprojekt.com",
  "country": "Poland",
  "foundationYear": 1994
}
```

The developer will receive ID `1`.

---

## Step 4 — Add Game *(requires token)*

**POST** `http://localhost:8081/api/games`

Uses the IDs generated in the previous steps (`developerId: 1`, `categoryIds: [1, 2]`).

```json
{
  "title": "Wiedźmin 3: Dziki Gon",
  "description": "Epicka gra RPG w swiecie dark fantasy",
  "price": 99.99,
  "releaseDate": "2015-05-19",
  "developerId": 1,
  "categoryIds": [1, 2]
}
```

---

## Step 5 — Update Game *(requires token)*

**PUT** `http://localhost:8081/api/games/1`

```json
{
  "title": "Wiedźmin 3: Edycja Gry Roku",
  "description": "Wersja ze wszystkimi dodatkami",
  "price": 149.99,
  "releaseDate": "2016-08-30",
  "developerId": 1,
  "categoryIds": [1]
}
```

---

## Step 6 — Search & Filter Games *(public)*

**GET** `http://localhost:8081/api/games`

> No token required.

| Description | URL |
|-------------|-----|
| All games | `http://localhost:8081/api/games` |
| Search by title | `http://localhost:8081/api/games?title=Wiedźmin` |
| Filter by max price | `http://localhost:8081/api/games?maxPrice=100.00` |
| Filter by developer | `http://localhost:8081/api/games?developerId=1` |
| Combined parameters | `http://localhost:8081/api/games?minPrice=50.00&maxPrice=200.00&title=Wiedźmin` |

---

## Step 7 — Register New User *(public)*

**POST** `http://localhost:8081/api/accounts/register`

> No token required.

```json
{
  "login": "nowy_gracz",
  "password": "mojetajnehaslo123"
}
```

---

## Step 8 — Test Authorization (Login as Regular User)

**POST** `http://localhost:8081/api/auth/login`

```json
{
  "login": "nowy_gracz",
  "password": "mojetajnehaslo123"
}
```

Copy the new token and try to perform a **DELETE** request:

**DELETE** `http://localhost:8081/api/games/1`

The application should reject this request and return **`403 Forbidden`**.

> Regular users have the `ROLE_USER` role. Deleting resources requires `ROLE_ADMIN`.
