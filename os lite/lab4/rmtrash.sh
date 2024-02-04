#!/bin/bash

trash_dir=~/.trash
trash_log=~/.trash.log

if [[ ! -d $trash_dir ]]; then
	mkdir $trash_dir
fi

if [[ -f "$1" ]]; then
	new_name="$1_$(date '+%m-%d-%Y_%H:%M:%N')"
	ln $1 $trash_dir/$new_name
	echo "$(realpath $1) / $new_name" >> $trash_log 
	rm $1
else
	echo "No such file"
	exit 1;
fi
