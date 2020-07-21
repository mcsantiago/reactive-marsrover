docker build -t marsrover:front .
docker run -it --rm -v /app/node_modules -p 3001:3000 -e CHOKIDAR_USEPOLLING=true marsrover:front