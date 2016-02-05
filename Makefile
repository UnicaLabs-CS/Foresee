# Project name
proj = jpc

# Main path of the project
path = it/unica/$(proj)
pack = it.unica.$(proj)

# Path for the tests
tests_path = $(path)/tests
tests_pack = $(pack).tests

# Path for the dataset
datasets_path = $(path)/datasets

# Path(s) to clean
clean_path = $(path)/predictions \
             $(datasets_path)
             $(test_path)

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
datasets: $(datasets_path)/Triple.class \
          $(datasets_path)/Dataset.class \
          $(datasets_path)/Movielens.class

$(datasets_path)/Triple.class: $(path)/datasets/Triple.java
	javac $(datasets_path)/Triple.java
	
$(datasets_path)/Dataset.class: $(path)/datasets/Dataset.java $(datasets_path)/Triple.class
	javac $(datasets_path)/Dataset.java

$(datasets_path)/Movielens.class: $(datasets_path)/Movielens.java $(datasets_path)/Dataset.class
	javac $(datasets_path)/Movielens.java

