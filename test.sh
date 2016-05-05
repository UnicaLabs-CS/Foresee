#!/bin/bash

if [ $1 == "-l" ]; then
    # List the available classes
	ls -l it/unica/foresee/tests/*.class
elif [ $1 == "-r" ]; then
    # Remove to recompile
    rm -v it/unica/foresee/tests/$2.class
else
# Test the given class - Much faster to type than the following!
java -cp .:test-libs/junit-4.jar:test-libs/hamcrest.jar:libs/commons-math3.jar org.junit.runner.JUnitCore it.unica.foresee.tests.$1
fi
