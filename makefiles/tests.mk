# Tests Compiling
# ===============
all-tests: all-tests-datasets

all-tests-datasets: $(tests_path)/TripleTest.class \
                    $(tests_path)/MovielensTest.class

$(tests_path)/TripleTest.class: $(tests_path)/TripleTest.java $(datasets_path)/Triple.class
	javac -cp .:$(junit) $(tests_path)/TripleTest.java

$(tests_path)/MovielensTest.class: $(tests_path)/MovielensTest.java datasets
	javac -cp .:$(junit) $(tests_path)/MovielensTest.java

# Tests Running
# =============
run-all-tests: run-all-tests-datasets

run-all-tests-datasets: all-tests-datasets \
                        run-TripleTest \
                        run-MovielensTest

run-TripleTest: $(tests_path)/TripleTest.class
	java -cp .:$(junit):$(hamcrest) org.junit.runner.JUnitCore $(tests_pack).TripleTest

run-MovielensTest: $(tests_path)/MovielensTest.class
	java -cp .:$(junit):$(hamcrest) org.junit.runner.JUnitCore $(tests_pack).MovielensTest
