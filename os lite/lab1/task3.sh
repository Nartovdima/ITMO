#!/bin/bash

echo "Choose one option from menu:"

select option in nano vi links exit; do
	case $option in
		"nano")
			nano
			;;
		"vi")
			vi
			;;
		"links")
			links
			;;
		"exit")
			exit
			;;
		*)
			echo "Please, choose number from 1 to 4."
	esac	
done