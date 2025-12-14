# Project Management System

![Version](https://img.shields.io/badge/version-1.0.0-blue) ![Build](https://img.shields.io/badge/build-passing-brightgreen) ![License](https://img.shields.io/badge/license-MIT-green)

A comprehensive **Fullstack Application** designed to streamline project tracking and task management.
This solution provides a secure and intuitive platform for users to create projects, manage task lifecycles, and visualize real-time progress through dynamic dashboards.

---

## Author
**Loubna Tanassa**
* Software & Network Engineering Student (5th Year)
* Fullstack Developer (Java/Angular)

---

## Technical Architecture

This project is built using a modern, decoupled architecture ensuring scalability and security.

### Backend (Server Side)
* **Framework:** Spring Boot 3
* **Language:** Java 17
* **Security:** Spring Security with JWT (Stateless Authentication)
* **Database:** MySQL
* **Persistence:** Spring Data JPA (Hibernate)

### Frontend (Client Side)
* **Framework:** Angular 17+ (Standalone Components)
* **Styling:** Bootstrap 5 (Responsive Design)
* **Communication:** REST API via HttpClient & RxJS
* **Routing:** Angular Router with Auth Guards

---

## Key Features

### Security & Access Control
* **Secure Registration:** User account creation with BCrypt password hashing.
* **JWT Authentication:** Stateless session management using JSON Web Tokens.
* **Protected Routes:** Frontend guards to prevent unauthorized access.

### Project Dashboard
* **Project Creation:** Intuitive interface to create and organize new IT projects.
* **Progress Tracking:** Dynamic progress bars automatically calculated (0% to 100%) based on completed tasks.
* **Data Visualization:** Instant overview of all active projects.

### Task Management
* **Granular Control:** Add, edit, and delete tasks within specific projects.
* **Real-time Updates:** "Toggle" completion status to instantly update project metrics.
* **Smart Filtering:** Clean interface to distinguish between pending and completed tasks.

---

## Installation & Setup Guide

### System Requirements
* **Java JDK 17** or higher
* **Node.js v18+** & **NPM**
* **MySQL Server**

### Step 1: Database Setup
Create an empty database in MySQL shell or Workbench:
```sql
CREATE DATABASE gestion_projet_db;
