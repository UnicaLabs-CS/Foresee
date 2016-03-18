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
             

# Path of the makefiles (.mk) to be included
mk_path = makefiles/autogen

# Path(s) to clean
clean_path = $(tests_path) \
             $(paths_list) \
             $(mk_path)

# Path of the libraries
libs_path = libs

# Path of the test libraries
test_libs_path = test-libs

# Libraries
# =========
test-libs-link:
	cd $(test_libs_path); make all

libs-link:
	cd $(libs_path); make all

# Generic
# =======
all: main

clean:
	for file in "`find -name *.class`"; \
		do rm -v $$file; \
	done; \
	if -f $(proj).jar; then rm $(proj).jar; fi; \
	if -f "Makefile"; then rm "Makefile"; fi;

# Make the readme inside a body tag
docs/README.html:
	echo '<body>' >  docs/README.html; \
	pandoc README.md --to=HTML5 >> docs/README.html; \
	echo '</body>' >> docs/README.html;
	

# Generate the documentation
doc: docs/README.html javadoc-style/stylesheet.css javadoc-style/md-doclet.jar
	javadoc -overview docs/README.html \
	-docletpath javadoc-style/md-doclet.jar \
	-stylesheetfile javadoc-style/stylesheet.css \
	-doclet org.umlgraph.markdown.doclet.UmlGraphDoc \
	-version -author -d docs -subpackages it

# Create a jar file
jar: main Manifest.txt
	jar -cvfm $(proj).jar Manifest.txt `find -name *.class`;

# Main
# ====
main: classForesee

# Libs
# ====
junit = $(libs_path)/junit-4.jar
hamcrest = $(libs_path)/hamcrest.jar
system_rules = $(libs_path)/system-rules.jar
