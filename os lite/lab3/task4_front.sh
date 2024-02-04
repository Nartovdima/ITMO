#!/bin/bash

cpulimit -l 10 ./task4_main &
./task4_main &
./task4_main &
kill $!