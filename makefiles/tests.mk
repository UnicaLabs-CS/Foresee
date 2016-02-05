# Tests Compiling
# ===============
all-tests: $(tests_path)/TripleTest.class

$(tests_path)/TripleTest.class: $(tests_path)/TripleTest.java $(datasets_path)/Triple.class
	javac -cp .:$(junit) $(tests_path)/TripleTest.java

# Tests Running
# =============
all-tests-run: all-tests run-TripleTest

run-TripleTest:
	java -cp .:$(junit):$(hamcrest) org.junit.runner.JUnitCore $(tests_pack).TripleTest
