FROM openjdk:17

# Создаем директорию для приложения
WORKDIR /app

# Копируем JAR-файл вашего приложения в контейнер
COPY target/BankProcessor-0.0.1-SNAPSHOT.jar /app/BankProcessor-0.0.1-SNAPSHOT.jar

# Команда для запуска приложения
CMD ["java", "-jar", "BankProcessor-0.0.1-SNAPSHOT.jar"]