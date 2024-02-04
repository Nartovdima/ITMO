#!/bin/bash

./task4

awk 'BEFORE {
	sum = 0;
	num = 1;
	ppid = 0;
}
{
	if ($5 != ppid) {
		print "Average_Running_Children_of_ParentID=", ppid, " is ", sum / num;
		sum = $8;
		num = 1;
	} else {
		sum += $8;
		num += 1;
	}
	ppid = $5;
	print $0;
}
END {
	print "Average_Running_Children_of_ParentID=", ppid, " is ", sum / num;
}' processes_art.info
