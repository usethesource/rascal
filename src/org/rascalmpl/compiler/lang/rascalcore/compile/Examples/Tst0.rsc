module lang::rascalcore::compile::Examples::Tst0

import IO;
import List;

loc RASCAL =      |mvn://org.rascalmpl!rascal!0.40.17/!|;
loc RASCAL_JAR  = |jar+mvn://org.rascalmpl!rascal!0.40.17/!|; 

void main(){
    println("exists(<RASCAL>): <exists(RASCAL)>");
    println("exists(<RASCAL_JAR>): <exists(RASCAL_JAR)>");
    path = "org/rascalmpl/library/analysis/grammars/Ambiguity.rsc";
    println("exists(<RASCAL+path>): <exists(RASCAL+path)>");
    println("exists(<RASCAL_JAR+path>): <exists(RASCAL_JAR+path)>");

    println("<RASCAL>.ls: <head(RASCAL.ls, 3)>");
    println("<RASCAL_JAR>.ls: <head(RASCAL_JAR.ls, 3)>");
}