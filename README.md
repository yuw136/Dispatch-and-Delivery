# Dispatch and Delivery System

## About This Project

An autonomous delivery management platform that makes package delivery simple and efficient. Users can place delivery orders through an intuitive web interface, and the system automatically dispatches the best robot or drone for the job. Watch your package travel in real-time on an interactive map and receive instant notifications when it's time for pickup or delivery.

Key features include:

- Smart delivery options with cost and time comparisons
- Live package tracking with map visualization
- Instant notifications for pickup and delivery events
- User-friendly order management dashboard
- Admin panel for fleet management

Built with Spring Boot (Java), React, PostgreSQL, Redis, and Google Maps API.

This project is a group project. Developers:Yingchao Cai, Linde Kang, Louis Lee, Yuanzeng Li, Lihua Peng, Congxiao Wang, Liheng Wang, Qilong Wang, Yuanfan Wang, Zian Xiang

## Demo

Watch the demo video: [View Demo](https://drive.google.com/file/d/1TVvA3qf_tvYvdlFhzQ57k_zwj0lrCOsT/view?usp=sharing)

## Setup Instructions

### Prerequisites

- Docker
- Java 21+
- Node.js 18+
- Google Maps API Key

### Quick Start

1. Configure Google Maps API Key in `.env` file at project root:

```
VITE_GOOGLE_MAP_API_KEY="your-api-key"
GOOGLE_MAP_API_KEY="your-api-key"
```

2. Install frontend dependencies:

```bash
cd frontend
npm install
cd ..
```

3. Start all services:

```bash
npm run dev
```

This command will:

- Start PostgreSQL and Redis containers
- Launch backend service on port 8080
- Launch frontend service on port 5173
- Initialize sample data (12 orders and routes)

4. Access the application at http://localhost:5173

### Manual Setup

Start databases:

```bash
docker run --name postgres-dispatch -e POSTGRES_DB=dispatch_delivery -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres:latest
docker run --name redis-dispatch -p 6379:6379 -d redis:latest
```

Start backend:

```bash
cd backend
./gradlew bootRun
```

Start frontend:

```bash
cd frontend
npm run dev
```

## Project Structure

```
backend/
  src/main/java/com/flagcamp/dispatchanddelivery/
    - client/           # External API clients (Google Maps)
    - config/           # Configuration classes (Security, Redis, WebSocket)
    - controller/       # REST API controllers
    - entity/           # JPA entities
    - listener/         # Event listeners for async communication
    - manager/          # Business logic managers
    - model/            # DTOs, requests, responses, enums
    - repository/       # JPA repositories
    - security/         # Security implementations
    - service/          # Business logic services
    - socket/           # WebSocket handlers
  src/main/resources/
    - application.yml   # Spring Boot configuration
    - schema.sql        # Database schema
    - data.sql          # Sample data

frontend/
  src/
    - components/       # React components
    - api/              # API client functions
    - pages/            # Page components
    - contexts/         # React contexts
    - hooks/            # Custom React hooks
    - utils/            # Utility functions
```
