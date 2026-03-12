# User Behaviors Tracking Service

A Spring Boot application for tracking and publishing E-commerce user behaviors using Apache Kafka.

## Overview

This service captures user events (page views, clicks, purchases, etc.) via REST API and publishes them to Kafka topics for downstream processing.

## Tech Stack

- **Framework:** Spring Boot 4.0.3
- **Language:** Java 17
- **Message Broker:** Apache Kafka
- **Serialization:** Jackson (JSON)

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/publish` | Publish user event to Kafka |

### Request Body

```json
{
  "userId": "user123",
  "eventType": "page_view",
  "payload": {
    "page": "/home",
    "timestamp": "2026-03-12T10:30:00Z"
  }
}
```

### Response

```json
{
  "success": true,
  "message": "Event published successfully",
  "userId": "user123",
  "eventType": "page_view",
  "eventId": null,
  "timestamp": "2026-03-12T10:30:00",
  "payload": { ... }
}
```

## Building

```bash
./mvnw clean package
```

## Running

```bash
./mvnw spring-boot:run
```

The application runs on `http://localhost:8080` by default.

## Configuration

Kafka and application settings are in `src/main/resources/application.yaml`.