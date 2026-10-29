# Recipe & Nutrition App

A recipe and nutritional value management application built with a Client-Server architecture connected via RESTful API.

---

## Project Structure

```text
Recipe-Nutrition-App-Final/
├── backend/    # REST API built with Spring Boot 3 & Java 17
├── frontend/   # Native Android Application (Java, Gradle)
└── database/   # MySQL database initialization script & sample seed data
```

---

## Tech Stack

- **Frontend**: Android Native (Java 11+), Volley REST Client, Material Components, ViewPager2.
- **Backend**: Spring Boot 3.3.5 (Java 17), Spring Data JPA, Spring Security (BCrypt hashing).
- **Database**: MySQL 8.x.

---

## REST API Documentation

The system provides core endpoints at `http://localhost:8080/api`:

| Category | Method | Endpoint | Description |
|---|---|---|---|
| **Authentication** | `POST` | `/api/auth/register` | Register a new account |
| | `POST` | `/api/auth/login` | Log in and retrieve session information |
| **Recipes** | `GET` | `/api/recipes` | Get list of recipes (supports `?search=` and `?userId=`) |
| | `GET` | `/api/recipes/{id}` | Get recipe details, ingredients, cooking steps & nutrition |
| | `POST` | `/api/recipes` | Create a new recipe |
| | `PUT` | `/api/recipes/{id}` | Update recipe information |
| **Favorites** | `GET` | `/api/favorites?userId={id}` | Get user's favorite recipe list |
| | `POST` | `/api/favorites/{recipeId}?userId={id}` | Add a recipe to favorites |
| | `DELETE` | `/api/favorites/{recipeId}?userId={id}` | Remove a recipe from favorites |
| **User** | `GET` | `/api/users/me?userId={id}` | View user profile details |
| | `PUT` | `/api/users/me?userId={id}` | Update account information |

---

## Quick Start Guide

### 1. Database (MySQL)
Run the initialization scripts located in the `Recipe-Nutrition-App-Final/database/` directory in MySQL:
```bash
mysql -u root -p < Recipe-Nutrition-App-Final/database/recipe_nutrition1.sql
mysql -u root -p recipe_nutrition < Recipe-Nutrition-App-Final/database/recipes_seed_vi.sql
```

### 2. Run Backend (Spring Boot)
1. Configure database credentials via environment variables or create `Recipe-Nutrition-App-Final/backend/src/main/resources/application-local.properties`:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=your_mysql_password
   ```
2. Start the server:
   ```bash
   cd Recipe-Nutrition-App-Final/backend
   ./mvnw spring-boot:run
   ```
   *The API server will run at `http://localhost:8080`.*

### 3. Run Frontend (Android)
1. Open the `Recipe-Nutrition-App-Final/frontend` directory in **Android Studio**.
2. Allow Android Studio to sync Gradle dependencies automatically.
3. Run the application on an Android Emulator: Defaults to connecting to `10.0.2.2:8080` (host machine localhost alias).
   *(If running on a physical device: Enter your computer's local network IP in the server address configuration button within the app)*.
