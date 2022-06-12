#!/bin/bash


if [ "$1" == "-d" ];
then 

	echo "Running it in DEBUG mode..."
	echo ""
	java -jar src/videopoker.jar $1 10000 ./card-file.txt ./cmd-file.txt

elif [ "$1" == "-s" ];
then 

	read -p "Bet amount and number of deals: " amount ndeals
	echo "Running it in SIMULATION mode..."
	echo""
	java -jar src/videopoker.jar $1 10000 $amount $ndeals

else
	echo "Commands do not match either options..."
fi

