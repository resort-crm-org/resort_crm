# Resort CRM

A Spring Boot backend application for managing Resort Customer Relationship Management operations.

## Project Summary

Resort CRM is a full-stack application designed to manage resort operations efficiently. The system consists of two main components working in tandem:

### Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                      Frontend Application                   │
│                  (HTML/CSS/JavaScript/React)                │
│                                                              │
│  ├── Guest Management Dashboard                            │
│  ├── Room Inventory Management                             │
│  └── Room Allotment & Booking Interface                    │
│                                                              │
│           ↕ RESTful API Communication (JSON)               │
│                                                              │
└─────────────────────────────────────────────────────────────┘
           ↓                                      ↓
┌──────────────────────────────────────────────────────────────┐
│                    Backend (Spring Boot)                     │
│                     Resort CRM Service                       │
│                                                              │
│  ├── REST Controllers (Endpoints)                           │
│  ├── Business Logic Services                                │
│  ├── Database Layer (JPA/Hibernate)                         │
│  └── Exception Handling & Validation                        │
│                                                              │
└──────────────────────────────────────────────────────────────┘
           ↓
┌──────────────────────────────────────────────────────────────┐
│                    H2 Database                               │
│                  (In-Memory Database)                        │
└──────────────────────────────────────────────────────────────┘
```

### Frontend & Backend Communication

**Communication Protocol**: RESTful API over HTTP/HTTPS

**Data Format**: JSON (JavaScript Object Notation)

**Base URL**: `http://localhost:8080` (development environment)

#### How They Communicate:

1. **Frontend Initiation**: The frontend application sends HTTP requests to the backend REST API endpoints
2. **Request Processing**: The backend Spring Boot application receives the request, validates input, and processes business logic
3. **Database Operations**: The backend performs necessary CRUD operations on the H2 database
4. **Response**: The backend returns a JSON response with status codes and data
5. **Frontend Rendering**: The frontend receives the response and updates the UI accordingly

#### Example Flow:

```
Frontend Request:
┌─────────────────────────────────────────────────────────────┐
│  POST /api/guests                                            │
│  Content-Type: application/json                              │
│                                                              │
│  {                                                           │
│    "name": "John Doe",                                      │
│    "email": "john@example.com",                             │
│    "phone": "1234567890"                                    │
│  }                                                           │
└─────────────────────────────────────────────────────────────┘
           ↓
Backend Processing:
┌─────────────────────────────────────────────────────────────┐
│  1. GuestController receives request                        │
│  2. GuestService validates and processes data               │
│  3. JPA saves Guest entity to H2 database                   │
│  4. Returns Guest object with assigned ID                   │
└─────────────────────────────────────────────────────────────┘
           ↓
Frontend Response:
┌─────────────────────────────────────────────────────────────┐
│  HTTP 201 Created                                            │
│  Content-Type: application/json                              │
│                                                              │
│  {                                                           │
│    "id": 1,                                                 │
│    "name": "John Doe",                                      │
│    "email": "john@example.com",                             │
│    "phone": "1234567890"                                    │
│  }                                                           │
└─────────────────────────────────────────────────────────────┘
```

### Key Components

**Frontend Responsibilities**:
- User interface and user experience
- Form validation and data collection
- Request/response handling
- State management
- Data display and visualization

**Backend Responsibilities**:
- Business logic implementation
- Data validation and processing
- Database management
- Authentication/Authorization (future)
- Error handling and logging
- API request routing

## Overview

This project provides a RESTful API for managing guest information, room management, and room allotments in a resort. The application is built with Spring Boot 3.3.4 and uses H2 in-memory database for data persistence.

## Technology Stack

- **Java**: 17
- **Spring Boot**: 3.3.4
- **Spring Data JPA**: For database operations
- **H2 Database**: In-memory relational database
- **Maven**: Build and dependency management
- **Docker**: Containerization support

## Project Structure

```
src/main/java/com/resort/crm/
├── ResortCrmApplication.java          # Main application entry point
├── controller/
│   ├── GuestController.java           # Guest management endpoints
│   ├── RoomController.java            # Room management endpoints
│   └── RoomAllotmentController.java   # Room allotment endpoints
├── service/
│   ├── GuestService.java              # Guest business logic
│   ├── RoomService.java               # Room business logic
│   └── RoomAllotmentService.java      # Room allotment business logic
├── repository/
│   ├── GuestRepository.java           # Guest data access
│   └── RoomRepository.java            # Room data access
├── model/
│   ├── Guest.java                     # Guest entity
│   ├── Room.java                      # Room entity
│   └── RoomStatus.java                # Room status enumeration
└── exception/
    ├── BadRequestException.java       # Bad request exception
    ├── ResourceNotFoundException.java  # Resource not found exception
    └── RestExceptionHandler.java      # Global exception handler
```

