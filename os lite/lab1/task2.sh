#!/bin/bash

ans=""
while read line
do
	if [[ "$line" == "q" ]] 
	then
		echo $ans
		exit
	fi
	ans+="${line}"
done

