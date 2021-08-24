#! /bin/sh
jstatd \
    -p 1233 \
    -n Neptune \
    -J-Djava.security.policy=all.policy \
    -J-Djava.rmi.server.logCalls=true &
java \
    -Dcom.sun.management.jmxremote.ssl=false \
    -Dcom.sun.management.jmxremote.port=1232 \
    -Dcom.sun.management.jmxremote.authenticate=false \
    -XX:+PrintGCDetails \
    -XX:+PrintGCDateStamps \
    -jar Neptune_Discord_Bot-1.0-SNAPSHOT.jar