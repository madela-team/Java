FROM maven:3.3.9-jdk-8 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn -DskipTests=true clean package

FROM tomcat:8.5-jre8

RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
