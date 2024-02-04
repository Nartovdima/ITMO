#!/bin/bash

awk '
{
	if ($3 == "(WW)") {
		$3 = "Warning:"; 
		print
	}
} 
' /var/log/anaconda/X.log > full.log

awk '
{
	if ($3 =="(II)") {
		$3 = "Information:"; 
		print
	}
}' /var/log/anaconda/X.log >> full.log 
cat full.log