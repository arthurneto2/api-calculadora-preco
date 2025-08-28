# Use uma imagem base do Java
FROM openjdk:21

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o WAR da aplicação para o contêiner
COPY ./target/api-0.0.1-SNAPSHOT.war /app/pediline_core.war

# Comando para executar a aplicação quando o contêiner iniciar
CMD ["java", "-XX:+ExitOnOutOfMemoryError", "-Duser.timezone=America/Sao_Paulo", "-jar", "pediline_core.war"]
