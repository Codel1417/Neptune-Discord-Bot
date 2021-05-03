#! /bin/sh
jstatd -p 1233 -n Neptune -J-Djava.security.policy=all.policy -J-Djava.rmi.server.logCalls=true &
java -jar Neptune_Discord_Bot-1.0-SNAPSHOT.jar