## Features

- **Guest Management**: Create, read, update, and delete guest records
- **Room Management**: Manage room information and availability
- **Room Allotment**: Handle room allocation and booking operations
- **Exception Handling**: Centralized error handling with custom exceptions
- **RESTful API**: Clean and intuitive API endpoints
- **H2 Console**: Web-based console for database inspection

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Git (optional, for cloning the repository)

## Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd resort_crm
```

### 2. Build the Project
```bash
./mvnw clean package
```

On Windows:
```bash
mvnw.cmd clean package
```

### 3. Run the Application
```bash
./mvnw spring-boot:run
```

On Windows:
```bash
mvnw.cmd spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Run the Application (Quick Reference)
```bash
./mvnw spring-boot:run
```

On Windows:
```bash
mvnw.cmd spring-boot:run
```

Application runs at: `http://localhost:8080`

## Configuration

Configuration can be modified in `src/main/resources/application.properties`:

```properties
spring.application.name=resort_crm
server.port=8080                          # Server port

# Database Configuration
spring.datasource.url=...                 # Database URL
spring.datasource.username=sa             # Database username
spring.datasource.password=               # Database password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update      # Auto schema creation/update
spring.jpa.show-sql=true                  # Log SQL statements

# H2 Console
spring.h2.console.enabled=true            # Enable H2 console
spring.h2.console.path=/h2-console        # H2 console path
```

## Database

### H2 Database Console
Access the H2 database console at: `http://localhost:8080/h2-console`

**JDBC URL**: `jdbc:h2:mem:resortcrm;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`

**Username**: `sa`

**Password**: (leave empty)

## API Endpoints

### Guest Management
- `GET /api/guests` - Get all guests
- `GET /api/guests/{id}` - Get guest by ID
- `POST /api/guests` - Create new guest
- `PUT /api/guests/{id}` - Update guest
- `DELETE /api/guests/{id}` - Delete guest

### Room Management
- `GET /api/rooms` - Get all rooms
- `GET /api/rooms/{id}` - Get room by ID
- `POST /api/rooms` - Create new room
- `PUT /api/rooms/{id}` - Update room
- `DELETE /api/rooms/{id}` - Delete room

### Room Allotment
- `POST /api/allotments` - Allocate room to guest
- `GET /api/allotments` - Get all allotments
- `PUT /api/allotments/{id}` - Update allotment
- `DELETE /api/allotments/{id}` - Delete allotment


## Build

```bash
./mvnw clean package
```

On Windows:
```bash
mvnw.cmd clean package
```

### Backend Build Success

![Backend Build](Proof/Backend%20Build.png)

### Frontend Build Success

![Frontend Build](Proof/Frontend%20Build.png)


## Docker

Build and run using Docker:

```bash
docker build -t resort-crm .
docker run -p 8080:8080 resort-crm
```

### Docker Hub Repository

![Docker Hub](Proof/Docker%20Hub.png)

### Docker Playground Deployment

![Docker Playground](Proof/Docker%20Playground.png)

## Challenges Faced and Solutions

During the development and DevOps integration of the Resort CRM backend, several challenges were encountered. These issues were resolved through systematic debugging and configuration adjustments.

### 1. Maven Command Not Found

**Issue**:  
While building the project in certain environments (such as Docker Playground), the `mvn` command was not available.

**Cause**:  
Maven was not installed in the execution environment.

**Solution**:  
The Maven Wrapper (`mvnw`) provided by Spring Boot was used to build the project:
```bash
./mvnw clean package
```

On Windows, use:
```bash
mvnw.cmd clean package
```

The Maven Wrapper eliminates the need for a separate Maven installation and ensures consistent builds across all environments.

### 2. Port Already in Use

**Issue**:  
Application startup failed with "Address already in use" error when attempting to start the application on port 8080.

**Cause**:  
Another application or a previous instance of the Resort CRM application was already running on port 8080.

**Solution**:  
Either kill the process using the port or configure a different port in `application.properties`:
```properties
server.port=8081
```

To identify and terminate the process using the port:
```bash
# On Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# On Linux/Mac
lsof -i :8080
kill -9 <PID>
```

### 3. H2 Database Connection Issues

**Issue**:  
Application failed to connect to the H2 database with connection refused errors.

**Cause**:  
Incorrect or missing database URL configuration in `application.properties`.

