#!/bin/bash

curr_date=$(date '+%Y-%m-%d')
destination_directory=~/restore
min_period=1000000
min_backup_date_dir="-1"

for line in $(ls ~ | grep -Eo "^Backup_[[:digit:]]{4}-[[:digit:]]{2}-[[:digit:]]{2}$"); do # Backup_YYYY-MM-DD
	backup_date=$(echo $line | awk -F "_" '{print $2}')
	let period=($(date -d "$curr_date" +%s) - $(date -d "$backup_date" +%s))/86400
	if [[ -d ~/$line ]] && [[ $min_period -gt $period ]] then
		min_period=$period
		min_backup_date_dir=$line
	fi
done

if [[ min_backup_date_dir == "-1" ]]; then
	echo "No backup directory found"
	exit 1
fi

source_directory=~/$min_backup_date_dir

for file in $(find $source_directory | cut -d '/' -f 5-); do
	if [[ -f $source_directory/$file ]] && [[ ! $file =~ .+"."[0-9]{4}-[0-9]{2}-[0-9]{2}_[0-9]{2}:[0-9]{2}:[0-9]{9}$ ]]; then 
		cp -a $source_directory/$file $destination_directory/$file
	elif [[ -d $source_directory/$file ]]; then
		mkdir $destination_directory/$file
	fi
done