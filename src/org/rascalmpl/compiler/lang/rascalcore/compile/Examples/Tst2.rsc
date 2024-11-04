//@bootstrapParser
module lang::rascalcore::compile::Examples::Tst2

import IO;
import ValueIO;
extend lang::rascalcore::check::CheckerCommon;

loc EXCEPTIONTPL1
 = |jar+file:///Users/paulklint/.m2/repository/org/rascalmpl/rascal/0.40.8-SNAPSHOT/rascal-0.40.8-SNAPSHOT.jar!rascal/$Exception.tpl|;

 loc VersionTPL = |file:///Users/paulklint/git/generated-sources/target/typepal/generated-resources/src/main/java/rascal/analysis/typepal/$Version.tpl|;
 void main(){
    tm = readBinaryValueFile(#TModel, VersionTPL);
    iprintln(tm.logical2physical);
    println("usesPhysicalLocs: <tm.usesPhysicalLocs?>, <tm has usesPhysicalLocs>, <tm.usesPhysicalLocs>");
 //   println("convertedToPhysical: <tm.convertedToPhysical?>, <tm has convertedToPhysical>, <tm.convertedToPhysical>");
}