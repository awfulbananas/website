#!/bin/bash
#this script compiles and runs the java source files found in generator/src

#apparently the javac command doesn't play well with terminal pipes, so this is
#unfortunately necessary (until I bother to set up gradle or maven anyways)

echo "compiling code"
javac -d 'generator/out' $(find generator/src | grep '.java')
cd generator/out

echo "running script"
java Main