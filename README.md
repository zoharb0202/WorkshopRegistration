# Workshop Registration - EX1 (Vanilla)

Implementation of the workshop registration system API (`model-1.0.jar`, `logic-1.0.jar`) in plain Java 25.

## Modules
- `model` - the model objects: `InstructorImpl`, `StudentImpl`, `WorkshopImpl`, `TeamImpl` (package `patmal.workshops.model`)
- `logic` - `WorkshopSystemImpl`, the implementation of `WorkshopSystem` (package `patmal.workshops.logic`)

## Artifacts
The `artifacts` folder holds the implementation jars:
- `workshops-model.jar`
- `workshops-logic.jar`

## Build and test
- `build.bat` - compiles both modules and creates the jars in `artifacts`
- `run-tester.bat` - runs the tester (`lib/workshop-tester.jar`) against the jars in `artifacts`

Unknown ids are not treated as errors: `getX(id)` returns `null` and `getStudentsOfX(id)` returns an empty list.
