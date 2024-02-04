#!/bin/bash

while true; do
	read LINE
	case $LINE in
	cat)
		kill -USR1 $(cat .pid)
		;;
	dog)
		kill -USR2 $(cat .pid)
		;;
	TERM)
		kill -SIGTERM $(cat .pid)
		exit
		;;
	*)
		echo "unsupported symbol"
		;;
	esac
done