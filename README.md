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
