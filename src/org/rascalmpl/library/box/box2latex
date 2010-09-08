#!/bin/bash
for A 
in $@
do
  SUFFIX=`echo $A|grep -o '[^.]*$'`
  if [[ $A != /?* ]]
  then
     A=`pwd`/$A
  fi
  if test "${SUFFIX}" = "rsc" 
  then
     SUFFIX=rascal
  fi
rascal <<END
import box::$SUFFIX::Default;
toLatex(|file://$A|);
END
done

