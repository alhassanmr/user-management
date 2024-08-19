# User-management
user account management service with postgresql db

## Table of Contents
1. [Description](#description)
2. [Local Dev Set Up](#local-dev-set-up)
3. [Testing Guidelines](#testing-guidelines)
4. [Running  Application With Docker](running-application-with-docker)
5. [Testing Application](testing-application)
6. [Performance and Optimization](Performance and Optimization)
6. [Cloud Deployment](Cloud Deployment)

## Description
This a solution to manage users and authenticate the user with jwt token generation

## Local Dev Set Up
You should have the following setup beforehand:
* Java Development Kit (JDK) 21 or higher
* PostgreSQL Database
* Git
* IDE (example IntelliJ)
* Docker
* Postman / Insonmia
* (optional) Database GUI
    * Dbeaver
* Open the terminal and change to the directory you want to the project to be
* Clone the project using "git clone https://github.com/alhassanmr/user-management.git"
* Navigate to the project directory:
* Navigate to the project directory:
* Make sure to update `application.properties` with have all the appropriate database configurations once the database is configured.
* Run `mvn install` to all dependencies
* Run `mvn spring-boot:run` to run the application

## Running Application With Docker
* Open the CMD, and run the below on the CMD
* Ensure you are at the root of the project directory
* run command "docker-compose up" in the root folder to start application

## Usage and Testing Application
* Visit http://localhost:9090/swagger-ui.html to access the API documentation and test the endpoints.
* With `Postman` you can test all request type

## Performance and Optimization
### Strategies for optimizing database queries and ensuring the API performs well under load.

## Enhancing Application Performance: Database and API Optimization

To ensure the application scales efficiently, responds promptly, and operates reliably, optimizing database queries and API performance is paramount. This guide outlines strategies to achieve these goals.

### Database Optimization

**Indexing Strategically:**
* Create indexes for frequently searched fields (like username or email) to accelerate data retrieval.
* Employ composite indexes for fields often queried together.
* Avoid excessive indexing as it can hinder write operations.

**Streamline Queries:**
* Prevent the N+1 query issue by using eager or batch fetching.
* Convert subqueries to joins or existence conditions for improved efficiency.

**Leverage Caching:**
* Cache frequently accessed query results using tools like Redis or Memcached.
* Utilize query plan caching to bypass redundant execution plan generation.

**Implement Effective Pagination:**
* Limit fetched rows for large datasets to optimize performance.
* Employ keyset pagination over offset-based for better handling of large result sets.

**Utilize Optimization Tools:**
* Analyze query execution plans using tools like `EXPLAIN` to identify bottlenecks.
* Regularly profile queries to pinpoint and rectify performance issues.

**Balance Normalization and Denormalization:**
* Maintain data integrity through proper normalization.
* Consider denormalization for specific scenarios to enhance read performance, but proceed cautiously to avoid data inconsistencies.

**Optimize Data Handling:**
* Process data in batches for bulk operations to minimize database interactions.
* Efficiently manage database connections using connection pools like HikariCP.

### API Performance Optimization

**Embrace Asynchronous Processing:**
* Utilize asynchronous programming for non-blocking operations.
* Offload time-consuming tasks to background jobs using message queues.

**Implement Caching:**
* Reduce API server load by enabling HTTP caching.
* Cache expensive or frequently accessed API responses.
* Cache database query results to minimize database interactions.

**Distribute Load Effectively:**
* Employ load balancers to distribute incoming requests across multiple servers.
* Implement auto-scaling to handle fluctuating workloads.

**Manage API Usage:**
* Protect the API from abuse and ensure fair usage through rate limiting.
* Prevent individual users from overwhelming the API by applying throttling.

**Optimize Data Transfer:**
* Use efficient serialization formats like Protobuf or Avro instead of JSON or XML.
* Compress API responses using gzip or Brotli to reduce payload size.

**Efficient Data Access:**
* Fetch only necessary data through lazy loading.
* Use projections or Data Transfer Objects (DTOs) to retrieve specific fields.

**Leverage HTTP/2:**
* Enable HTTP/2 for improved performance through multiplexing, header compression, and other advantages.

**Monitor and Profile:**
* Track API performance and identify bottlenecks using monitoring tools.
* Regularly profile the API to optimize performance.

**Manage Concurrency:**
* Effectively handle concurrent requests using thread pools.
* Employ concurrency controls to manage shared resources.

**Database Connection Management:**
* Use connection pools to optimize database connection management.
* Prevent SQL injection by using prepared statements.

### How handle scaling the service if it were to receive a high volume of traffic
## Scaling the Application: Horizontal Scaling and Distributed Caching

**Horizontal scaling** is a method of increasing an application's capacity by adding more servers or instances to handle increased traffic. This approach distributes the workload across multiple machines, improving performance and reliability.

### Implementing Horizontal Scaling

To achieve horizontal scaling:

* **A load balancer:** This distributes incoming traffic across multiple instances of the application. Popular options include cloud-based solutions like AWS Elastic Load Balancer, Google Cloud Load Balancing, and Azure Load Balancer, or software-based load balancers like NGINX or HAProxy.
* **Multiple instances:** Ensure the application is designed to be stateless, meaning any instance can handle any request. Deploy multiple identical instances behind the load balancer.
* **Health checks:** Regularly monitor the health of the instances. The load balancer should only direct traffic to healthy instances.
* **Optional: Session affinity:** While generally discouraged in favor of stateless design, session affinity (sticky sessions) can be used to direct a user's requests to the same instance for specific use cases.

### Leveraging a Distributed Cache

To maintain performance and consistency in a horizontally scaled environment, a **distributed cache** like Redis is essential. It provides a centralized storage for session data and JWT revocation lists, accessible to all application instances.

To implement a distributed cache:

* **Set up Redis:** Deploy a Redis cluster that the application instances can connect to. It can use cloud-managed Redis services or self-manage it.
* **Store session data:** Save session data in Redis to ensure it's accessible from any instance. Many frameworks, like Spring Session, offer integration with Redis for session management.
* **Manage JWT revocation:** Maintain a blacklist or whitelist of revoked tokens in Redis. When a token is revoked, store it in Redis with an expiration time matching the token's lifetime. Before accepting a JWT, check Redis to verify its validity.

By combining horizontal scaling and a distributed cache, it can create a highly available and scalable application capable of handling increased traffic while maintaining data consistency.

## Cloud Deployment
### This guide gets the user-management app running on AWS!

**Prerequisites:**

* AWS Account
* AWS CLI
* Java & Maven (Optional: Docker)

**1. Prepare the App:**

* Update `application.properties` with environment variables (database, JWT Secret)
* Build the app with `mvn clean package`

**2. Set Up Database (RDS):**

* Create an RDS instance of PostgresSQL in the AWS Management Console.
* Configure the instance and security group.

**3. Deploy to Elastic Beanstalk:**

* Create an Elastic Beanstalk environment.
* Choose platform (Java) and upload the JAR file.
* Configure environment variables (DB details, JWT Secret).
* Deploy - Elastic Beanstalk handles scaling and monitoring.

**4. Secure the Deployment:**

* Configure security groups for load balancer and instances.

**5. Add a Distributed Cache (Optional):**

* Create an ElastiCache Redis cluster in the console.
* Integrate Redis with Spring Boot using `spring-boot-starter-data-redis`.

**6. Scale Horizontally (Optional):**

* Enable auto-scaling in Elastic Beanstalk based on CPU or other metrics.

**7. Monitor & Log:**

* Use AWS CloudWatch to monitor performance and set up alerts.
* Access logs through CloudWatch Logs.

**8. Access the App:**

* Elastic Beanstalk provides a URL for the application.

**9. Custom Domain (Optional):**

* Set up a Route 53 hosted zone and point the domain to the load balancer.

**10. Docker Deployment (Optional):**

* Create a Dockerfile and configure Elastic Beanstalk for Docker deployment.

**This guide provides a streamlined path to deploying the Spring Boot application on AWS!**
