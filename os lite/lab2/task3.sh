#!/bin/bash

ps h -o pid -A --sort=-start_time | head -n1
