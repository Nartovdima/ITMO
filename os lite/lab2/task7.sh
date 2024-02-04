#!/bin/bash

for process in /proc/*; do
	if [[ -f $process/status ]] && [[ -f $process/io ]]; then
		pid=$(awk '{ if ($1 == "Pid:") print $2 }' $process/status)
		bytes_read=$(awk '{ if ($1 == "read_bytes:") print $2 }' $process/io)
		echo "$pid $bytes_read"
	fi
done > rom_process_usage.info

sleep 1m

while read line; do
	pid=$(echo $line | awk '{print $1}')
	previous_byte_usage=$(echo $line | awk '{print $2}')
	new_bytes_read=0
	full_name=""
	if [[ -f /proc/$pid/io ]]; then
		new_bytes_read=$(awk '{ if ($1 == "read_bytes:") print $2 }' /proc/$pid/io)
		full_name=$(ps -h -o cmd -p $pid)
		let del=$new_bytes_read-$previous_byte_usage
		echo "$del $pid $full_name"
	fi
done < rom_process_usage.info | sort -rnk 1 | head -n3 | awk '{print $2, ":", $3, ":", $1}' 
rm rom_process_usage.info
