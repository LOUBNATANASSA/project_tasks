# Project Management System

![Version](https://img.shields.io/badge/version-1.0.0-blue)
![Build](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-green)
![Java](https://img.shields.io/badge/Java-21-red)
![Angular](https://img.shields.io/badge/Angular-21-DD0031)
![Tests](https://img.shields.io/badge/Testing-JUnit%20%7C%20Vitest-success)

A comprehensive **Fullstack Application** designed to streamline project tracking and task management.  
This solution provides a secure and intuitive platform for users to create projects, manage task lifecycles, and visualize real-time progress through dynamic dashboards.

---

## 🎥 Video Demo

 **Watch the full application demo here:**  
https://drive.google.com/file/d/1FVVWXv_7R68ksn749Eq7tpWRyoGKosyE/view?usp=sharing

---

## Author
**Loubna Tanassa**  
* Software & Network Engineering Student (5th Year)  
* Fullstack Developer (Java / Angular)

---

## Technical Architecture

This project follows a **modern, scalable, and test-driven architecture**, ensuring performance, security, and maintainability.

---

## Backend (Server Side)

* **Framework:** Spring Boot 3
* **Language:** **Java 21**
* **Security:** Spring Security with JWT (Stateless Authentication)
* **Database:** MySQL
* **Persistence:** Spring Data JPA (Hibernate)

### Backend Testing
* Unit Testing with **JUnit 5**
* Mocking with **Mockito**
* Service & Repository layer validation

---

## Frontend (Client Side)

* **Framework:** **Angular 21** (Standalone Components)
* **Build & CLI Tools:**
  * `@angular/cli` **21.0.3**
  * `@angular/build` **21.0.3**
* **Core Packages:**
  * `@angular/core` **21.0.5**
  * `@angular/common` **21.0.5**
  * `@angular/router` **21.0.5**
  * `@angular/forms` **21.0.5**
* **Styling:** Bootstrap 5 (Responsive Design)
* **Communication:** REST API via HttpClient & RxJS
* **Reactive Programming:** **RxJS 7.8.2**
* **Language:** **TypeScript 5.9.3**
* **Zone Management:** `zone.js 0.16.0`

### Frontend Testing
* Unit Testing with **Vitest 4**
* Component and service isolation

---

## Key Features

### Security & Access Control
* Secure user registration with **BCrypt password hashing**
* **JWT Authentication** (stateless)
* Protected routes using Angular **Auth Guards**

### Project Dashboard
* Create and manage IT projects
* Dynamic progress tracking (0% → 100%)
* Real-time overview of all projects

### Task Management
* Add, edit, and delete tasks
* Toggle task completion with instant updates
* Filter completed and pending tasks

---

##  Testing & Code Quality

* Backend unit tests for business logic
* Mocked dependencies for isolated testing
* Clean and maintainable codebase

---

##  How to Run the Application

### Prerequisites
* **Java JDK 21**
* **Node.js v18+**
* **NPM**
* **MySQL Server**
* **Angular CLI 21**


```bash
# 1) Clone the repository
git clone https://github.com/LOUBNATANASSA/project_tasks.git
cd project_tasks

# 2) Create the MySQL database
# (Make sure MySQL server is running)
mysql -u root -p <<EOF
CREATE DATABASE IF NOT EXISTS gestion_projet_db;
EOF


# 3) Run Backend (Spring Boot)
cd backend
mvn clean install
mvn spring-boot:run

# Backend available at:
# http://localhost:8080

# 4) Run Frontend (Angular)
cd ../frontend
npm install
ng serve

# Frontend available at:
# http://localhost:4200

