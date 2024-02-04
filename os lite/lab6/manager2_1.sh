#!/bin/bash

log_name=experiment2_1.log

> $log_name

for i in {1..20}; do
	echo "$i ->" >> $log_name
	./file_generator.sh

	for j in {1..2}; do
		{ \time -f "%e" ./sequential_starter2.sh $i ; } 2>> $log_name
	done
done