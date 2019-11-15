IN command Line:

1. BUILD:

mvnw install dockerfile:build

or

mvn dockerfile:build

2. RUN:

docker run -p 7398:7398 quiz-bot:latest

3. Stop Image:

docker container stop "quiz-bot:latest"
---------------------------------------------------------------------------------------
  OR

  1. BUILD PROJECT:

  mvn clean install

  2. BUILD Image

  docker build -t gitlab.beeline.kg:4567/competencecup/quiz-bot .

  3. PUSH Image

  docker push gitlab.beeline.kg:4567/competencecup/quiz-bot
---------------------------------------------------------------------------------------

