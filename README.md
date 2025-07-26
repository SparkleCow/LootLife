# What is LootLife

LootLife is a Spring Boot application that gamifies personal task management by applying RPG mechanics to real-world productivity. 
Users create and complete tasks to earn experience points across different character attributes, creating an engaging system that makes productivity 
feel like playing a role-playing game

## Advance Consortium

## The system transforms traditional task management by introducing:

- Character Progression: Users gain levels and experience points
- Attribute System: Seven different stats (Strength, Intelligence, Wisdom, Charisma, Dexterity, Constitution, Luck)
- Task Difficulty Scaling: Tasks have different difficulty levels that affect XP rewards
- Mission Completion Tracking: Comprehensive statistics on completed tasks

## Technologies Used in LootLife

- Java 21 → Application runtime environment

- Spring Boot 3.4.5 → Application framework and dependency injection

- Spring OAuth2 Client – External authentication integration (OAuth2).

- PostgreSQL → Primary data persistence

- Spring Data JPA → Object-relational mapping (ORM)

- Spring Security 6.x → Authentication and authorization

- Spring OAuth2 Client → External authentication providers

- JJWT 0.12.6 → JSON Web Token handling

- MapStruct 1.6.3 → DTO to entity conversion

- Lombok 1.18.32 → Boilerplate code reduction

- Spring Mail → Email notifications

- Thymeleaf → HTML template rendering

- Maven → Dependency management and build tool

- Angular 19.2.14
  
- TypeScript 5.5.2

- Tailwind CSS 4.1.11

## High-Level System Architecture


<img width="1326" height="606" alt="image" src="https://github.com/user-attachments/assets/d93d777f-70c5-4c85-938b-00a96b80c1ce" />


## Core Entity Relationships

<img width="991" height="903" alt="Captura de pantalla 2025-07-20 210648" src="https://github.com/user-attachments/assets/42c2f111-7977-4386-9823-b40851ddd45c" />

## Backend Architecture

<img width="1211" height="855" alt="image" src="https://github.com/user-attachments/assets/11b06aa4-f4f5-40b0-b622-795b6883eaeb" />

## Key Features
### Authentication System
Dual Authentication: Supports traditional username/password and OAuth2 (Google, GitHub)
JWT Token Management: Secure token-based authentication with expiration handling
Account Verification: Email-based account activation system
Role-based Access Control: User roles and permissions management
### Gamification Mechanics
Multi-Attribute Progression: Seven distinct character attributes that level independently
Dynamic XP Calculation: Experience rewards based on task difficulty and completion
Task Categorization: Tasks can affect multiple stat categories simultaneously
Progress Tracking: Comprehensive statistics on missions completed and character growth
### Task Management
Flexible Task System: Support for both simple completion and quantitative progress tasks
Difficulty Scaling: Four difficulty levels (Easy, Medium, Hard, Extreme) affecting rewards
Deadline Management: Optional task deadlines with expiration handling
Active/Inactive States: Task lifecycle management
### Technical Features
RESTful API Design: Clean API endpoints for all operations
Data Validation: Comprehensive input validation using Bean Validation
Email Integration: HTML email templates for notifications
PostgreSQL Integration: Robust data persistence with JPA/Hibernate
MapStruct Integration: Efficient DTO mapping with compile-time code generation

## How can I install it ? 

### Clone the repository:
Use the following command in your Git terminal:
git clone https://github.com/SparkleCow/LootLife.git

### Start the containers:
Run docker-compose up -d to start the database.

### Configure Spring:
If you have any custom configurations or modify attributes in the Docker Compose file, make sure to update the corresponding variables.

### Configure Angular:
Run npm install to download all the Node modules used in the project.

### Start backend and frontend:
Once the container is up and running, start the Spring and Angular services:
Use mvn spring-boot:run for Spring and ng serve for Angular.
