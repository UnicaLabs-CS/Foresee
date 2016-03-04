# Project name
proj = foresee

# Main path of the project
path = it/unica/$(proj)
pack = it.unica.$(proj)

paths_list = $(path) \
             $(path)/utils \
             $(path)/predictions \
             $(datasets_path) \
             $(interpreters_path) \
             

# Path for the tests
tests_path = $(path)/tests
tests_pack = $(pack).tests

# Path for the dataset
datasets_path = $(path)/datasets

# Path for the parser
interpreters_path = $(path)/interpreters

# Path(s) to clean
clean_path = $(tests_path) \
             $(paths_list)

# Path of the makefiles (.mk) to be included
mk_path = makefiles

# Path of jUnit jar file
libs_path = libs

# Generic
# =======
all: main

clean:
	for p in $(clean_path); do rm -v $$p/*.class; done; rm $(proj).jar

doc:
	javadoc -version -author -d docs -subpackages it

jar: main
	jar -cvfm $(proj).jar Manifest.txt `for p in $(paths_list); do echo $$p/*.class; done;`

# Libraries
# =========
test-libs:
	cd $(libs_path); make test-libs

# Tests makefile
include $(mk_path)/tests.mk

# Main
# ====
main: $(path)/Foresee.class

$(path)/Foresee.class: utils interpreters $(path)/Settings.class
	javac $(path)/Foresee.java

$(path)/Settings.class:
	javac $(path)/Settings.java

# Utils
# =====
utils: $(path)/utils/Tools.class

$(path)/utils/Tools.class:
	javac $(path)/utils/Tools.java

# Interpreters
# ============
interpreters: $(interpreters_path)/Env.class \
         $(interpreters_path)/Semantic.class \
         $(interpreters_path)/CommandList.class \
         $(interpreters_path)/Interpreter.class \
         $(interpreters_path)/FSCommandList.class \
         $(interpreters_path)/ARTCommandList.class

$(interpreters_path)/Env.class:
	javac $(interpreters_path)/Env.java

$(interpreters_path)/Semantic.class: $(interpreters_path)/Semantic.java
	javac $(interpreters_path)/Semantic.java

$(interpreters_path)/Interpreter.class: datasets \
                                        utils \
                                        $(interpreters_path)/CommandList.class
	javac $(interpreters_path)/Interpreter.java

$(interpreters_path)/CommandList.class: $(interpreters_path)/Semantic.class
	javac $(interpreters_path)/CommandList.java

$(interpreters_path)/ARTCommandList.class: $(interpreters_path)/FSCommandList.class
	javac $(interpreters_path)/ARTCommandList.java

$(interpreters_path)/FSCommandList.class: datasets \
                                           utils \
                                           $(interpreters_path)/Interpreter.class
	javac $(interpreters_path)/FSCommandList.java

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

