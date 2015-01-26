module lang::rascal::grammar::tests::Compare

import IO;
import List;
import String;
import Set;

value main(list[value] args) {
 INT = split("\n", readFile(|project://rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/generated_parsers/CParserINT.java|));
 COMP = split("\n", readFile(|project://rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/generated_parsers/CParserCOMP.java|));
 
 INTSET = toSet(INT);
 COMPSET = toSet(COMP);
 println("INT: <size(INT)> lines, COMP <size(COMP)> lines");
 println("INT - COMP : <size(INT - COMP)> lines, COMP - INT <size(COMP - INT)> lines");
 
 println("INT - COMP:");
 iprintln(INT-COMP);
 
 println("COMP - INT:");
 iprintln(COMP - INT);
 
 
 println("INTSET: <size(INTSET)> lines, COMPSET <size(COMPSET)> lines");
 println("INTSET - COMPSET : <size(INTSET - COMPSET)> lines, COMPSET - INTSET <size(COMPSET - INTSET)> lines");
 
 println("INTSET - COMPSET:");
 iprintln(INTSET-COMPSET);
 
 println("COMPSET - INTSET:");
 iprintln(COMPSET - INTSET);
 
 return true;
}
 
 