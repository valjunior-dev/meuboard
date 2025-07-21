# MeuBoard

## Descrição
MeuBoard é um gerenciador de boards (quadro de tarefas) desenvolvido em Java, com funcionalidades de CRUD, movimentação de cards, bloqueio/desbloqueio e controle de versões do banco usando Liquibase.

## Tecnologias usadas
- Java 17  
- Liquibase para versionamento do banco de dados  
- MySQL  
- Gradle  
- Git para versionamento  

## Funcionalidades
- Criar, editar e deletar boards e cards  
- Movimentar cards entre colunas  
- Bloquear e desbloquear cards  
- Controle de versões do banco com scripts Liquibase  

## Como rodar o projeto
1. Clone o repositório:

   ```bash
   git clone https://github.com/valjunior-dev/meuboard.git
   cd meuboard
   
2. Configure o banco de dados MySQL:

- Certifique-se de que o MySQL está instalado e rodando.
- Crie um banco com o nome esperado (ex: `meuboard`).
- Atualize o arquivo `src/main/resources/application.properties` com as credenciais corretas para seu ambiente:

