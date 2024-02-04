#!/bin/bash

ps -A -o pid,command | awk '{print $1, " ", $2}' | grep -E "*/sbin/*" | awk '{print $1}'
