# Multi-Threaded Flight Seat Booking System

This project is a Spring Boot REST API designed to demonstrate concurrent resource management and race condition mitigation in a multi-user environment.

* **The Simulation:** A 9x3 airplane seat allocation matrix (27 seats total).
* **The Core Tech:** Built using Spring Data JPA, an in-memory H2 database, and Hibernate Optimistic Locking.

---

## Overview

The primary objective of this application is to safely handle multiple simulation threads or users attempting to reserve the exact same seat simultaneously.

* **Data Integrity:** Prevents double-bookings and transactional conflicts under heavy concurrent load.
* **Performance Optimization:** Relies on a version-based concurrency control model (`@Version`) instead of using resource-heavy database table locks.

---

## Project Architecture

As illustrated in the project directory layout in `image_afe331.png`, the application is structured into standard decoupled layers:

```plaintext
AirplaneBookingSystem [booking]
 └── src
      └── main
           ├── java
           │    └── com
           │         └── flysafe
           │              ├── controller
           │              │    └── SeatController.java
           │              ├── prototype
           |              ├── BookingManager.java
           │              ├── BookingRequest.java
           │              ├── FlightBookingApplication.java
           │              ├── Seat.java
           │              ├── SeatRepository.java
           │              └── SeatService.java
           └── resources
```
---
## Key Features

| Feature | Component / Endpoint | Technical Implementation |
| :--- | :--- | :--- |
| **Automated Seeding** | `FlightBookingApplication` | Implements `CommandLineRunner` to auto-populate the database with 27 empty seat entities at launch. |
| **Manifest Retrieval** | `GET /api/seats/all` | Streams database records to return a JSON array containing the status and assignments of all seats. |
| **Seat Booking** | `POST /api/seats/book` | Evaluates coordinate availability and links a passenger name to a specific row and column. |
| **Passenger Update** | `PUT /api/seats/update/{id}` | Modifies a passenger name on an active reservation via a decoupled `BookingRequest` DTO. |
| **Reservation Purge** | `DELETE /api/seats/cancel/{id}` | Resets the seat state by removing the assigned name and flipping the booking flag back to false. |
| **Collision Resolution** | `@Version` Annotation | Intercepts concurrent write requests using Hibernate Optimistic Locking. |

---

## Concurrency Mechanics

When two or more threads attempt to book the exact same coordinate at the same time, a race condition occurs. This application handles data collision via **Optimistic Locking**:

* The `Seat` entity contains a version tracking column managed via the `@Version` annotation.
* When concurrent transactions read the same seat state, they retrieve identical version integers.
* The transaction that commits first updates the row and increments the database version layer.
* When subsequent transactions attempt to write their changes, the database detects the mismatched version state.
* The application rejects the out-of-date updates, rolling back the transaction and surfacing an `ObjectOptimisticLockingFailureException`.
* The `SeatController` catches this error and returns a clean `409 CONFLICT` response.

---
### Key Annotations & Architecture

The application uses the following Spring Boot and JPA annotations to separate concerns across the API, business, and data tiers:

#### 1. REST API Layer
*   `@RestController`: Exposes the class as an API controller, converting return values directly to HTTP responses.
*   `@RequestMapping("/api/seats")`: Binds the global base endpoint for seat management.
*   `@PostMapping`, `@GetMapping`, `@PutMapping`, `@DeleteMapping`: Route specific HTTP methods to CRUD operations.
*   `@RequestParam`, `@PathVariable`, `@RequestBody`: Bind URI paths, parameters, and incoming JSON payloads directly to method inputs.

#### 2. Database Persistence (JPA/Hibernate)
*   `@Entity`: Instructs Hibernate to map the `Seat` object to a relational database table.
*   `@Id` & `@GeneratedValue`: Designate and automatically calculate unique primary keys.
*   `@Column`: Overrides field configurations to prevent SQL syntax errors with reserved keywords (e.g., `row`).
*   `@Version`: Implements a version counter to execute Optimistic Locking without locking database rows.

#### 3. Core Framework & Integrity
*   `@SpringBootApplication`: Configures component scanning, auto-configuration, and application bootstrapping.
*   `@Service` & `@Repository`: Register application components into the Spring IoC container for dependency injection.
*   `@Transactional`: Sets transaction boundaries, triggering automatic database rollbacks if any unhandled exception occurs.
---
## Configuration & Setup

### 1. Prerequisites
* **Java Development Kit (JDK):** Version 17 or higher
* **Build Tool:** Maven 3.6+

### 2. Compilation and Execution

First, clone the repository and navigate into the project root directory:

```bash
git clone [https://github.com/ayus28ayushi-ai/multi-threaded-booking.git](https://github.com/ayus28ayushi-ai/multi-threaded-booking.git)
cd multi-threaded-booking
```

The application relies entirely on Spring Boot's built-in auto-configuration for an in-memory H2 database, requiring no manual configuration properties files to run out of the box.

Run the following Maven command from the project root directory:

```bash
mvn clean spring-boot:run
```
Upon successful context loading, the console logs will confirm initialization:

```text
Database initialization done with empty seats!
```
---
## API Testing Options

### 1. Swagger UI (Interactive Browser Testing)
Once the server initializes, you can visually test endpoint actions, parameter payloads, and exception responses:
* **URL:** `http://localhost:8080/swagger-ui/index.html#/`

### 2. IntelliJ HTTP Client
For raw script testing and multi-thread execution simulations, run the requests structured in your local project test file:
* **File:** `test-requests.http`

---


## Author

* ***Name:*** **Ayushi Singh**
* ***GitHub Profile***: https://github.com/ayus28ayushi-ai
 

## Acknowledgments

* Developed as a practical simulation to study concurrent transaction handling and data integrity using Spring Boot and Hibernate JPA.