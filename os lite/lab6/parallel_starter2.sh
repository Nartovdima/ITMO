#!/bin/bash

for (( i=1; i <= $1; i++ )); do
	./file_processor ~/lab6/staff/file$i &
done
wait
