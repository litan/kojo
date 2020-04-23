#!/usr/bin/env bash
java -Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -jar sbt-launch.0.13.18.jar "$@"
