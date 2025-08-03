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

# Appendix 1: Useful commands

- build package by command: ./mvnw clean package -Dmaven.test.skip
- run application with profile by command: ./mvnw spring-boot:run -Pdev
- run application with by command: java -jar h5radar-x.y.z.jar --application.keys.google_analytics=123
- run application with by command: java -jar h5radar-x.y.z.jar --spring.liquibase.label-filter=de
- run database migration by command: ./mvnw liquibase:update
- create checkstyle report by command: ./mvnw checkstyle:checkstyle
- get coverage report by command: ./mvnw jacoco:report -Pdev
- create javadoc by command: ./mvnw javadoc:javadoc
- create cross reference source by command: ./mvnw jxr:jxr
- create site by command: ./mvnw site
- run spotbugs GUI by command: ./mvnw spotbugs:gui
- run sonarqube analysis by command: ./mvnw sonar:sonar
- run single test by command: ./mvn test -Dtest=TechnologyIntegrationTests
- run docker compose by command: docker-compose -f ./postgresql.yml up
- view metrics at url: http://127.0.0.1:8080/actuator/prometheus
- view swagger at url: http://127.0.0.1:8080/swagger-ui/index.html
- view api docs at url: http://127.0.0.1:8080/v3/api-docs
- build docker by command: docker build -t h5radar/radar:latest .
- run docker by command: docker run -p 8070:8070 h5radar/radar:latest
