#!/bin/bash

find /var/log -name "*.log" -exec cat {} \; | wc -l