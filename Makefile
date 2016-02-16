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

# Path for the parser
parsers_path = $(path)/parsers

# Path(s) to clean
clean_path = $(path) \
             $(path)/utils \
             $(path)/predictions \
             $(datasets_path) \
             $(parsers_path) \
             $(tests_path)

# Path of the makefiles (.mk) to be included
mk_path = makefiles

# Path of jUnit jar file
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

# Main
# ====
main: $(path)/JPC.class

$(path)/JPC.class: utils
	javac $(path)/JPC.java

# Utils
# =====
utils: $(path)/utils/Tools.class

$(path)/utils/Tools.class:
	javac $(path)/utils/Tools.java

# Parsers
# =======
parsers: $(parsers_path)/Semantic.class \
         $(parsers_path)/JPCParser.class \
         $(parsers_path)/ARTParser.class

$(parsers_path)/Semantic.class: $(parsers_path)/Semantic.java
	javac $(parsers_path)/Semantic.java

$(parsers_path)/JPCParser.class: datasets utils
	javac $(parsers_path)/JPCParser.java

$(parsers_path)/ARTParser.class: datasets utils $(parsers_path)/JPCParser.class
	javac $(parsers_path)/ARTParser.java

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
	javac $(datasets_path)/Dataset.java -Xlint

$(datasets_path)/Movielens.class: $(datasets_path)/Movielens.java $(datasets_path)/Dataset.class
	javac $(datasets_path)/Movielens.java

