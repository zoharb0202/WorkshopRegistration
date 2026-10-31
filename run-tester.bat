@echo off
cd /d "%~dp0"

java -cp "lib\workshop-tester.jar;lib\logic-1.0.jar;lib\model-1.0.jar;artifacts\workshops-model.jar;artifacts\workshops-logic.jar" patmal.workshops.main.Main
echo.
echo Exit code: %errorlevel%
pause
