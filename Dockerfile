FROM maven:3.9-amazoncorretto-21-alpine

RUN apk update && apk add --no-cache git
RUN git clone --branch=asd https://github.com/nicola-gentile/snack.git
WORKDIR snack
RUN mvn clean install

EXPOSE 8080

ENTRYPOINT java -jar target/snack-1.0-SNAPSHOT.jar
