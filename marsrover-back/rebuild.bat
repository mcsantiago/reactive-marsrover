./mvnw package
docker build -t demo/marsrover .
docker run -p 8080:8080 demo/marsrover