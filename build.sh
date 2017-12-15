#!/bin/bash
echo 'Building with Maven...'
mvn clean package -Dmaven.test.skip=true
echo 'Finished!'
