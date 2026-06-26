# E-Commerce Application

A full-stack e-commerce application with a Java Spring Boot backend and a vanilla JavaScript frontend. This capstone project demonstrates a complete e-commerce workflow including product browsing, shopping cart management, user authentication, and order processing.

## Project Overview

This application provides a complete e-commerce platform with the following features:

- **User Authentication & Authorization**: Secure login and registration system
- **Product Catalog**: Browse products by categories with filtering capabilities
- **Shopping Cart**: Add, remove, and manage items in a shopping cart
- **Order Management**: Place and track orders
- **User Profiles**: Manage user account information
- **Responsive UI**: Bootstrap-based frontend for multiple devices

## Technologies Used

### Backend
- **Java 11+**
- **Spring Boot**: REST API framework
- **Spring Data JPA**: Database access layer
- **Spring Security**: Authentication and authorization
- **Maven**: Build automation
- **MySQL/MariaDB**: Database

### Frontend
- **HTML5**: Markup structure
- **CSS3 with Bootstrap**: Responsive styling
- **JavaScript (ES6+)**: Client-side logic
- **Axios**: HTTP client for API calls
- **Mustache.js**: Template rendering
- **jQuery**: DOM manipulation

## Project Structure

```
capstone-3-main/
├── backend/                          # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/org/yearup/
│   │   │   │   ├── controllers/      # REST API endpoints
│   │   │   │   ├── models/           # Entity classes
│   │   │   │   ├── repository/       # Data access layer
│   │   │   │   ├── service/          # Business logic
│   │   │   │   ├── security/         # Security configuration
│   │   │   │   └── ECommerceApplication.java
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── database scripts
│   │   └── test/                     # Unit and integration tests
│   ├── database/                     # Database schemas
│   ├── pom.xml                       # Maven configuration
│   └── mvnw                          # Maven wrapper
├── frontend/                         # Frontend Application
│   ├── js/
│   │   ├── services/                 # API service modules
│   │   ├── application.js            # Main application logic
│   │   └── lib/                      # Third-party libraries
│   ├── css/                          # Stylesheets
│   ├── templates/                    # HTML templates
│   ├── images/                       # Product images and data
│   └── index.html                    # Main HTML file
├── capstone-insomnia-collection.yaml # API testing collection
└── README.md                         # This file
```

## Prerequisites

- **Java 11 or higher**
- **Maven 3.6+** (or use the included Maven wrapper)
- **MySQL 5.7+ or MariaDB 10.3+**
- **Node.js** (optional, for running a local development server)
- **Modern web browser** (Chrome, Firefox, Safari, Edge)

## Installation & Setup

### 1. Database Setup

1. Start your MySQL/MariaDB server
2. Choose a database schema from the `backend/database/` directory based on your store type:
    - `create_database_clothingstore.sql`
    - `create_database_easyshop.sql`
    - `create_database_grocerystore.sql`
    - `create_database_recordshop.sql`
    - `create_database_videogamestore.sql`

3. Run the schema to create the database:
   ```bash
   mysql -u root -p < backend/database/create_database_easyshop.sql
   ```

### 2. Backend Configuration

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Update `src/main/resources/application.properties` with your database configuration:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/easyshop_db
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   ```

3. Build the project:
   ```bash
   ./mvnw clean install
   ```
   (or `mvnw.cmd clean install` on Windows)

### 3. Frontend Setup

The frontend is a static HTML/CSS/JavaScript application. No additional setup is required beyond having the files in the `frontend/` directory.

## Running the Application

### Starting the Backend

1. From the `backend/` directory, start the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

2. The backend API will be available at: `http://localhost:8080`

3. API documentation is available at: `http://localhost:8080/swagger-ui.html`

### Starting the Frontend

**Option 1: Using a Simple HTTP Server (Recommended)**

From the `frontend/` directory:

```bash
# Using Python 3
python3 -m http.server 3000

# Using Python 2
python -m SimpleHTTPServer 3000

# Using Node.js http-server (if installed)
npx http-server -p 3000
```

Then navigate to: `http://localhost:3000`

**Option 2: Direct File Access**

Open `frontend/index.html` directly in your browser:
```bash
open frontend/index.html
```
(Note: Some features may not work due to CORS restrictions)

## API Endpoints

The backend provides the following REST API endpoints:

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Products
- `GET /api/products` - Get all products
- `GET /api/products/:id` - Get product by ID
- `GET /api/products/category/:categoryId` - Get products by category

### Categories
- `GET /api/categories` - Get all categories

### Shopping Cart
- `GET /api/cart` - Get current user's shopping cart
- `POST /api/cart/items` - Add item to cart
- `DELETE /api/cart/items/:itemId` - Remove item from cart

### Orders
- `POST /api/orders` - Create a new order
- `GET /api/orders` - Get user's orders
- `GET /api/orders/:id` - Get order details

### Profile
- `GET /api/profile` - Get user profile
- `PUT /api/profile` - Update user profile

## API Testing

An Insomnia collection is provided for testing the API:

1. Open Insomnia API Client
2. Import `capstone-insomnia-collection.yaml`
3. Configure environment variables if needed
4. Test the endpoints


## Features

### User Management
- Register a new account
- Secure login with JWT authentication
- Manage profile information
- Password management

### Product Browsing
- Browse all products with pagination
- Filter by category
- Search functionality
- Detailed product information
- Product images

### Shopping Cart
- Add/remove items from cart
- Update item quantities
- View cart total
- Persistent cart management

### Orders
- Place orders from cart items
- View order history
- Track order status
- Order details including line items


