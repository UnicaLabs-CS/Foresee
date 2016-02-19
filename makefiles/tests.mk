# Tests Compiling
# ===============

# Libs
# ====
junit = $(libs_path)/junit-4.jar
hamcrest = $(libs_path)/hamcrest.jar
system_rules = $(libs_path)/system-rules.jar

# Running commands
# ================
run_junit_test = java -cp .:$(junit):$(hamcrest) org.junit.runner.JUnitCore

all-tests: test-libs \
           all-tests-datasets \
           all-tests-interpreters

# Datasets
# ========
all-tests-datasets: $(tests_path)/TripleTest.class \
                    $(tests_path)/MovielensTest.class

$(tests_path)/TripleTest.class: $(tests_path)/TripleTest.java $(datasets_path)/Triple.class
	javac -cp .:$(junit) $(tests_path)/TripleTest.java

$(tests_path)/MovielensTest.class: $(tests_path)/MovielensTest.java datasets
	javac -cp .:$(junit) $(tests_path)/MovielensTest.java
	
# Interpreters
# ============
all-tests-interpreters: $(tests_path)/ForeseeTest.class \
                        $(tests_path)/InterpreterTest.class

$(tests_path)/ForeseeTest.class: interpreters
	javac -cp .:$(junit) $(tests_path)/ForeseeTest.java

$(tests_path)/InterpreterTest.class: interpreters
	javac -cp .:$(junit) $(tests_path)/InterpreterTest.java

# Tests Running
# =============
run-all-tests: run-all-tests-datasets

# Datasets
# ========
run-all-tests-datasets: all-tests-datasets \
                        run-TripleTest \
                        run-MovielensTest

run-TripleTest: $(tests_path)/TripleTest.class
	$(run_junit_test) $(tests_pack).TripleTest

run-MovielensTest: $(tests_path)/MovielensTest.class
	$(run_junit_test) $(tests_pack).MovielensTest

# Interpreters
# ============
run-all-tests-interpreters: run-ForeseeTest \
                            run-InterpreterTest

run-ForeseeTest: $(tests_path)/ForeseeTest.class
	java -cp .:$(junit):$(hamcrest) org.junit.runner.JUnitCore $(tests_pack).ForeseeTest

run-InterpreterTest: $(tests_path)/InterpreterTest.class
	$(run_junit_test) $(tests_pack).InterpreterTest

