#!/bin/sh
JAVA_HOME=/tools/jdk1.7.0_40


$JAVA_HOME/bin/java -cp mqtt-listener-1.0.jar:libs/* consumer.MessageSubscriber /share/vsb/MQTTListener/configuration.properties
