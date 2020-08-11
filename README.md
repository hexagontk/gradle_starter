
Gradle Starter Application
==========================
This is an Hexagon service created from a template.

Software Requirements
---------------------
To build the application you will need:
* JDK 11+ for compiling the sources.
* An Internet connection to download the dependencies.

To run the application:
* JRE 11+ (JDK is not required at runtime).

Development
-----------
* Build: `./gradlew build`
* Rebuild: `./gradlew clean build`
* Run: `./gradlew run`
* Watch: `./gradlew -t watch`
* Test (*\*Test*): `./gradlew test`
* Integration Test (*\*IT*): `./gradlew verify`
* Test Coverage: `./gradlew jacocoTestReport`
* Run Container (after assemble): `docker-compose up -d`

The reports are located in the `build/reports` directory after building the project.

Gradle Wrapper Setup
--------------------
You can change Gradle version in `gradle/wrapper/gradle-wrapper.properties`.

Usage
-----
After building the project (`./gradlew build`), archives with the application's distributions are
stored in `build/distributions`.

To install the application you just need to unpack one distribution file.

After installing the application, you can run the application executing the `bin/organization_space`
script.

Logs are stored in the `log` directory inside the script's execution directory.

Once the application is running, you can send a request executing:
`curl http://localhost:9090/text`
