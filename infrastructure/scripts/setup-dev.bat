@echo off 
set ENV=%1 
if "%ENV%"=="" set ENV=dev 
echo Adding Helm repos... 
helm repo add bitnami https://charts.bitnami.com/bitnami >nul 
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts >nul 
helm repo add elastic https://helm.elastic.co >nul 
helm repo update >nul 
echo Applying helmfile for ENV=%ENV%... 
