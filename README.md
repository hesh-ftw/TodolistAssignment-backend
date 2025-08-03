
# Todo List APP(Java,Springboot)





#### Before setting up the project, ensure you have the following installed on your system:

- Java Development Kit (JDK 17+) 

- Apache Maven

- MySQL Server


 

## Follow these steps to set up and run the project locally:

#### 1. Clone the Repository

```
https://github.com/hesh-ftw/TodolistAssignment-backend.git
```

#### 2. Clone the Repository

```
cd TodolistAssignment-backend

```

#### 3. configure and update the database credentials

```

navigate to this: src/main/resources/application.properties

create MYSQL database schema as todo_list

spring.datasource.url=jdbc:mysql://localhost:3306/your_mysql_db_name
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

```

#### 4. Build the project- use maven

```
./mvnw clean install

```

#### 5. Run the project

```
./mvnw spring-boot:run

```

#### Please find the frontend repository of this application below 

https://github.com/hesh-ftw/todolist-frontend
