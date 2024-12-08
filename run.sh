if [ ! -f "build/libs/PlayerEnemyShipVariety.jar" ]; then
    ./gradlew clean build
    if [ ! -f "build/libs/PlayerEnemyShipVariety.jar" ]; then
        exit 1
    fi
fi
# JAR 파일 실행
java -jar build/libs/PlayerEnemyShipVariety.jar