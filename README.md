# UniMethod

UniMethod is a Spring Boot web application for managing university publication data, synchronizing records from a DSpace repository, and generating report files from uploaded templates.

The application starts from `src/main/java/com/example/unimethod/UniMethod.java` and uses Spring MVC, Thymeleaf, Spring Security, Spring Data JPA, MySQL, and Apache POI.

## Features

- User registration and login with Spring Security.
- Teacher accounts are registered as pending and can be approved or rejected by an administrator.
- Role-based access control for admin pages, DSpace synchronization, template deletion, and report deletion.
- Publication management:
  - create, edit, delete, list, search, filter, and sort publications;
  - validate publication form input;
  - check duplicate titles and URLs;
  - manage authors from multiline form input.
- DSpace synchronization:
  - import publications from configured DSpace department collections;
  - synchronize one department or all enabled departments;
  - track created, updated, and skipped records.
- Duplicate publication analysis using text normalization, Levenshtein similarity, TF-IDF vectorization, and cosine similarity.
- Report templates:
  - upload report templates;
  - store template files under `storage/templates`.
- Report generation:
  - generate DOCX reports from selected templates and publication filters;
  - store generated reports under `storage/reports`;
  - download generated report files.

## How It Works

1. Users register through `/register`. New users receive the `TEACHER` role and `PENDING` status.
2. An administrator reviews users from `/admin` and changes their status to active or rejected.
3. Authenticated users can manage publications from `/publications`.
4. Administrators can synchronize publication metadata from DSpace through `/sync/dspace`.
5. Uploaded report templates are saved locally and recorded in the database.
6. Report generation selects publications by request filters, fills a template through the report generator, saves the output file, and stores a report record in the database.

## Tech Stack

- Java 17
- Spring Boot 4.0.2
- Spring MVC
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL
- Maven Wrapper
- Apache POI for DOCX report generation
- H2 for tests

## Requirements

- JDK 17 or newer
- MySQL Server
- PowerShell, Windows Terminal, or another shell that can run the Maven Wrapper
- Network access if DSpace synchronization is used

## Database Setup

The default database configuration is in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db1
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

Create the local database before launching the application:

```sql
CREATE DATABASE db1 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

If your MySQL username, password, host, or database name is different, update `application.properties` before running the app.

Important: the application has admin-only screens, but this repository does not include a visible first-admin bootstrap. Make sure the database contains at least one active user with `role = 'ADMIN'` and `status = 'ACTIVE'`, or add a local bootstrap mechanism for development.

## Installation

Clone the repository and enter the project directory:

```powershell
git clone <repository-url>
cd UniMethod
```

Build the project:

```powershell
.\mvnw clean package
```

Run the tests:

```powershell
.\mvnw test
```

## Launch

Start the application with the Maven Wrapper:

```powershell
.\mvnw spring-boot:run
```

Then open:

```text
http://localhost:8080
```

You can also run the packaged JAR after building:

```powershell
java -jar target\unimethod-0.0.1-SNAPSHOT.jar
```

## Main Routes

- `/login` - sign in.
- `/register` - register a teacher account.
- `/` - home page.
- `/admin` - user administration, admin only.
- `/publications` - publication list, filters, and sorting.
- `/publications/new` - create a publication.
- `/publications/{id}/edit` - edit a publication.
- `/publications/duplicates` - possible duplicate publications.
- `/templates` - list report templates.
- `/templates/upload` - upload a report template.
- `/reports` - report dashboard.
- `/reports/generate/{templateId}` - generate a report from a template.
- `/sync/dspace` - DSpace synchronization, admin only.

## DSpace Configuration

DSpace synchronization settings are stored in `src/main/resources/application.yml`.

The configuration includes:

- base DSpace API URL;
- request page size;
- connection and read timeouts;
- enabled department collection scopes.

Change department entries or disable synchronization targets in `application.yml` when adapting the system to another repository.

## File Storage

The application creates and uses these local directories:

- `storage/templates` for uploaded templates;
- `storage/reports` for generated reports.

These paths are relative to the application working directory.

## Development Notes

- The JPA schema mode is `update`, so Hibernate updates tables automatically during local development.
- Validation is handled with Jakarta Validation annotations and service-level duplicate checks.
- Publication form data is separated from the JPA entity through `PublicationFormDto`.
- The repository contains tests for controllers, services, repositories, duplicate analysis, security configuration, and file storage.

