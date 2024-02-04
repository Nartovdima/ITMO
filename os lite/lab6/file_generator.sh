#!/bin/bash

files_dir=staff
N=12500000
for i in {1..20}; do
	file_name=$files_dir/file$i;
	> $file_name
	
	./single_file_generator $N $file_name
done

