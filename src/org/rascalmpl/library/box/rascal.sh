RDIR=$INSTALLEDHOME/rascal
INSTALLED=$INSTALLEDHOME/rascal-fragment/installed
$JAVAHOME/bin/java -d32 -cp $RDIR/lib/jline-0.9.94.jar:$RDIR/bin:$INSTALLEDHOME/sglr-invoker/bin:$WORK/org.eclipse.imp.pdb.values/bin:$WORK/org.eclipse.imp.pdb/bin \
-Djava.library.path=$INSTALLED/lib \
    -Drascal.parsetable.default.file=$INSTALLED/share/rascal-grammar/rascal.tbl \
    -Drascal.parsetable.header.file=$INSTALLED/share/rascal-grammar/rascal-header.tbl \
    -Drascal.parsetable.cache.dir=/tmp \
    -Drascal.rascal2table.command=$INSTALLED/bin/rascal2table \
    -Drascal.rascal2table.dir=$INSTALLED/bin \
    -Drascal.sdf.library.dir=$INSTALLED/share/sdf-library/library \
    -Drascal.base.binary.path=$INSTALLED/bin \
    $@ \
    -Xss8m \
    -Xmx256m \
org.rascalmpl.interpreter.RascalShell
