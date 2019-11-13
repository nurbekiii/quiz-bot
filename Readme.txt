IN command Line:

1. BUILD:

mvnw install dockerfile:build

or

mvn dockerfile:build

2. RUN:

docker run -p 7398:7398 quiz-bot:latest

3. Stop Image:

docker container stop "your-image-name"