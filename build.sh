#!/bin/bash

cd src/
javac command/*.java player/*.java player/cards/*.java  main/*.java game/*.java game/match/*.java
jar cmf manifest.txt videopoker.jar command/* player/* main/* game/*
cd ..

echo ""
echo "Done building jar named videopoker.jar..."
echo ""

echo "Run it in debug or simulation mode? [d/s]"
read mode

if [[ "$mode" == "d" ]];
then 
	java -jar src/videopoker.jar -d 10000 ./card-file.txt ./cmd-file.txt
elif [[ "$mode" == "s" ]];
then 
	java -jar src/videopoker.jar -s 10000 5 10000
else
	echo "Command does not match either options..."
fi

