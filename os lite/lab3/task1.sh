#!/bin/bash

mkdir ~/test && 
{
	echo "catalog test was created successfully" >> ~/report; 
	> ~/test/$(date '+%m-%d-%Y_%H:%M')_script_start
};
ping www.net_nikogo.ru || echo "$(date '+%m-%d-%Y_%H:%M'), can not ping host" >> ~/report
