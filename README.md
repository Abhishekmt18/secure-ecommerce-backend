# 🔐 Secure E-Commerce Application with AI Fraud Detection

A secure, backend-focused e-commerce application that integrates **AI-based fraud detection** and **biometric verification workflows** to prevent high-risk transactions.
Built as a **major academic project** with real-world architecture and microservice-style integration.

---

## 📌 Project Overview

This project simulates a real-world **secure e-commerce system** where transactions are dynamically evaluated using an **AI fraud detection service**.
Based on the fraud risk level (LOW / MEDIUM / HIGH), the system decides whether to:

* Allow the transaction directly
* Require biometric verification
* Block the transaction completely

The backend is built using **Spring Boot**, while fraud analysis is handled by a **separate Python service**, communicating via REST APIs.

---

## ✨ Key Features

* User registration and login
* Product listing and cart management
* Checkout and order creation
* AI-based fraud risk analysis
* Risk-based transaction handling:

  * **LOW risk** → Direct success
  * **MEDIUM risk** → Biometric verification required
  * **HIGH risk** → Transaction blocked
* Transaction history with fraud/success status
* Clean REST API design
* Database-backed persistence using MySQL

---

## 🛠️ Tech Stack

### Backend

* Java
* Spring Boot
* Spring Data JPA / Hibernate
* RESTful APIs

### Database

* MySQL

### AI / Fraud Detection

* Python
* Flask (REST service)

### Frontend (Demo)

* HTML
* CSS
* JavaScript

### Tools

* Postman
* Git
* GitHub

---

## 🏗️ Architecture Overview

```
[ Browser / Postman ]
          |
          v
[ Spring Boot Backend ]
          |
          | REST API (JSON)
          v
[ Python Fraud Detection Service ]
```

### How it works:

1. User checks out items from cart
2. Backend calculates total transaction amount
3. Backend sends amount to Python AI service
4. Python service returns risk level
5. Backend applies business rules based on risk
6. Transaction result is saved in database

---

## 🔌 API Endpoints (Main)

### User APIs

```
POST /api/users/login
```

### Product APIs

```
GET /api/products
```

### Cart APIs

```
POST /api/cart/add?userId={id}&productId={id}&quantity={n}
GET  /api/cart/{userId}
```

### Order & Transaction APIs

```
POST /api/orders/checkout/{userId}
GET  /api/transactions/{userId}
```

### Python Fraud Service

```
POST http://localhost:5000/fraud/check
```

---

## ⚙️ Fraud Decision Logic

| Amount Range   | Risk Level | Action Taken           |
| -------------- | ---------- | ---------------------- |
| ≤ 50,000       | LOW        | Order placed directly  |
| 50,001–100,000 | MEDIUM     | Biometric verification |
| > 100,000      | HIGH       | Transaction blocked    |

---

## ▶️ How to Run the Application

### 1️⃣ Start MySQL

* Create database: `secure_ecommerce_db`
* Update credentials in `application.properties`

### 2️⃣ Run Spring Boot Backend

```bash
mvn spring-boot:run
```

Backend runs on:

```
http://localhost:8080
```

### 3️⃣ Run Python Fraud Detection Service

```bash
python app.py
```

Python service runs on:

```
http://localhost:5000
```

### 4️⃣ Test Using Browser / Postman

* Login user
* Add products to cart
* Checkout and observe fraud handling
* View transaction history

---

## 🔄 Backend ↔ Python Communication

* Spring Boot uses `RestTemplate`
* Sends transaction amount as JSON
* Python service returns fraud risk classification
* Backend enforces business rules accordingly

This design mimics **real microservice communication** used in production systems.

---

## 📈 Why This Project Matters

* Demonstrates **real-world backend design**
* Shows **AI service integration**
* Implements **risk-based decision making**
* Uses clean architecture and REST principles
* Suitable for **Java Backend / Full Stack Fresher roles**

---
