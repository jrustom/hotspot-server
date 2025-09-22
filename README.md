# Hotspot Backend

Java Spring Boot backend for the Hotspot location-based messaging app.

## Tech Stack

- Java 21
- Spring Boot
- Spring WebSocket (STOMP over SockJS)
- MongoDB Atlas
- Maven

## Setup

1. **Requirements**: Java 21, Maven, MongoDB Atlas account

2. **Environment Variables** (create `.env` file):
   ```env
   MONGO_USER=your_mongodb_username
   MONGO_PW=your_mongodb_password
   MONGO_URI=your_mongodb_atlas_connection_string
   WS_STOMPJS_URL=ws://localhost:8080/ws
   WS_BROKER_PREFIX=/topic
   WS_BROKER_URL=/ws
   WS_SERV_PREFIX=/app
   ```

3. **Run locally**:
   ```bash
   mvn spring-boot:run
   ```

Backend runs on `http://localhost:8080`

## Features

- REST API for hotspots and messages
- WebSocket messaging via STOMP over SockJS
- MongoDB Atlas integration for data persistence
- Real-time communication for chats
