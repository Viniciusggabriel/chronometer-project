FROM maven:3.9.6-amazoncorretto-21
LABEL authors="VINICIUS-GABRIEL"
LABEL name="backend-counter".

WORKDIR /app

# Arquivo de dependência
COPY pom.xml .

# Baixa as dependencias do projeto
RUN mvn dependency:go-offline -B

# Copia os arquivos
COPY src /app/src
COPY .env /app/.env

# Compila o build .jar
RUN mvn package

# A porta com qual o projeto vai rodar
EXPOSE 8080

# Comando para executar o aplicativo
CMD ["java", "-jar", "target/backend-counter-1.0-SNAPSHOT-jar-with-dependencies.jar"]
