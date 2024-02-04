#!/bin/bash

for (( i=1; i <= $1; i++ )); do
	./matrix $i
done