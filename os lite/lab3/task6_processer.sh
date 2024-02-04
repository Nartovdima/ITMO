#!/bin/bash

echo $$ > .pid

res=1
MODE=0 # 0 - add, 1 - mul, 2 - stop 

usr1()
{
	MODE=0
}

usr2()
{
	MODE=1
}

sigterm()
{
	MODE=2
}

trap 'usr1' USR1
trap 'usr2' USR2
trap 'sigterm' SIGTERM

while true; do
	case $MODE in
	0)
		let res=$res+2
		echo $res
		;;
	1)
		let res=$res\*2
		echo $res
		;;
	2)
		echo "Stopped by SIGTERM"
		exit
		;;
	esac
	sleep 1
done