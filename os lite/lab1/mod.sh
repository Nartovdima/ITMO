#!/bin/bash

awk 'BEFORE {
		sum = 0; 
		num_of_students = 0; 
	} 
	{
		if ($4 == "M3137") {
			sum += $5;
			num_of_students += 1;
		}
	} 
	END { 
		if (num_of_students != 0) {
			print sum / num_of_students;
		} else {
			print "No students exist("	
		}
	}' student_list