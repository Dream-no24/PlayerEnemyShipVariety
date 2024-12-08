@echo off
if not exist "build/libs/PlayerEnemyShipVariety.jar" (
    gradlew clean build
    if not exist "build/libs/PlayerEnemyShipVariety.jar" (
        pause
        exit /b 1
    )
)
java -jar build/libs/PlayerEnemyShipVariety.jar
pause
