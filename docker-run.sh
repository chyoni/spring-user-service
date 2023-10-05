#!/bin/bash

docker run -d --network ecommerce-network \
    --name user-service \
    -e "spring.cloud.config.uri=http://config-service:8888" \
    -e "spring.rabbitmq.host=rabbitmq" \
    -e "eureka.client.serviceUrl.defaultZone=http://discovery-service:8761/eureka/" \
    -e "logging.file=/api-logs/users-ws.log" \
    cwchoiit/user-service:0.0.1

