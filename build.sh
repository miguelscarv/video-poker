#!/bin/bash

cd src/
javac command/*.java player/*.java player/cards/*.java  main/*.java game/*.java game/match/*.java
jar cmf manifest.txt videopoker.jar command/* player/* main/* game/*
rm  command/*.class player/*.class player/cards/*.class  main/*.class game/*.class game/match/*.class
cd ..

echo "Done building jar named videopoker.jar..."
