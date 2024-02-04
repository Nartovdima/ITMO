#!/bin/bash
shopt -s extglob

IFS=$'\n'
trash_dir=~/.trash
trash_log=~/.trash.log
service_dir=.untrash_service_files
new_log=$service_dir/.trash.log

mkdir $service_dir && touch $service_dir/new_log

trim () { #string
	string=$1
	string=${string##*( )}
	string=${string%%*( )}
	echo $string
}

cnt=0;

for line in $(cat $trash_log); do
	filepath=$(echo $line | awk -F '/' 'BEGIN {OFS = "/";}NF{NF--};1')
	filepath=$(trim $filepath)

	filedir=$(echo $filepath | awk -F '/' 'BEGIN {OFS = "/";}NF{NF--};1')
	filedir=$(trim $filedir)

	old_name=$(echo $filepath | awk -F '/' '{print $NF}')
	old_name=$(trim $old_name)

	curr_name=$(echo $line | awk -F '/' '{print $NF}')
	curr_name=$(trim $curr_name)

	#echo "$filepath, $filedir, $old_name, $curr_name"

	if [[ $1 == $old_name ]]; then 
		echo "Do you want restore $filepath? {Yes for accept, No otherwise}"
		read response
		while [ $response != "Yes" ] && [ $response != "No" ]; do
			echo "Unexpected answer, please try again"
			read response
		done
		if [[ $response == "Yes" ]]; then 
			#restore
			new_name=$old_name
			if [[ ! -d $filedir ]]; then
				echo "Origin directory has been deleted, restored directory set to ${HOME}"
				filedir=~
			fi
			while [ -f $filedir/$new_name ]; do
				echo "In directiry $filedir file with name $old_name already exists, please enter new name"
				read new_name
			done
			ln $trash_dir/$curr_name $filedir/$new_name
			rm $trash_dir/$curr_name
			echo "$old_name successfuly restored with full name: $filedir/$new_name"
			# delete record from log!!!
		else
			echo $line >> $new_log
			echo "Skipped $filepath"
		fi
		let cnt++
	else
		echo $line >> $new_log
	fi
done 

if [[ cnt -eq 0 ]]; then
	echo "No such file"
fi

ln -f $new_log $trash_log
rm -rf $service_dir

shopt -u extglob
