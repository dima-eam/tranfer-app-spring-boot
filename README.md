# tranfer-app-spring-boot
Simple money transfer service

This application build with Maven (so you need it). To make a test:

1. Checkout project and cd into project directory
2. Run *mvn clean package*
3. If build is successful, run *java -jar target/transfer-service-1.0.0-SNAPSHOT.jar* (port 8080)
4. Running application can be tested with any rest client. Example:

    *POST http://localhost:8080/app/api/account/create*
    
    *BODY {"name":"test1","balance":"100.00"}*
    
TODOs
-
0. enable oauth by config (conditional)
1. Add Dockerfile and docker-compose
2. Add Kafka producer/consumer
3. Add integration tests for services
4. https endpoint (separate configs and props)
5. jmh module
6. update to spring 5, spring boot 2
7. actuator metrics