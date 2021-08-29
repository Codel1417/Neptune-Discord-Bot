#! /bin/sh
cd /ejstatd
mvn exec:java -Djava.rmi.server.hostname=$HOST_HOSTNAME -Dexec.args="-pr2222 -ph2223 -pv2224" &

cd /nep
./gradlew run