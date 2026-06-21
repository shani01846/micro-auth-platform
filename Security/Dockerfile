# שלב 1: בניית הפרויקט עם Maven ו-Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# העתקת קובץ ה-POM והורדת תלויות (Caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# העתקת הקוד ובניית קובץ ה-JAR
COPY src ./src
RUN mvn clean package -DskipTests

# שלב 2: סביבת הריצה הרזה עם Java 21
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# העתקת ה-JAR המוכן משלב הבנייה
COPY --from=build /app/target/*.jar app.jar

# חשיפת הפורט של ה-Security
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]