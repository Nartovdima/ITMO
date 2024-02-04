#!/bin/bash

curr_date=$(date '+%Y-%m-%d')
min_period=1000000
backup_directory=~/Backup_$curr_date
directory_for_backup=~/source
log_file=~/backup_report

min_backup_date_dir=$curr_date

for line in $(ls ~ | grep -Eo "^Backup_[[:digit:]]{4}-[[:digit:]]{2}-[[:digit:]]{2}$"); do # Backup_YYYY-MM-DD
	backup_date=$(echo $line | awk -F "_" '{print $2}')
	let period=($(date -d "$curr_date" +%s) - $(date -d "$backup_date" +%s))/86400
	if [[ -d ~/$line ]] && [[ $min_period -gt $period ]] then
		min_period=$period
		min_backup_date_dir=$line
	fi
done

completely_new_backup=false
if [[ $min_period -lt 7 ]]; then
	backup_directory=~/$min_backup_date_dir
else 
	mkdir $backup_directory
	completely_new_backup=true
fi


if [[ $completely_new_backup == true ]]; then
	echo "New Backup directory created: $backup_directory." >> $log_file

	for file in $(find $directory_for_backup | cut -d '/' -f 5-); do
		# echo $file
		cp -a $directory_for_backup/$file $backup_directory/$file
		echo "$file copied to $backup_directory/$file" >> $log_file
	done
else
	echo "Starting new backup at $curr_date" >> $log_file
	for file in $(find $directory_for_backup | cut -d '/' -f 5-); do
		if [[ ! -f $backup_directory/$file ]] && [[ ! -d $backup_directory/$file ]]; then
			cp -a $directory_for_backup/$file $backup_directory/$file
			echo "$file copied to $backup_directory/$file" >> $log_file
		elif [[ ! -d $directory_for_backup/$file ]]; then
			backuped_file_size=$(wc -c $backup_directory/$file | awk '{print $1}')
			current_file_size=$(wc -c $directory_for_backup/$file | awk '{print $1}')

			if [[ $backuped_file_size -ne $current_file_size ]]; then
				date_with_ns=$(date '+%Y-%m-%d_%H:%M:%N')
				mv $backup_directory/$file $backup_directory/$file.$date_with_ns
				cp -a $directory_for_backup/$file $backup_directory/$file
				echo "Previous version renamed: $file -> $file.$date_with_ns" >> $log_file
				echo "New version added: $directory_for_backup/$file -> $backup_directory/$file" >> $log_file
			fi
		fi
	done
fi
