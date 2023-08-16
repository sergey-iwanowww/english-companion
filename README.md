# english-companion

English companion is a service for foreign language learners integrated with a telegram bot. 

The service helps to memorize words and phrases added by the user to his dictionary. It consists of two services: 
- telegram-bot - implements integration with the telegram bot, 
- engine - implements the logic for managing a set of user phrases and asking him questions. 
The services are implemented as spring-boot applications, interaction between services is carried out through the rabbitmq message broker.

Spring Data JPA, Flyway, RabbitMQ, PostgreSQL are used.

## Building english-companion

To build, run:

    ./gradlew build
    
Then to build docker images:

    docker build -f ./services/engine/Dockerfile ./services/engine -t ec-engine
    docker build -f ./services/telegram-bot/Dockerfile ./services/telegram-bot -t ec-telegram-bot
    
## Starting english-companion

To run services, run:
    
    docker-compose -f ./docker/docker-compose.yml up -d
