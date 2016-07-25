#!/usr/bin/env bash
java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=6006 HbcStatus-1.0-SNAPSHOT-jar-with-dependencies.jar