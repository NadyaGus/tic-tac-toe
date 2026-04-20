# Tic-Tac-Toe Game Platform

Spring Boot application for playing tic-tac-toe with computer AI and multiplayer mode.

## 🚀 Features

- **Single Player**: Play against AI with Minimax algorithm
- **Multiplayer**: PVP mode for playing with other users
- **JWT Authentication**: Secure login
- **User Management**: Registration, authentication, profile management
- **Game Management**: Create, join, and track games
- **Leaderboard**: Top players rating based on win rate

## 🛠 Technologies

- **Java 17**
- **Spring Boot 4.0.4**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **Gradle 9.4**
- **JWT Authentication**

## 📦 Project Structure

```
tic-tac-toe/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/tic_tac_toe/
│   │   │       ├── Application.java
│   │   │       ├── datasource/     # Data Layer (entities, repositories)
│   │   │       ├── domain/         # Business Logic (models, services)
│   │   │       ├── di/             # Configuration
│   │   │       └── web/            # Web Layer (controllers, filters)
│   │   └── resources/
│   │       └── application.properties
└── build.gradle.kts
```

## 🗃️ Database Setup

1. Create database:
```sql
CREATE DATABASE "tic-tac-toe";
```

2. Create user (credentials from build.gradle.kts):
```sql
CREATE USER root WITH PASSWORD 'root';
GRANT ALL PRIVILEGES ON DATABASE "tic-tac-toe" TO root;
```

## ▶️ Run Application

```bash
./gradlew bootRun
```

Application will be available at `http://localhost:5432` for Postman (or any HTTP client)

## 🌐 API Endpoints

### Authentication
- `POST /user/register` - User registration
- `POST /user/login` - User login
- `POST /user/refresh/access` - Refresh access token
- `POST /user/refresh/refresh` - Refresh refresh token

### Games
- `POST /game/new?mode=computer` - Create game with AI
- `POST /game/new?mode=human` - Create game with human
- `POST /game/join/{gameId}` - Join existing game
- `POST /game/{uuid}` - Make move
- `GET /game/{uuid}` - Get game state
- `GET /game/available` - Get available games
- `GET /game/finished` - Get finished games

### Users
- `GET /user/{uuid}` - Get user profile
- `GET /user/top` - Get top players leaderboard

## 🔐 Security

- JWT-based authentication
- Refresh token blacklist
- Spring Security protection
- BCrypt password encoding

## 📈 Roadmap

- Unit and integration tests
- Create frontend application
- Implement game replay functionality