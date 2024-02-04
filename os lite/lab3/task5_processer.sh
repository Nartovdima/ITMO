#!/bin/bash

shopt -s extglob

function shutdown() {
	killall tail
	killall task5_generator
	exit
}

mode=0 # 0 - add 1 - mul
res=1

(tail -f pipe) |
while true; do
	read LINE
	case $LINE in
		\+)
			echo "set addition mod"
			mode=0
			;;
		\*)
			echo "set multiplication mod"
			mode=1
			;;
		QUIT)
			echo "exit"
			shutdown
			;;
		*)
			if [[ "$LINE" =~ [0-9]+ ]]; then
				if [[ $mode -eq 0 ]]; then
					let res=$res+$LINE
				else
					let res=$res\*$LINE
				fi
				echo $res
			else
				echo "unexpected input"
				shutdown
			fi
			;;
	esac
done