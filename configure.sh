#!/bin/bash

# Settings
project="foresee"
extension=".java"
src="it/unica/foresee"
basedir=`pwd`
mkfile="Makefile"
mkfolder_base="makefiles/autogen"
conffolder_base="makefiles/config"
mkfolder="$basedir/$mkfolder_base"
conffolder="$basedir/$conffolder_base"
dirs=`find $src -type d` #list the directories under src
include_base="$mkfolder_base/include.mk"
include="$mkfolder/include.mk"
command="javac -cp"
options=""
libs="."



# Execution
echo "Generating makefiles for classes and packages..."

# Clean up
rm "$mkfolder/"*.mk

# Makefile to include other makefiles
touch $include

# Main makefile
touch "$mkfile"

echo "#To edit the makefile look in the $conffolder_base folder\n" > $mkfile
echo "include $include_base" >> $mkfile
echo "include $conffolder_base/conf.mk" >> $mkfile

# Loop troughout the dirs under src
for dir in $dirs
do
  cd "$basedir/$dir"
  
  # Check that the dir is not empty
  if [ ! -z "`ls -a | grep $extension`" ]
  then
    #Get package name
    package=${PWD##*/}
    
    # Interfaces packages need more care
    if [ "$package" = "interfaces" ]
    then
      package_path="$src/$oldpackage/$package"
      package="$oldpackage-$package"
      type="interface"
    else
      package_path="$src/$package"
      # If it's not an interface it's a class
      type="class"
    fi
    
    if [ "$package" = "$project" ]
    then
      package_path=$src
    fi
    
    echo "\n$package"
    
    # Test packages need more libs
    if [ "$package" = "tests" ]
    then
      pklibs="$libs"':$(testlibs)'
    else
      pklibs=$libs
    fi
    
    # Reset the file list
    filelist=""
    
    #Create the makefile and include it
    pkinclude="$mkfolder/$package.mk"
    
    touch "$pkinclude"
    echo "include $pkinclude" >> $include
    echo "# $package dependencies, generated by configure.sh\n" > $pkinclude
    
    
    for file in *.java
    do
      # Obtain the name without '.java'
      class="`echo $file | cut -d '.' -f 1`"
      echo "\t $class"
      
      # List all the class of the package
      filelist="$filelist $type$class"
      
      echo "$type$class: $package_path/$class.class \n" >> $pkinclude
      echo "$package_path/$class.class:\n\t$command $pklibs $package_path/$file $options\n\n" >> $pkinclude
      
    done
    
    # The package depends on its containing files
    echo "$package:$filelist" >> $pkinclude
    
    # Useful for interfaces
    oldpackage=$package

  else
    # Allow empty packages with interfaces
    oldpackage=${PWD##*/} 
  fi
done

echo "Generation completed. Now you can use the make tool"













