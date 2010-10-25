for A 
in $@
do
  SUFFIX=`echo $A|grep -o '[^.]*$'`
  if [[ $A != /?* ]]
  then
     A=`pwd`/$A
  fi
${BINDIR}/rascal -Drascal.no_cwd_path=true <<END
import box::$SUFFIX::Default;
toHtml(|file://$A|);
END
done