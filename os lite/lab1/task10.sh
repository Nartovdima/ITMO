#!/bin/bash

man bash | grep -o "[a-zA-Z]\{4,\}" | sort -f | uniq -c | sort -nr | head -n3 | awk '{print $2}'
