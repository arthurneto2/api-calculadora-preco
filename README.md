# 🏷️ API Calculadora de Custo de Produtos  
`Java` `Spring Boot` `JPA` `JWT` `PostgreSQL` `Docker`

## 🔍 Descrição  
API desenvolvida para calcular o custo total e preço de venda de produtos personalizados, com base em:  
- **Insumos cadastrados** (matérias-primas com custo unitário)  
- **Quantidade de insumos** utilizados por produto  
- **Margem de lucro** definida pelo usuário

## ⚙️ Funcionalidades  
- ✅ Cadastro de insumos e produtos  
- 🔢 Cálculo automático de custo total + margem  
- 🔒 Autenticação via JWT (Spring Security)  

## 🛠️ Tecnologias  
- **Backend**:  
  ![Java](https://img.shields.io/badge/Java-17%2B-%23ED8B00?logo=openjdk)  
  ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1%2B-%236DB33F?logo=spring)  
  ![JPA](https://img.shields.io/badge/JPA-Hibernate-%2300A98F?logo=hibernate)  
- **Segurança**:  
  ![JWT](https://img.shields.io/badge/JWT-Auth0-%23000000?logo=jsonwebtokens)  
- **Banco de Dados**:  
  ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15%2B-%234479A1?logo=postgresql)  
  ![Docker](https://img.shields.io/badge/Docker-24.0%2B-%232496ED?logo=docker)  

## 🚀 Execução com Docker Compose

### Pré-requisitos
- Docker 24.0+
- Docker Compose 2.20+
- Java 17 (apenas para desenvolvimento)
- Maven (apenas para desenvolvimento)

### Passo a Passo

1. **Clone o repositório**:
   ```bash
   git clone https://github.com/arthurneto2/api-calculadora-preco.git
   cd api-calculadora-preco
2. **Suba os containers (PostgreSQL + API)**:
    ```bash
    docker-compose -f src/main/resources/docker-compose.yml up -d
    Execute a aplicação Spring Boot:
3.**Execute a aplicação Spring Boot**:
  ```bash
    mvn spring-boot:run
