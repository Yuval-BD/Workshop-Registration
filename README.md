# Workshop Registration System — EX2 (Maven Evolution)

A multi-module Maven project: the model and logic implementation of the Workshop
Registration API, extended with e-mail validation, JSON export and logging, packaged
as a single runnable uber jar.

## Prerequisites
- JDK 25
- Maven 3.9+

## 1. Install the given API jars into the local Maven repository (.m2)

The model, logic and tester jars are not published on Maven Central, so they must be
installed once per machine. Place the three provided 2.0 jars in the `libs/` folder,
then run these commands from the repository root:

    mvn install:install-file "-Dfile=libs/model-2.0.jar" "-DgroupId=patmal.workshops" "-DartifactId=model" "-Dversion=2.0" "-Dpackaging=jar"
    mvn install:install-file "-Dfile=libs/logic-2.0.jar" "-DgroupId=patmal.workshops" "-DartifactId=logic" "-Dversion=2.0" "-Dpackaging=jar"
    mvn install:install-file "-Dfile=libs/workshop-tester-2.0.jar" "-DgroupId=patmal.workshops" "-DartifactId=workshop-tester" "-Dversion=2.0" "-Dpackaging=jar"

(On Windows PowerShell keep the quotes exactly as shown.)

## 2. Build

    mvn clean install

Builds all modules and produces the uber jar at `app/target/workshop-cli.jar`.

## 3. Run

    java -jar app/target/workshop-cli.jar

Creates demo data, logs the events, and writes `instructors.json`, `students.json`
and `teams.json` to the current folder.

## 4. Run the tester

    java -cp "libs/workshop-tester-2.0.jar;app/target/workshop-cli.jar" patmal.workshops.main.Main

(Use `:` instead of `;` on Linux/macOS.) Expected result: `RESULT: ALL OK`.

## Project structure

    root (aggregator + top parent)
    |-- core (aggregator + parent)
    |   |-- model-impl   implementation of the model interfaces
    |   `-- logic-impl   implementation of WorkshopSystem
    `-- app              runnable application, builds the uber jar

## Third-party libraries

- JMail            e-mail validation (zero transitive dependencies)
- Jackson Databind JSON export
- Log4j 2          logging
