# 🎵 Record Shop E-Commerce API
A Java Spring Boot REST API for an e-commerce record shop application. The backend handles user authentication, category and product management, shopping cart operations, and order checkout.

---
## ✨ Project Features
This application provides a complete e-commerce platform with the following features:

- **User Authentication & Authorization** — Secure registration/login with JWT, role-based access control (`USER` / `ADMIN`)
- **Product Catalog** — Browse and search products by category, price range, and subcategory
- **Shopping Cart** — Add, update quantity, and clear cart items
- **Order Management** — Checkout converts the cart into an order, then clears the cart
- **Admin Tools** — Create, update, and delete categories and products (admin only)
- **User Profiles** — View and update account information
- **Responsive UI** — Frontend styled for multiple devices

---
## 🛠️ Tech Stack
### Backend
- **Java 17+**
- **Spring Boot**: REST API framework
- **Spring Data JPA**: Database access layer
- **Spring Security**: Authentication and authorization
- **Maven**: Build automation
- **MySQL**: Database

### Frontend
- **HTML5**: Markup structure
- **CSS3 with Bootstrap**: Responsive styling
- **JavaScript**: Client-side logic

---
## 📁 Project Structure
```
org.yearup
├── controllers/   REST endpoints
├── models/        JPA entities & DTOs
├── repository/    Spring Data JPA repositories
├── service/       Business logic
└── security/jwt/  JWT provider & filter
```

---
## 🌐 API Endpoints
The backend provides the following REST API endpoints:
### Products
- `GET /products` - Search/list products
- `GET /products/{id}` - Get product by ID
- `POST /products` - Add new product (ADMIN)
- `PUT /products/{id}` - Update product (ADMIN)
- `DELETE /products/{id}` - Delete product (ADMIN)
### Categories
- `GET /categories` - Get all categories
- `GET /categories/{id}` - Get category by ID
- `GET /categories/{id}/products` - Get products in a category
- `POST /categories` - Add new category (ADMIN)
- `PUT /categories/{id}` - Update category (ADMIN)
- `DELETE /categories/{id}` - Delete category (ADMIN)
### Shopping Cart
- `GET /cart` - Get current user's shopping cart
- `POST /cart/products/{id}` - Add item to cart
- `PUT /cart/products/{id}` - Update product quantity
- `DELETE /cart` - Clear cart
### Orders
- `POST /orders` - Checkout and create a new order
### Profile
- `GET /profile` - Get user profile
- `PUT /profile` - Update user profile

---
## 💡 Code Highlight
One of my favorite pieces of code is the **checkout logic** in `OrderService.java`. It needed to pull the user's saved shipping info, convert every cart item into a permanent 
order record, and then empty the cart.

```java
@Transactional
public Order checkout(int userId) {
    Profile profile = profileRepository.findById(userId).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    List<CartItem> cartItems = shoppingCartRepository.findByUserId(userId);
    if (cartItems.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    Order order = new Order();
    order.setUserId(profile.getUserId());
    order.setDate(LocalDateTime.now());
    order.setAddress(profile.getAddress());
    order.setCity(profile.getCity());
    order.setState(profile.getState());
    order.setZip(profile.getZip());
    order.setShippingAmount(0);
    orderRepository.save(order);

    for (CartItem cartItem : cartItems) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(order.getOrderId());
        orderLineItem.setPrice((productRepository.findById(cartItem.getProductId()).orElseThrow()).getPrice());
        orderLineItem.setProductId(cartItem.getProductId());
        orderLineItem.setQuantity(cartItem.getQuantity());
        orderLineItem.setDiscount(0);
        orderLineItemRepository.save(orderLineItem);
    }
    shoppingCartRepository.deleteByUserId(userId);
    return order;
}
```

The `@Transactional` annotation wraps the whole method in a single database transaction — if anything fails midway
(like a product getting deleted between the cart add and checkout), every change rolls back instead of leaving a half-created order. 
The method pulls the user's saved address from their `Profile`, snapshots the current price of each cart item into a permanent `OrderLineItem`, then clears the cart only after everything else succeeds.

---
## 🚧 Challenges I Faced
One of the biggest challenges in this project was implementing the checkout process. It required connecting multiple parts of the application, including authentication, user profiles, the shopping cart, order creation, and the database. Learning how an Order connects to multiple OrderLineItems and how data moves through the application helped deepen my understanding of Spring Boot, JPA, and backend development.

---
## ✅ API Testing
An Insomnia collection is provided for testing the API:
1. Open Insomnia API Client
2. Import `capstone-insomnia-collection.yaml`
3. Configure environment variables if needed
4. Test the endpoints

---
## 👤 Author
**Tim Startsev**, Year Up United — Spring 2026
