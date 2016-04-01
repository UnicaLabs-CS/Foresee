#!/bin/bash

# Much faster to type than the following!
java -cp .:test-libs/junit-4.jar:test-libs/hamcrest.jar org.junit.runner.JUnitCore it.unica.foresee.tests.$1
