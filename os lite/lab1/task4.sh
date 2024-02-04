#!/bin/bash

if [[ "$PWD" == "$HOME" ]] then
	echo $HOME
	exit 1
else
	echo "Can not exute this script in ${PWD}, please switch to ${HOME}."
	exit 0
fi	