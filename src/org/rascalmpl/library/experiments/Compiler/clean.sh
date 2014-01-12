#!/bin/sh

# Clean -- remove all generated .rvm files
#
# Usage:
# cd experiments/Compiler directory
# clean

COMMAND="rm"
echo COMMAND -- ${COMMAND}

Handle(){
  case $1 in
  ../../experiments/Compiler/RVM/programs/Example[0-9]*.rvm)
        echo "Keep:" $1
	;;
  *)
    echo "Remove:" $1
	${COMMAND} $1
        ;;
  esac
}

for dir in Benchmarks Examples Tests ../.. ../../../../../../rascal-test
do 
	 for f in `find $dir -name "*.rvm"`
         do
		Handle $f
	done
done
