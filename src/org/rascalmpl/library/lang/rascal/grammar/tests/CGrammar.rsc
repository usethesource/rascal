module lang::rascal::grammar::tests::CGrammar

import IO;
import Grammar;
import ParseTree;
import String;
import lang::rascal::grammar::ParserGenerator;
import lang::rascal::grammar::Lookahead;
import util::Benchmark;
import util::Reflective;
import ValueIO;

public Grammar C = readBinaryValueFile(#Grammar, getModuleLocation("lang::rascal::grammar::tests::CGrammar").parent + "C-grammar.bin");

loc CParserLoc = getModuleLocation("lang::rascal::grammar::tests::CGrammar").parent + "generated_parsers/CParser.java.gz";

str generateCParser() = newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "CParser", C);

void generateAndWriteCParser(){
	writeFile(CParserLoc, generateCParser());
}

int generateAndTimeCParser() { 
	println("GenerateAndTimeCParser");
	t = cpuTime(); 
	generateCParser();
	return (cpuTime() - t)/1000000;
}	

value main() { return generateAndTimeCParser(); }

test bool tstGenerateCParser() = sameLines(generateCParser(), readFile(CParserLoc));