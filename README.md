# mint
Mint Coding Test

# to run
# set environment viraibles
export KAFKA_ADDRESSES='xxxxxxxxx'
//url of the cardservice 
export BASE_URL='xxxxxxx'

#Build with docker

//build cardservice app by running below command in terminal\
mvn package && java -jar target/cardservice-docker-0.1.0.jar
//docker build
docker build -t djbabs/cardservice --network="host" -f DockerFile

//build cardconsumer app by running below command in terminal\
mvn package && java -jar target/cardconsumer-docker-0.1.0.jar
//docker build
docker build -t djbabs/cardconsumer --network="host" -f DockerFile

