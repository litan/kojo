#!/usr/bin/env bash
java -Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -jar sbt-launch.1.5.5.jar "$@"
