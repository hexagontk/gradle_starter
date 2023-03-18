
# Gradle Starter Application
This is a Hexagon service created from a template.

## Software Requirements
To build the application you will need:
* JDK 11+ for compiling the sources.
* An Internet connection to download the dependencies.

To run the application:
* For the Gradle distribution: JRE 11+ is required (JDK is not required at runtime).
* For the jpackage bundle: any major OS will run it (Alpine Linux causes problems).
* To run the native executable there is no specific requirements.

## Development
* Build: `./gradlew build`
* Rebuild: `./gradlew clean build`
* Run: `./gradlew run`
* Test (*\*Test*): `./gradlew test`
* Integration Test (*\*IT*): `./gradlew verify`
* Test Coverage: `./gradlew jacocoTestReport`
* Run Container (after assemble): `./gradlew dockerBuild && docker-compose up -d`

The reports are located in the `build/reports` directory after building the project.

## Gradle Wrapper Setup
You can change the Gradle version in `gradle/wrapper/gradle-wrapper.properties`.

## Docker
To generate the Docker images, you can use the `dockerBuild` Gradle task: `./gradlew dockerBuild`.
After that you can start the whole service stack executing: `docker-compose up -d`.

## Usage
After building the project (`./gradlew build`), archives with the application's distributions are
stored in `build/distributions`.

To install the application you just need to unpack one distribution file.

After installing the application, you can run the application executing the `bin/gradle_starter`
script.

Once the application is running, you can send a request executing:
`curl http://localhost:9090/text`

## Native Image
```bash
./gradlew -P agent test
./gradlew nativeCompile

# Executable
build/native/nativeCompile/gradle_starter

# Memory
ps -o rss -C gradle_starter
```
