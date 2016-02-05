# Project name
proj = jpc

# Main path of the project
path = it/unica/$(proj)
pack = it.unica.$(proj)

# Path for the tests
tests_path = $(path)/tests
tests_pack = $(pack).tests

# Path(s) to clean
clean_path = $(path)/predictions \
			 $(path)/tests

# Path to makefiles (.mk) to be included
mk_path = makefiles

# Path to jUnit jar file
libs_path = libs
junit = $(libs_path)/junit-4.jar
hamcrest = $(libs_path)/hamcrest.jar

# Generic
# =======
all:
	echo "Work in progress.."

clean:
	for p in $(clean_path); do rm -v $$p/*.class; done;

doc:
	javadoc -version -author -d docs -subpackages it

# Libraries
# =========
test-libs:
	cd $(libs_path); make test-libs

# Tests makefile
include $(mk_path)/tests.mk

# Predictions
# ===========
prediction: $(path)/predictions/Prediction.java
	javac $(path)/predictions/Prediction.java

# Datasets
# ========
triple: $(path)/datasets/Triple.java
	javac $(path)/datasets/Triple.java
	
dataset: $(path)/datasets/Dataset.java triple
	javac $(path)/datasets/Dataset.java
