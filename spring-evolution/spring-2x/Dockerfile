# Сборка на JDK 8 (Maven 3.3.9 — последняя версия, совместимая с Java 6+)
FROM maven:3.3.9-jdk-8 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Указываем, что компилируем под Java 6 (ваш pom.xml должен иметь <maven.compiler.source>1.6</maven.compiler.source>)
RUN mvn -DskipTests=true clean package

# Запуск на Tomcat 7 или 8.5 (оба работают с Spring 2.5)
FROM tomcat:8.5-jre8

# Опционально: удаляем стандартные веб-приложения
RUN rm -rf /usr/local/tomcat/webapps/*

# Копируем WAR как ROOT
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]