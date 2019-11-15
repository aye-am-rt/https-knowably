cd frontend;
npm install --save-dev @angular-devkit/build-angular;
ng build;

cd ..
mvn clean package -DskipTests;
sudo docker-compose up --build -d config-server;
sudo docker-compose up --build --no-recreate;
