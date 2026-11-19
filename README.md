# Workshop Registration - EX2 (Maven Evolution)

The workshop registration system of EX1, moved into a Maven multi-module project.

## Setup: installing the API jars into the local repository

The API jars (`model-2.0.jar`, `logic-2.0.jar`) and the tester (`workshop-tester-2.0.jar`) are not on Maven Central,
so they have to be installed into the local repository (`.m2`) once on every computer the project is built on.
The jars are in the `lib` folder. Run these commands from the root folder of the project:

```
mvn install:install-file -Dfile=lib/model-2.0.jar -DgroupId=patmal.workshops -DartifactId=model -Dversion=2.0 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=lib/logic-2.0.jar -DgroupId=patmal.workshops -DartifactId=logic -Dversion=2.0 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=lib/workshop-tester-2.0.jar -DgroupId=patmal.workshops -DartifactId=workshop-tester -Dversion=2.0 -Dpackaging=jar -DgeneratePom=true
```

`-DgeneratePom=true` matters: every jar holds a `pom.xml` of its own (under `META-INF/maven`) whose parent,
`patmal.workshops:workshops-aggregator:1.0-SNAPSHOT`, does not exist anywhere. Without the flag Maven installs
that pom, and every build that depends on the jar fails with
`Could not find artifact patmal.workshops:workshops-aggregator:pom:1.0-SNAPSHOT`.
With the flag Maven writes a new minimal pom with only the coordinates above.

After installing, the jars are found under `<user home>/.m2/repository/patmal/workshops/`.

## Project structure

```
root                  pom: aggregator of core and app, top parent
|-- core              pom: aggregator of model-impl and logic-impl, their parent (its parent is root)
|   |-- model-impl    jar: implementation of the model interfaces
|   `-- logic-impl    jar: implementation of WorkshopSystem (depends on model-impl)
`-- app               jar: the runnable application (depends on logic-impl)
```

All the versions (Java, the API jars, the tester, our own modules) are declared once in the root `pom.xml`:
as properties, and in `dependencyManagement`. The child poms list their dependencies without a version.

## Libraries

| Library | Version | Module | Scope | Used for |
|---|---|---|---|---|
| Log4j 2 (`log4j-api`) | 2.26.1 | logic-impl | compile | logging in the code |
| Log4j 2 (`log4j-core`) | 2.26.1 | app | runtime | the logging implementation; configured by `app/src/main/resources/log4j2.xml` |
| commons-validator | 1.11.0 | logic-impl | compile | e-mail validation |
| Gson | 2.15.0 | logic-impl | compile | JSON export |

Log4j is imported into `dependencyManagement` as a BOM (`log4j-bom`), so one property controls the versions of all its artifacts.

## Build

From the root folder:

```
mvn clean install
```

The build creates the uber jar `app/target/workshop-cli.jar` (maven-assembly-plugin, `jar-with-dependencies`):
the application, our modules, the API classes and all the runtime dependencies in one jar.

## Running the application

```
java -jar app/target/workshop-cli.jar
```

It creates demo data and writes `instructors.json`, `students.json` and `teams.json` into the current folder.

## Running the tester

```
java -cp "lib/workshop-tester-2.0.jar;app/target/workshop-cli.jar" patmal.workshops.main.Main
```

(On Linux / macOS use `:` instead of `;`.)
