@echo off 
rem Simple rollback placeholder: re-apply previous helmfile state or tagged release 
echo To implement: helmfile -f infrastructure\helm\helmfile.yaml apply --skip-diff-on-install --args "--history-max 20"
