# payment-processor-api


# Arquitetura 
O projeto foi desenvolvido com as seguintes tecnologias
* Java 8
* Spring Boot
* Flyway
* Amazon RDS / Postgresql
* Docker

# Pré-Requisitos
A máquina (Linux ou Mac) deveriá possuir
* JDK 1.8
* Maven 3.0+
* Docker

# Instalação
Executar o script bootstrap.sh presente na raiz do projeto. Este script executará:
* Os scripts de migration presentes na pasta /migration do projeto. Os scripts criam os esquemas e tabelas na base, além de inserir alguns dados dummy.
* Construirá as imagens docker ruhan/accounts-api e ruhan/transactions-api
'''
mvn clean install dockerfile:build
'''
* Subirá os containers das imagens criadas no passo anterior

# Acesso
* accounts-api estará disponível na porta 8082
* transactions-api estará disponível na porta 8080

# Testes
Os testes automáticos são executados na fase de instalação