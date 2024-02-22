# ClickHouse Data Uploader

The ClickHouse Data Uploader is a Java application designed to upload advertising and user activity data into a ClickHouse database. It includes functionalities to create tables, materialized views, and run Docker containers for ClickHouse server.

## Prerequisites

- Java Development Kit (JDK) installed (minimum version: 8)
- Maven installed
- Docker installed (for running ClickHouse server as a Docker container)

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/Artyom7577/clickHouseProject.git
cd clickHouseProject
```

### 2. Configure ClickHouse Connection

Open the relevant Java classes (`AdvertisingDataUploader`, `CreateUserSessionsMaterializedView`, `DockerRunner`, `UserActivityDataUploader`) and update the `jdbcUrl` variable with the appropriate ClickHouse database connection URL.

### 3. Install ClickHouse JDBC Driver

Ensure that the ClickHouse JDBC driver is added to your project's dependencies. You can find the latest version [here](https://clickhouse.tech/docs/en/getting-started/drivers/jdbc/).

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run Docker Container for ClickHouse Server

```bash
java -cp target/your-application.jar org.example.DockerRunner
```

### 6. Upload Advertising Data

Edit the `AdvertisingDataUploader` class if needed and run:

```bash
java -cp target/your-application.jar org.example.AdvertisingDataUploader
```

### 7. Create User Sessions Materialized View

Edit the `CreateUserSessionsMaterializedView` class if needed and run:

```bash
java -cp target/your-application.jar org.example.CreateUserSessionsMaterializedView
```

### 8. Upload User Activity Data

Edit the `UserActivityDataUploader` class if needed and run:

```bash
java -cp target/your-application.jar org.example.UserActivityDataUploader
```

**Note: Please execute the following steps in sequence:**
1. Run `DockerRunner` class to start the ClickHouse server as a Docker container.
2. Run `AdvertisingDataUploader` to upload advertising data.
3. Run `CreateUserSessionsMaterializedView` to create the user sessions materialized view.
4. Run `UserActivityDataUploader` to upload user activity data.

This sequence ensures a proper setup and data flow in the ClickHouse database.