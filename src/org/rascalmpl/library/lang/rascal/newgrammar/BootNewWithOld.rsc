module lang::rascal::newgrammar::BootNewWithOld

import lang::rascal::\newsyntax::Rascal; 

import lang::rascal::grammar::ParserGenerator;
import lang::rascal::grammar::definition::Modules;

import IO;
import ValueIO;  
import Grammar;

private str package = "org.rascalmpl.library.lang.rascal.newsyntax";
private loc inputFolder = |rascal:///lang/rascal/newsyntax|;
private loc outputFolder = |boot:///src/org/rascalmpl/library/lang/rascal/newsyntax|;

private str grammarName = "Rascal";
private str rootName = "RascalParser";

public Grammar getRascalGrammar() {
  println("parsing the rascal definition of rascal");
  Module \module = parse(#start[Module], inputFolder + "/Rascal.rsc").top;
  println("imploding the syntax definition and normalizing and desugaring it");
  return modules2grammar("lang::rascal::newsyntax::Rascal", {\module});
}

public void bootstrap() {
  gr = getRascalGrammar();
  println("generating new root parser");
  source = generateRootParser(package,rootName, gr);
  println("writing rascal root parser");
  writeFile(outputFolder + "/<rootName>.java", source);
}
