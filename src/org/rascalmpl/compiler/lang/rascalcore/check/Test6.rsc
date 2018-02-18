module lang::rascalcore::check::Test6
import lang::rascal::grammar::ParserGenerator;
import Grammar;
import IO;

import lang::rascalcore::check::Test1;

loc ClassTest1ParserLoc = |project://rascal-core/src/org/rascalmpl/core/library/rascalcore/grammar/tests/generated_parsers/ClassicTest.java|;

str generateTest1Parser() = newGenerate("org.rascalmpl.core.library.lang.rascal.grammar.tests.generated_parsers", "Test1", grammar({\start(\sort("E"))}, #E.definitions));

void generateAndWriteTest1Parser(){
    writeFile(ClassTest1ParserLoc, generateTest1Parser());
}