**Solution**:  
Verify and update the H2 database configuration:
```properties
spring.datasource.url=jdbc:h2:mem:resortcrm;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
```

The in-memory database URL `jdbc:h2:mem:resortcrm` ensures the database persists during the application session.

### 4. Docker Build Failures

**Issue**:  
Docker build failed with Java compilation errors or missing dependencies.

**Cause**:  
Incorrect Dockerfile configuration or missing dependencies in the build process.

**Solution**:  
Ensure the Dockerfile properly stages the Maven build and uses the correct Java base image:
```dockerfile
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package

FROM openjdk:17-jdk-slim
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 5. CORS Issues in Frontend Integration

**Issue**:  
Frontend applications received CORS (Cross-Origin Resource Sharing) errors when attempting to communicate with the backend API.

**Cause**:  
Missing CORS configuration in the Spring Boot application.

**Solution**:  
Configure CORS in the main application or create a WebConfig class:
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}
```

## Optional Deployment (Experimental)

> **Note**: Backend deployment to Render was explored as an optional enhancement and is not mandatory for core project evaluation.

### Production Deployment on Render

The Resort CRM backend is deployed and hosted on **Render.com**, a modern cloud platform for building and running applications.

**Deployment URL**: [https://resortcrm-by-dk.onrender.com](https://resortcrm-by-dk.onrender.com) (Production Endpoint)

#### Accessing the Production Backend

- **Base URL**: `https://resortcrm-by-dk.onrender.com`
- **Database**: Uses Render's managed database service or cloud-based alternatives
- **Auto-Deploy**: The application auto-deploys on commits to the main branch
- **Environment**: Production-grade environment with automatic scaling

#### Render Configuration

- **Service Type**: Web Service
- **Runtime**: Java 17
- **Build Command**: `./mvnw clean package`
- **Start Command**: `java -jar target/resort_crm-0.0.1-SNAPSHOT.jar`
- **Region**: Auto-selected for optimal performance

#### Frontend Integration with Production Backend

Update your frontend application's API base URL to point to the deployed backend:

```javascript
// Frontend Configuration
const API_BASE_URL = 'https://resortcrm-by-dk.onrender.com';

// Example API call
fetch(`${API_BASE_URL}/api/guests`)
  .then(response => response.json())
  .then(data => console.log(data))
  .catch(error => console.error('Error:', error));
```

#### Environment Variables

Production environment variables are configured in Render's dashboard:
- `server.port`: Automatically managed by Render
- `spring.datasource.url`: Production database URL
- `spring.jpa.hibernate.ddl-auto`: Set to `validate` or `update` for production

#### Monitoring & Logs

- Access logs through Render's dashboard
- Monitor application performance and resource usage
- Set up alerts for downtime or errors

### Backend Deployment

![Backend Deployment](Proof/Backend%20Deployment.png)

### Frontend Deployment

![Frontend Deployed](Proof/Frontend%20deployed.png)

### Custom Domain Configuration

![GitHub Custom Domain](Proof/Github%20Custom%20Domain.png)

## Known Limitations

- Uses H2 in-memory database; data resets on application restart
- No authentication or authorization implemented
- Designed for single-resort usage
- Not optimized for large-scale concurrent traffic
- Room allotment system uses basic assignment logic

## Testing

Run tests with:

```bash
./mvnw test
```

On Windows:
```bash
mvnw.cmd test
```

## Error Handling

The application includes centralized exception handling for:
- **BadRequestException**: Invalid request parameters
- **ResourceNotFoundException**: Requested resource not found
- **General HTTP Exceptions**: Mapped through `RestExceptionHandler`

## Code Quality

This project integrates SonarCloud for static code analysis and code quality monitoring:
- **Organization**: 23suca03-dineshkarthick
- **Project Key**: 23suca03-dineshkarthick_resort_crm
- **SonarCloud URL**: https://sonarcloud.io

### SonarCloud Analysis Results

#### Backend Analysis

![Sonar Backend](Proof/Sonar%20Backend.png)

#### Frontend Analysis

![Sonar Frontend](Proof/Sonar%20Frontend.png)

## Development

### Hot Reload
The project includes Spring Boot DevTools for automatic restart during development. Changes to Java files will trigger automatic recompilation.

### IDE Setup
Import the project as a Maven project in your IDE (IntelliJ IDEA, Eclipse, VS Code with Java extensions, etc.).

## License

See LICENSE file for details.

## Support

For issues or questions, please refer to the project repository or contact the development team.

---

**Last Updated**: January 31, 2026
