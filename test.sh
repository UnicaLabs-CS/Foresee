#!/bin/bash

# Test the given class - Much faster to type than the following!
function run_test()
{
  java -cp .:test-libs/junit-4.jar:test-libs/hamcrest.jar:libs/commons-math3.jar org.junit.runner.JUnitCore it.unica.foresee.tests.$1Test
}

# List the available tests
function list_test()
{
  find it/unica/foresee/tests/ -name *Test.class -exec basename \{} Test.class \;
}

# Main
if [ "$1" == "-l" ] || [ -z "$1" ]; then
    # List the available classes
    echo "Available tests:";
	list_test
elif [ "$1" == "-r" ]; then
    # Remove to recompile
    rm -v it/unica/foresee/tests/$2Test.class
elif [ "$1" == "all" ] || [ "$1" == "-a" ]; then
	for test in `list_test`; do
		run_test $test;
		if [ $? -ne 0 ]; then break; fi; 
	done;
else
run_test $1
fi
