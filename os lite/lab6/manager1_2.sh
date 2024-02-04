#!/bin/bash

log_name=experiment1_2.log

> $log_name

for i in {1..20}; do
	echo "$i ->" >> $log_name

	for j in {1..2}; do
		{ \time -f "%e" ./parallel_starter1.sh $i ; } 2>> $log_name
	done
done