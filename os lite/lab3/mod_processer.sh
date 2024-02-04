#!/bin/bash

echo $$ > .pid

MODE=meow # options: cat, dog, stop

usr1()
{
	MODE=meow
}

usr2()
{
	MODE=bark
}

sigterm()
{
	MODE=stop
}

trap 'usr1' USR1
trap 'usr2' USR2
trap 'sigterm' SIGTERM

while true; do
	case $MODE in
	meow | bark)
		echo $MODE
		;;
	stop)
		echo "Stopped by SIGTERM"
		exit
		;;
	esac
	sleep 1
done