# 📚 Challenger Literalura  

Este projeto foi desenvolvido em **Java 17** utilizando o framework **Spring Boot**.  

## 🚀 Tecnologias e Dependências  
- **Java 17**  
- **Spring Boot**  
- **Lombok** – utilizado para redução de código boilerplate (necessário instalar e configurar o plugin e a dependência caso deseje rodar o projeto localmente).  
- **PostgreSQL** – banco de dados utilizado.  

## 🗄️ Banco de Dados  
- Usuário padrão: `postgres`  
- Senha: definida localmente pelo usuário  
- Nome do banco: `literalura` (pode ser alterado conforme preferência)  

## ✨ Boas Práticas  
Foi aplicada a técnica de **Clean Code**, garantindo melhor legibilidade e organização do código.  

## ✅ Funcionalidades  
- Todas as funcionalidades obrigatórias do desafio foram implementadas.  
- Todos os **extras opcionais** sugeridos também foram desenvolvidos e estão funcionando corretamente.  

📌 **Quadro do desafio no Trello:**  
[Literalura Challenge - Java](https://trello.com/b/eLc9Sj1A/literalura-challenge-java)  

## 🔧 Instalação e Execução  

### 1. Clonar o repositório  
```bash
git clone https://github.com/seu-usuario/challenger-literalura.git
cd challenger-literalura

CREATE DATABASE literalura;

spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=postgres
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update

3. Configurar Lombok

Certifique-se de que o plugin do Lombok esteja instalado na sua IDE (IntelliJ, Eclipse ou VS Code).
Também confirme se a dependência do Lombok está no arquivo pom.xml.






 


git clone https://github.com/seu-usuario/challenger-literalura.git
cd challenger-literalura
