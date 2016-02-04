# Tests Compiling
# ===============
all-tests: triple-test

triple-test: $(tests_path)/TripleTest.java triple
	javac -cp .:$(junit) $(tests_path)/TripleTest.java

# Tests Running
# =============
all-tests-run: triple-test

triple-test-run:
	java -cp .:$(junit):$(hamcrest) org.junit.runner.JUnitCore $(tests_pack).TripleTest
