#!/bin/bash

ans=$1
if [[ ans -le $2 ]]
	then ans=$2
fi

if [[ ans -le $3 ]]
	then ans=$3
fi
echo $ans
