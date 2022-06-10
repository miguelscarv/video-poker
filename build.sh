#!/bin/bash

cd src/
javac command/*.java player/*.java player/cards/*.java  main/*.java game/*.java game/match/*.java
jar cmf manifest.txt videopoker.jar command/* player/* main/* game/*
rm  command/*.class player/*.class player/cards/*.class  main/*.class game/*.class game/match/*.class
cd ..

echo ""
echo "Done building jar named videopoker.jar..."
echo ""

read -p "Run it in debug or simulation mode? [d/s] " mode

if [[ "$mode" == "d" ]];
then 
	java -jar src/videopoker.jar -d 10000 ./card-file.txt ./cmd-file.txt
elif [[ "$mode" == "s" ]];
then 
	read -p "How many plays: " nplays
	java -jar src/videopoker.jar -s 10000 5 $nplays
else
	echo "Command does not match either options..."
fi

