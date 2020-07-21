# Mars Rover (Curiosity) Photo Album

This is a demo web application featuring Reactive Java programming in Spring Boot. The project is meant to showcase how a Spring Boot REST API can achieve high scalability with the publisher/subscriber pattern against large data streams. The project itself is broken into two parts: The Spring Boot backend and a very simple ReactJS front end.

## Notes on build instructions
Docker container build instructions have not been tested thoroughly on MacOS devices and may need to be revised. The fail-safe build process is explained here.

### Front end build
Run `yarn start` to run the ReactJS application 

### Back end build
Run `./mvnw spring-boot:run` to run the Spring application