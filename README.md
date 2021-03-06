![Java CI with Maven](https://github.com/wlanboy/KafkaHazelcastWorker/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master)

# KafkaHazelcastWorker
Spring Boot based Kafka stream, Hazelcast cache and Hazelcast Executor service

## Dependencies
At least: Java 11 and Maven 3.5
Kafka running on host with hostname 'nuc'

## Build Kafka Client
mvn package

## Run services
* cp HazelcastWorker/bootstrap.properties HazelcastWorker/target
* cp HazelcastWorker/logback.xml HazelcastWorker/target
* java -jar ./HazelcastWorker/target/hazelcastworker-1.0.0-SNAPSHOT.jar


* cp KafkaSource/bootstrap.properties KafkaSource/target
* cp KafkaSource/logback.xml KafkaSource/target
* java -jar ./KafkaSource/target/kafkasource-1.0.0-SNAPSHOT.jar


* cp KafkaSink/bootstrap.properties KafkaSink/target
* cp KafkaSink/logback.xml KafkaSink/target
* java -jar ./KafkaSink/target/kafkasink-1.0.0-SNAPSHOT.jar


## Start processing
curl -X POST \
  http://127.0.0.1:8000/job \
  -H 'Content-Type: application/json' \
  -d '{
	"work": "test"
}'

## Check results (wait - each job sleeps 10s)
curl -X GET http://127.0.0.1:8010/work
