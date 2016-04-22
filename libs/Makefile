# Prepare libraries
# =================

all: log4j-api.jar log4j-core.jar commons-math3.jar commons-math3-javadoc.jar

# Symlink to current version of log4j-api
log4j-api.jar:
	ln -s log4j-api* log4j-api.jar

# Symlink to current version of log4j-core
log4j-core.jar:
	ln -s log4j-core* log4j-core.jar
	
# Symlink to current version of commons math
commons-math3.jar:
	ln -s `find . -regextype posix-extended -regex ".*commons-math3-([0-9].)*jar"` commons-math3.jar

# Symlink to current version of commons math javadoc
commons-math3-javadoc.jar:
	ln -s commons-math3-*javadoc.jar commons-math3-javadoc.jar

