#!/bin/sh
set -e -x

sudo true

cd ./migration
mvn flyway:migrate

cd ../accounts-api
mvn clean install dockerfile:build

cd ../transactions-api
mvn clean install dockerfile:build

docker run -d -p 8082:8082 --name accounts-api ruhan/accounts-api
docker run -d --name transactions-api -p 8080:8080 --link accounts-api:accounts-api ruhan/transactions-api