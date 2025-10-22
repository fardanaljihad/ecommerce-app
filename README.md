# E-Commerce App

## Overview

This e-commerce application is built using an **Event-Driven Architecture (EDA)** combined with a **RESTful API** to handle a key process, which is **order creation**. With this architecture, multiple backend services communicate **asynchronously through Apache Kafka**. The **RESTful APIs** serves as the entry point for the e-commerce frontend, handling requests such as placing orders, browsing products, and viewing order history.

---

## Tech Stack
- **Backend:** Spring Boot (Spring Security with JWT, Spring Data JPA + Hibernate, Spring Web/MVC, Lombok, Validation, and Exception Handler)
- **Frontend:** React
- **Database:** PostgreSQL 
- **Message Broker:** Apache Kafka

---

## Event Flows

### **Successful Order Creation**
The happy path for order creation proceeds as follows:
<img width="745" height="571" alt="E-Commerce Application Happy Path" src="https://github.com/user-attachments/assets/93a70329-8c77-4765-a59e-473d1be3b3c4" />

Other event flows and their details are available [here](https://github.com/fardanaljihad/ecommerce-app/blob/main/backend/README.md).
