***********Knowably***********

Run ```mvn clean compile package``` to package the service

It takes the structured query from the queryService and generate cypherQuery in order to fetch result from Neo4J

Publishes the result in kafka under a specific topic which is consumed by notification-service.
