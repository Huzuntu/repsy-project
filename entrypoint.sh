#!/bin/bash
set -e

service postgresql start

sleep 5

java -jar /app/app.jar