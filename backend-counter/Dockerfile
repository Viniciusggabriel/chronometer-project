FROM maven:3.9.6-amazoncorretto-21
LABEL authors="VINICIUS-GABRIEL"
LABEL name="backend-counter".

WORKDIR /app

# Argumentos que vão ser inseridos dentro do .env
ARG MYSQL_URL
ARG MYSQL_USER
ARG MYSQL_ROOT_PASSWORD

# Insere dessa forma dentro do .env
RUN echo "MYSQL_URL=$MYSQL_URL" >> .env
RUN echo "MYSQL_USER=$MYSQL_USER" >> .env
RUN echo "MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD" >> .env

# Arquivo de dependência
COPY pom.xml .

# Baixa as dependencias do projeto
RUN mvn dependency:go-offline -B

# Copia os arquivos
COPY src /app/src

# Compila o build .jar
RUN mvn package

# A porta com qual o projeto vai rodar
EXPOSE 8080

# Comando para executar o aplicativo
CMD ["java", "-jar", "target/backend-counter-1.0-SNAPSHOT-jar-with-dependencies.jar"]
