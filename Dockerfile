FROM openjdk:21-jdk-slim

RUN apt-get update && apt-get install -y postgresql && apt-get clean

ENV POSTGRES_DB=repsydb
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=password

COPY app.jar /app/app.jar

COPY init.sql /docker-entrypoint-initdb.d/init.sql

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

EXPOSE 5432
EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]


#FROM openjdk:21-jdk-slim
#WORKDIR /app
#COPY repsy-api/target/repsy-api-1.0.0.jar app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]
#
