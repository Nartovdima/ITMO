#!/bin/bash


tmp_file_name="${PWD}/tmp_processes_art.info"
file_name="${PWD}/processes_art.info"
> $tmp_file_name

for process in /proc/*; do
	if [[ -d $process ]]; then
		cd $process
		ppid=-1
		pid=-1
		if [[ -f ./status ]]; then
			ppid=$(cat status | awk '{ if ($1 == "PPid:") print $2 }')
			pid=$(cat status | awk '{ if ($1 == "Pid:") print $2 }')
		fi

		sum_exec_runtime=0
		nr_switches=1
		if [[ -f ./sched ]]; then
			sum_exec_runtime=$(cat sched | awk '{if ($1 == "se.sum_exec_runtime") print $3}')
			nr_switches=$(cat sched | awk '{if ($1 == "nr_switches") print $3}')
		fi
		cd ..
		art=$(echo "$sum_exec_runtime / $nr_switches" | bc -l)
		if [[ $pid -ne -1 ]]; then
			echo "$pid $ppid $art" >> $tmp_file_name
		fi
	fi
done

sort -nk2 $tmp_file_name | awk '{print "ProcessID=",$1, " : ", "Parent_ProcessID=",$2, " : ", "Average_Running_Time=",$3}' > $file_name
rm $tmp_file_name
