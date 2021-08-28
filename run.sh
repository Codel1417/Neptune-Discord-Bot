#! /bin/sh
jstatd \
    -p 1233 \
    -n Neptune \
    -J-Djava.rmi.server.logCalls=true \
    -J-Djava.security.policy=tools.policy &
java \
    -Dcom.sun.management.jmxremote.ssl=false \
    -Dcom.sun.management.jmxremote.port=1232 \
    -Dcom.sun.management.jmxremote.authenticate=false \
    -XX:+PrintGCDetails \
    -XX:+PrintGCDateStamps \
    -jar Neptune_Discord_Bot-1.0-SNAPSHOT.jar