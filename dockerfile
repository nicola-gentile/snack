FROM maven:3.9-amazoncorretto-21-alpine

RUN apk update && apk add --no-cache wget unzip
RUN wget https://github.com/nicola-gentile/snack/archive/refs/heads/asd.zip && \
    mkdir app && unzip asd.zip -d app && cd /app/snack-asd && mvn clean install

EXPOSE 8080:8080

ENTRYPOINT java -jar /app/snack-asd/target/snack-1.0-SNAPSHOT.jar



