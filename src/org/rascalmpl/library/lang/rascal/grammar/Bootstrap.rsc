module lang::rascal::grammar::Bootstrap

import lang::rascal::\syntax::Rascal; 

import lang::rascal::grammar::ParserGenerator;
import lang::rascal::grammar::SyntaxTreeGenerator;
import lang::rascal::grammar::definition::Modules;
import lang::rascal::grammar::definition::Parameters;

import IO;
import ValueIO;  
import Grammar;
import util::Monitor;

private str package = "org.rascalmpl.library.lang.rascal.syntax";
private loc inputFolder = |rascal:///lang/rascal/syntax|;
//private loc outputFolder = |boot:///src/org/rascalmpl/library/lang/rascal/syntax|;
//private loc astFolder = |boot:///src/org/rascalmpl/ast|;
private loc outputFolder = |home:///git/rascal/src/org/rascalmpl/library/lang/rascal/syntax|;
private loc astFolder = |home:///git/rascal/src/org/rascalmpl/ast|;

private str grammarName = "Rascal";
private str rootName = "RascalParser";

public Grammar getRascalGrammar() {
  event("parsing the rascal definition of rascal");
  Module \module = parse(#start[Module], inputFolder + "/Rascal.rsc").top;
  event("imploding the syntax definition and normalizing and desugaring it");
  return modules2grammar("lang::rascal::syntax::Rascal", {\module});
}

public void bootstrap() {
  gr = getRascalGrammar();
  bootParser(gr);
  bootAST(gr);
}

public void bootParser(Grammar gr) {
  event("generating new Rascal parser");
  source = newGenerate(package, rootName, gr);
  writeFile(outputFolder + "/<rootName>.java", source);
}

public void bootAST(Grammar g) {
  g = expandParameterizedSymbols(g);
  
  patterns = g.rules[sort("Pattern")];
  //patterns = visit(patterns) { case sort("Pattern") => sort("Expression") }
  
  // extend Expression with the Patterns
  g.rules[sort("Expression")] = choice(sort("Expression"), {patterns, g.rules[sort("Expression")]}); 
  g.rules -= (sort("Pattern"): choice(sort("Pattern"), {}));
  
  // make sure all uses of Pattern have been replaced by Expression
  g = visit(g) { case sort("Pattern") => sort("Expression") }
  
  grammarToJavaAPI(astFolder, "org.rascalmpl.ast", g);
}

