#!/bin/bash

ps u -U $UID > tmp_processes.info
wc -l tmp_processes.info | awk '{print $1}' > processes.info
awk '{print $2, " : ", $11}' tmp_processes.info >> processes.info
rm tmp_processes.info
