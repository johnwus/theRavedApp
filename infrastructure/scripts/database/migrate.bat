@echo off 
set ENV=%1 
if "%ENV%"=="" set ENV=dev 
echo Applying helmfile for ENV=%ENV% to trigger Flyway migrations on app startup... 
set ENV=%ENV%
