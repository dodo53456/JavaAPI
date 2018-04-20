# JavaAPI
RESTFul Web Service for this pre-interview test:
https://gist.github.com/winston/51d26e4587b5e0bbf03fcad558111c08


1. System overview:
a. REST web service framework: Jersey. (It's light weight and easy to implement, perfect for this project. If the project requires more features, can choose Spring Boot.) 
b. Application server: Apache Tomcat.
c. API to convert JSON to POJO Java class: GSON (Google Java lib for JSON)
d. Data persistance: H2 In Memory database (Again, it's light weight and easy to implement. Can create run time, in memory database, easy for testing in this project.)

2. Development tools:
a. IDE: Eclipse.
b. Testing: Postman.

3. Data Modelling:
a. USER table, with primary key id.
b. USER_RELATIONSHIP table, with primary key (main_id, linked_id), 1 to many. 

4. How to test:
a. Deploy the JavaAPI.war file attached to local Tomcat server.
b. Import the postman collection file LocalTest.postman_collection.json into Postman.
c. Start the local Tomcat server, run the first REST HTTP call from Postman to create DB schema(GET request name:  "0. Create DB Schema").
d. Then, from Postman, can run the other POST requests for testing.

5. Side notes: 
a. currently, the application server is case-sensitive for the email address, but it can be easily changed to case insensitive.
b. error handling: for each REST endpoint, there are different validation rules and error handling logics.
