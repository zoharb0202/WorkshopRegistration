@echo off
cd /d "%~dp0"

if exist build rmdir /s /q build
mkdir build\model
mkdir build\logic

echo Compiling the model module...
javac --release 25 -d build\model -cp lib\model-1.0.jar model\src\patmal\workshops\model\*.java
if errorlevel 1 goto failed

echo Compiling the logic module...
javac --release 25 -d build\logic -cp "lib\model-1.0.jar;lib\logic-1.0.jar;build\model" logic\src\patmal\workshops\logic\*.java
if errorlevel 1 goto failed

if not exist artifacts mkdir artifacts
jar cf artifacts\workshops-model.jar -C build\model .
if errorlevel 1 goto failed
jar cf artifacts\workshops-logic.jar -C build\logic .
if errorlevel 1 goto failed

rmdir /s /q build
echo.
echo Build finished - the jars are in the artifacts folder
goto end

:failed
echo.
echo Build failed

:end
pause
