# user-management
## Table of Contents
1. [Description](#description)
2. [Local Dev Set Up](#local-dev-set-up)
3. [Testing Guidelines](#testing-guidelines)
4. [Running  Application With Docker](running-application-with-docker)
5. [Testing Application](testing-application)

## Local Dev Set Up
You should have the following setup beforehand:
* Java Development Kit (JDK) 8 or higher
* PostgreSQL Database
* Git
* IDE (example IntelliJ)
* Docker
* Postman
* (optional) Database GUI
    * Dbeaver
* Open the terminal and change to the directory you want to the project to be
* Clone the project using "git clone https://github.com/alhassanmr/user-management.git"
* Navigate to the project directory:
* Navigate to the project directory:
* Make sure to update `application.properties` with have all the appropriate database configurations once your database is configured.
* `mvn install` all dependencies
* `mvn spring-boot:run` to run the application

## Running Application With Docker
* Open your CMD, and run the below on your CMD
* Ensure you are at the root of the project directory
* run command "docker-compose up" in the root folder to start application

## Usage and Testing Application
* Visit http://localhost:8080/swagger-ui.html to access the API documentation and test the endpoints.
* With `Postman` you can test all request type

## Contributing
* Fork the repository.
* Create a new branch for your feature or bug fix.
* Make your changes and commit them.
* Push the changes to your fork.
* Create a Pull Request (PR)
* Review and Merge
* Keep Your Fork Synced