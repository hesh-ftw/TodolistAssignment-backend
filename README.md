
# Todo List APP(Java,Springboot)





#### Before setting up the project, ensure you have the following installed on your system:

- Java Development Kit (JDK 17+) 

- Apache Maven

- MySQL Server


 



## Follow these steps to set up and run the project locally:

#### 1. Clone the Repository

```http
git clone https://github.com/hesh-ftw/TodolistAssignment-backend.git
```

#### 2. Clone the Repository

```http
cd TodolistAssignment-backend

```

#### 3. configure and update the database credentials

```http

navigate to this: src/main/resources/application.properties

create MYSQL database schema as todo_list

spring.datasource.url=jdbc:mysql://localhost:3306/your_mysql_db_name
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

```

#### 4. Build the project- use maven

```http
./mvnw clean install

```

#### 5. Run the project

```http
./mvnw spring-boot:run

```

