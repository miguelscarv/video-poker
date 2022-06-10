#!/bin/bash

cd src/
javac command/*.java player/*.java player/cards/*.java  main/*.java game/*.java game/match/*.java
jar cmf manifest.txt videopoker.jar command/* player/* main/* game/*
rm  command/*.class player/*.class player/cards/*.class  main/*.class game/*.class game/match/*.class
cd ..

echo ""
echo "Done building jar named videopoker.jar..."

if [[ "$1" == "-d" ]];
then 
	echo "Running it in DEBUG mode..."
	echo ""
	java -jar src/videopoker.jar $1 $2 ./card-file.txt ./cmd-file.txt
elif [[ "$1" == "-s" ]];
then 
	echo "Running it in SIMULATION mode"
	echo""
	java -jar src/videopoker.jar $1 $2 $3 $4
else
	echo "Commands do not match either options..."
fi

