# What is LootLife

LootLife is a Spring Boot application that gamifies personal task management by applying RPG mechanics to real-world productivity. 
Users create and complete tasks to earn experience points across different character attributes, creating an engaging system that makes productivity 
feel like playing a role-playing game

## The system transforms traditional task management by introducing:

- Character Progression: Users gain levels and experience points
- Attribute System: Seven different stats (Strength, Intelligence, Wisdom, Charisma, Dexterity, Constitution, Luck)
- Task Difficulty Scaling: Tasks have different difficulty levels that affect XP rewards
- Mission Completion Tracking: Comprehensive statistics on completed tasks

## Technologies Used in LootLife

- Java 21 → Application runtime environment

- Spring Boot 3.4.5 → Application framework and dependency injection

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

## Core Entity Relationships

![image](https://github.com/user-attachments/assets/13851ea6-f29d-442e-8653-d2bb47876785)

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
