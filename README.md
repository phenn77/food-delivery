# Meal Delivery API

Tech trace :
- Java 11
- Maven 11

IDE :
- IntelliJ IDEA 2021.2.3 (Community Edition)

Framework :
- Spring Boot


##Step to Start Application
1. Clone the application
2. Open using desired IDE
3. Let Maven run and download libraries
4. Run the application

###Check DB
in **application.yml** file, it's being defined as
````
spring:
    h2:
      console:
           enabled: true
````
or can be simplified to
>spring.h2.console.enabled: true

Hence, to check DB, simply go to Browser and run this URL :
>http://localhost:8080/h2-console

DB Credential :
````
Username : sa
Password : password
````

###Swagger Usage
To retrieve all APIs in the application, we use Swagger to doc them
>http://localhost:8080/swagger-ui/index.html

In there, will be list all of API with Request Body or Params needed to call on the process.

###Explanation
Library :
- Mandatory lib to run the application
```` 
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```` 
- **Mockito** : For Unit Testing
````
<dependencies>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>3.12.4</version>
        <scope>test</scope>
    </dependency>
</dependencies>
````
- **H2 Database**
````
<dependencies>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
````
- **Lombok** : Auto generating Setter / Getter
````
<dependencies>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
````
- **Jackson** : Read / Convert File or Raw JSON
````
<dependencies>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.13.0</version>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-core</artifactId>
        <version>2.13.0</version>
    </dependency>
</dependencies>
````
- **Swagger** : Specification for Documenting REST API
````
<dependencies>
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-ui</artifactId>
        <version>1.5.2</version>
    </dependency>
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-ui</artifactId>
        <version>1.5.2</version>
    </dependency>
</dependencies>
````

###Tasks
- List all restaurants that are open at a certain datetime
- List all restaurants that are open for x-z hours per day or week
- List all restaurants that have x-z number of dishes within a price range
- Search for restaurants or dishes by name, ranked by relevance to search term
- Search for restaurants that has a dish matching search term
- The top x users by total transaction amount within a date range
- The most popular restaurants by transaction volume, either by number of transactions or transaction amount
- Total number of users who made transactions above or below $v within a date range
- List all transactions belonging to a restaurant
- List all transactions belonging to a user
- Process a user purchase in an atomic manner and ensure the changes of cash balances can be applied safely and correctly.