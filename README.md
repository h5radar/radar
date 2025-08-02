# H5Radar Base Technology Catalog

This repository contains the base catalog of technologies used by the H5Radar platform. Each technology is enriched with metadata such as domain classification and maturity level to enable structured visualization and analysis. This is a Spring Boot based application.

H5Radar application consists of several modules. For an overview of the entire platform and its repositories, please see the [organization-level README](https://github.com/h5radar).

## Features

- Comprehensive list of technologies with metadata
- Classification by domain (e.g., Languages, Infrastructure, Datastores)
- Maturity levels for each technology (e.g., Adopt, Trial, Assess, Hold)
- License information for compliance tracking

## Resources

- [Website](https://www.h5radar.com)
- [Documentation](https://docs.h5radar.com)
- [Live Demo](https://app.h5radar.com)
- [Blog](https://blog.h5radar.com)

## Contributing

Contributions and issues are welcome. Please open pull requests or issues in this repository. For general discussions, roadmap input, and questions, please join our [organization Discussions](https://github.com/orgs/h5radar/discussions). We appreciate community involvement in shaping H5Radar!

## License

This repository is part of the H5Radar project and is licensed under the MIT License.

# Setup environment
## Windows environment
* download and install java, at least jdk-21
* create JAVA_HOME environment variable with value C:\Program Files\Java\jdk-21
* exit and run console again to apply environment variables
* download and install maven, at least 3.8.7
* create JAVA_HOME environment variable with value C:\apache-maven-3.8.7
* exit and run console again to apply environment variables
* setup GitHub account and add ssh keys to GitHub profile in order to clone repo
* build application by command: mvn clean package -Pdev -Dmaven.test.skip from root folder
* run application by command: mvn spring-boot:run -Pdev from root folder
* open browser with url http://127.0.0.1:8080 to view application

## Working with embedded H2 DB(In-Memory)
* run application by command: "mvn spring-boot:run -Pdev"
* enter http://localhost:8080/h2-console to browser
* enter "jdbc:h2:mem:h5radar" into JDBC URL field
* enter "h5radar" into User Name field
* enter "secret" into Password field

## Idea configuration
### Java checkstyle
* use config/h5radar_checks.xml as coding style
* setup "hard wrap at" value to 120

# Conventions
## Git conventions
* the first letter of the commit should be written in upper case
* the simple perfect should be used for commit message
* the title and description should be provided, for example by command: git commit -m "title" -m "description"

# Keycloak configuration
* download keycloack, uppack it and start by command: kc.sh start-dev --http-port=8180
* login to console at http://127.0.0.1:8180 and click create realm button
* select json file realm.json at docker folder to create a new realm

# Useful commands:
* build package by command: ./mvnw clean package -Dmaven.test.skip
* run application with profile by command: ./mvnw spring-boot:run -Pdev
* run application with by command: java -jar h5radar-x.y.z.jar --application.keys.google_analytics=123
* run application with by command: java -jar h5radar-x.y.z.jar --spring.liquibase.label-filter=de
* run database migration by command: ./mvnw liquibase:update
* create checkstyle report by command: ./mvnw checkstyle:checkstyle
* get coverage report by command: ./mvnw jacoco:report -Pdev
* create javadoc by command: ./mvnw javadoc:javadoc
* create cross reference source by command: ./mvnw jxr:jxr
* create site by command: ./mvnw site
* run spotbugs GUI by command: ./mvnw spotbugs:gui
* run sonarqube analysis by command: ./mvnw sonar:sonar
* run single test by command: ./mvn test -Dtest=TechnologyIntegrationTests
* run docker compose by command: docker-compose -f ./postgresql.yml up
* view metrics at url: http://127.0.0.1:8080/actuator/prometheus
* view swagger at url: http://127.0.0.1:8080/swagger-ui/index.html
* view api docs at url: http://127.0.0.1:8080/v3/api-docs
* build docker by command: docker build -t h5radar/radar:latest .
* run docker by command: docker run -p 8070:8070 h5radar/radar:latest
