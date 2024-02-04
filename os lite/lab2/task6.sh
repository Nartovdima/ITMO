#!/bin/bash

max_memory_usage=0
pid=-1
top_result=$(top -b -n1 -o VIRT | sed 1,7d | head -n1 | awk '{print $1}')
for process in /proc/*; do
	if [[ -f $process/statm ]] && [[ -f $process/status ]] ; then
		current_pid_memory_usage=$(awk '{print $1}' $process/statm)
		if [[ $max_memory_usage -lt $current_pid_memory_usage ]]; then
			max_memory_usage=$current_pid_memory_usage
			pid=$(awk '{ if ($1 == "Pid:") print $2 }' $process/status)
		fi
	fi
done
echo "top result = ${top_result}"
echo "/proc result = ${pid}" 
