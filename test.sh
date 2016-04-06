#!/bin/bash

# Much faster to type than the following!
java -cp .:test-libs/junit-4.jar:test-libs/hamcrest.jar:libs/commons-math3.jar org.junit.runner.JUnitCore it.unica.foresee.tests.$1
