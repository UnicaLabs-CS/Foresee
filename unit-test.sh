#!/bin/bash

make all

make doc

echo 'Running ForeseeTest..'
./test.sh ForeseeTest

echo 'Running MovielensTest..'
./test.sh MovielensTest

make jar
