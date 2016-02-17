@echo off
@echo 1.Create ibatis-api-archetype
call mvn clean archetype:create-from-project

@echo 2.Install ibatis-api-archetype
set PROJECT_CONTEXT_PATH=%cd%
echo %PROJECT_CONTEXT_PATH%
cd %PROJECT_CONTEXT_PATH%/target/generated-sources/archetype
call mvn install
pause

cd %PROJECT_CONTEXT_PATH%
cd ../../
echo %cd%

@echo 3.Generate your's project by ibatis-api-archetype
call mvn -B archetype:generate -DarchetypeCatalog=local -DarchetypeGroupId=com.soho.common.platform -DarchetypeArtifactId=ibatis-api-archetype -DarchetypeVersion=1.0.0-Final -DgroupId=com.platform.common -DartifactId=demo-project -Dversion=0.1.0-SNAPSHOT
pause