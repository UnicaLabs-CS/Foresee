#!/bin/bash

# Test the given class - Much faster to type than the following!
function run_test()
{
  java -Xmx10g -cp .:test-libs/junit-4.jar:test-libs/hamcrest.jar:libs/commons-math3.jar org.junit.runner.JUnitCore it.unica.foresee.tests.$1$2
}

# List the available tests
function list_test()
{
  find it/unica/foresee/tests/ -name *Test.class -exec basename \{} Test.class \;
}

# List the available simulations
function list_sim()
{
  find it/unica/foresee/tests/ -name *Simulation.class -exec basename \{} Simulation.class \;
}


# Main
if [ "$1" == "sim" ]; then
    if [ "$2" == "-l" ] || [ -z "$2" ]; then
		# List the available classes
		echo "Available simulations:";
		list_sim
	elif [ "$2" == "-r" ]; then
		# Remove to recompile
		rm -v it/unica/foresee/tests/$2Simulation.class
	elif [ "$2" == "all" ] || [ "$2" == "-a" ]; then
		for test in `list_sim`; do
			run_test $test "Simulation";
			if [ $? -ne 0 ]; then break; fi; 
		done;
	else 
		run_test $2 "Simulation"
	fi
elif [ "$1" == "-l" ] || [ -z "$1" ]; then
    # List the available classes
    echo "Available tests:";
	list_test
elif [ "$1" == "-r" ]; then
    # Remove to recompile
    rm -v it/unica/foresee/tests/$2Test.class
elif [ "$1" == "all" ] || [ "$1" == "-a" ]; then
	for test in `list_test`; do
		run_test $test "Test";
		if [ $? -ne 0 ]; then exit $?; fi; 
	done;
else
run_test $1 "Test"
fi
