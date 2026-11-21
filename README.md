# Workshop Registration - EX2 (Maven Evolution)

The workshop registration system of EX1, moved into a Maven multi-module project, extended with logging,
e-mail validation, JSON export and CSV import, and packaged as one runnable jar (`workshop-cli.jar`).

**Bonuses implemented:**
- **CSV import** (section 8.1) - `importStudentDataFromFile` with commons-csv; the application takes its students from a CSV file.
- **The tester runs as part of the build** (section 8.2) - in the `verify` phase; a failing tester fails the build.

EX1 (the plain IntelliJ project) is kept in the history under the tag `EX1-Vanilla`.

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
`-- app               jar: the runnable application, builds the uber jar (depends on logic-impl)
```

All the versions (Java, the API jars, the tester, the libraries, the plugins, our own modules) are declared once
in the root `pom.xml`: as properties, in `dependencyManagement` and in `pluginManagement`.
The child poms list their dependencies and plugins without a version.

Inside `logic-impl`, `WorkshopSystemImpl` is a thin class that delegates to one class per area:

| Package | Classes |
|---|---|
| `...logic.manager` | `InstructorManager`, `StudentManager`, `WorkshopManager`, `TeamManager`, `InputValidator` |
| `...logic.json` | `JsonExporter` and small classes shaped exactly like the exported JSON trees (no object points back to an ancestor) |
| `...logic.csv` | `CsvStudentImporter` |

Unknown ids are not treated as errors: `getX(id)` returns `null` and `getStudentsOfX(id)` returns an empty list.

## Libraries and plugins

| Library / plugin | Version | Module | Scope | Used for |
|---|---|---|---|---|
| Log4j 2 (`log4j-api`) | 2.26.1 | logic-impl | compile | logging in the code |
| Log4j 2 (`log4j-core`) | 2.26.1 | app | runtime | the logging implementation; configured by `app/src/main/resources/log4j2.xml` |
| commons-validator | 1.11.0 | logic-impl | compile | e-mail validation |
| Gson | 2.15.0 | logic-impl | compile | JSON export |
| commons-csv | 1.14.1 | logic-impl | compile | CSV import (bonus) |
| workshop-tester | 2.0 | app | test | run by the build (bonus); never packed into the uber jar |
| maven-assembly-plugin | 3.8.0 | app | - | builds `workshop-cli.jar` (`jar-with-dependencies`) in the `package` phase |
| exec-maven-plugin | 3.7.0 | app | - | runs the tester in the `verify` phase (bonus) |

Log4j is imported into `dependencyManagement` as a BOM (`log4j-bom`), so one property controls the versions of all its artifacts.

## Build

From the root folder:

```
mvn clean install
```

The build compiles all the modules in the right order, creates the uber jar `app/target/workshop-cli.jar`
(the application, our modules, the API classes and all the runtime dependencies in one jar),
and in the `verify` phase runs the tester against the implementation. The build fails if the tester finds a problem.

## Running the application

```
java -jar app/target/workshop-cli.jar
java -jar app/target/workshop-cli.jar app/src/main/resources/students.csv
```

The application creates instructors, workshops and teams, takes the students from the CSV file given as an argument
(without an argument it uses the `students.csv` inside the jar), and writes `instructors.json`, `students.json`
and `teams.json` into the current folder.

## Running the tester by hand

```
java -cp "lib/workshop-tester-2.0.jar;app/target/workshop-cli.jar" patmal.workshops.main.Main
```

(On Linux / macOS use `:` instead of `;`.)
