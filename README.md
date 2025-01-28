# Each Day Counts - Backend Service

The backend service for Each Day Counts, a platform that helps users track and manage their daily activities. This Spring Boot application handles user authentication, authorization, and data management for the Each Day Counts platform.

## 🎯 Overview

This backend service provides the core API functionality for Each Day Counts, including:
- User authentication and authorization
- Secure session management
- RESTful API endpoints for the frontend application
- Data persistence and management

## 🚀 Features

- **Authentication & Authorization**
  - Secure user registration and login
  - JWT-based authentication with HTTP-only cookies
  - Role-based access control
  - Password encryption

- **Security**
  - HTTPS-only communication
  - Protection against XSS attacks
  - Secure session management
  - Token-based authentication

## 🛠️ Technologies

- Java 17
- Spring Boot
- Spring Security
- JSON Web Tokens (JWT)
- Maven
- Jakarta EE

## 📋 Prerequisites

- Java JDK 17 or higher
- Maven 3.6+ 
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)

## 🔧 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/brainrackai/each-day-counts-be.git
   ```

2. Navigate to the project directory:
   ```bash
   cd each-day-counts-be
   ```

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The backend service will start running at `http://localhost:8080`

## 🔌 API Endpoints

### Authentication

#### Register New User
```bash
http
POST /auth/register
Content-Type: application/json
{
"email": "user@example.com",
"password": "yourpassword"
}
```


#### User Login
```bash
http
POST /auth/login
Content-Type: application/json
{
"email": "user@example.com",
"password": "yourpassword"
}
```


#### Get All Users (Admin Only)
```bash
http
GET /auth/users
Authorization: Bearer {jwt-token}
```


## 🔒 Security Implementation

- JWT tokens are stored in HTTP-only cookies
- Secure flag enabled for HTTPS-only transmission
- Cross-Site Scripting (XSS) protection
- 24-hour token expiration
- Stateless authentication architecture
- Password encryption using BCrypt

## 🔧 Configuration

The application can be configured through `application.properties` or environment variables:

- `SERVER_PORT`: Server port (default: 8080)
- `JWT_SECRET`: Secret key for JWT token generation
- `JWT_EXPIRATION`: Token expiration time in milliseconds

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🐛 Bug Reporting

If you find a bug, please open an issue with:
- Clear bug description
- Steps to reproduce
- Expected behavior
- Screenshots (if applicable)

## 📞 Support

For support:
- Open an issue on GitHub
- Contact the development team at dev@eachdaycounts.com

## ✨ Acknowledgments

- Spring Boot team for the excellent framework
- All contributors who have helped this project grow

---
Made with ❤️ by the Each Day Counts Team
