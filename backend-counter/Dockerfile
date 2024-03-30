FROM maven:3.9.6-amazoncorretto-21
LABEL authors="VINICIUS-GABRIEL"
LABEL name="backend-counter".

WORKDIR /app

# Copie apenas o arquivo de definição de dependências primeiro, para que ele possa ser cacheado
COPY pom.xml .

# Baixe as dependências do Maven
RUN mvn dependency:go-offline -B

# Copie os arquivos restantes do projeto
COPY src /app/src
COPY .env /app/.env

# Compile o aplicativo (ignorando os testes)
RUN mvn package

# Expor a porta na qual o aplicativo será executado
EXPOSE 8080

# Comando para executar o aplicativo
CMD ["java", "-jar", "target/backend-counter-1.0-SNAPSHOT-jar-with-dependencies.jar"]
