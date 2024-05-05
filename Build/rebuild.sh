cd ..
cd ./Card-O-Rama-Backend/

./mvnw install
mv ./target/Card-O-Rama-Backend-0.0.1-SNAPSHOT.jar ../Build/Card-O-Rama-Backend.jar

cd ..
cd ./Card-O-Rama-ReverseProxy

./mvnw install
mv ./target/Card-O-Rama-ReverseProxy-0.0.1-SNAPSHOT.jar ../Build/Card-O-Rama-ReverseProxy.jar

cd ../Build/
docker compose up --build