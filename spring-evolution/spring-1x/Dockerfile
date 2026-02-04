# ===========================
# Stage 1 — Build WAR with Maven
# ===========================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Copy project files
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn -e -X -DskipTests=true clean package


# ===========================
# Stage 2 — Run with Tomcat
# ===========================
FROM tomcat:8.5-jdk8

# Remove default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy generated WAR
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
