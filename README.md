# E-Commerce App

## Overview

This project is built using an **Event-Driven Architecture (EDA)** pattern combined with a **RESTful API** layer to support e-commerce applications, especially to handle **create order request**.

It demonstrates how multiple backend services communicate **asynchronously via Apache Kafka topics** to handle complex workflows such as order creation, stock validation, and payment authorization.

The backend system provides **RESTful APIs** that serve as the main entry point for users interacting through the e-commerce frontend.
These APIs are responsible for handling client requests — such as placing an order, viewing products, or checking order status — and then triggering the corresponding event flows within the backend.

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

1. `Order Service` creates a new Order in the `CREATED` state and publishes an `order-created` event.  
2. `Order Line Item Service` consumes the event and saves item details to the `Order Line Item Table`.  
3. `Product Service` verifies stock availability, reduces inventory, and publishes a `stock-reserved` event.  
4. `Payment Service` creates a `PENDING` payment record.  
5. `Payment Service` consumes `stock-reserved`, authorizes the payment, and publishes `payment-authorized`.  
6. `Order Service` consumes `payment-authorized`, updates order to `APPROVED`, and publishes `order-approved`.


### **Insufficient Stock**
The failed order creation flow caused by insufficient product stock proceeds as follows:

1. `Order Service` creates a new Order in the `CREATED` state and publishes an `order-created` event.  
2. `Order Line Item Service` records all items in the `Order Line Item Table`.  
3. `Product Service` detects insufficient stock and publishes `stock-reservation-failed`.  
4. `Payment Service` creates a `PENDING` payment but cancels it upon receiving `stock-reservation-failed`.  
5. `Order Service` consumes `stock-reservation-failed`, marks order as `REJECTED`, and publishes `order-rejected`.


### **Payment Failed**
The failed order creation flow caused by a payment authorization failure proceeds as follows:

1. `Order Service` creates a new Order in the `CREATED` state and publishes an `order-created` event.  
2. `Order Line Item Service` records item details.  
3. `Product Service` reserves the required stock and publishes `stock-reserved`.  
4. `Payment Service` attempts to authorize the payment but fails, publishing `payment-failed`.  
5. `Product Service` releases previously reserved stock.  
6. `Order Service` updates the Order state to `REJECTED` and publishes `order-rejected`.


The detailed event flow descriptions are available [here](https://github.com/fardanaljihad/ecommerce-app/blob/main/backend/README.md).
