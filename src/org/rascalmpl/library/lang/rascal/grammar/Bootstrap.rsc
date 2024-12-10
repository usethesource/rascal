@bootstrapParser
module lang::rascal::grammar::Bootstrap

import lang::rascal::\syntax::Rascal; 
import ParseTree;

import lang::rascal::grammar::ParserGenerator;
import lang::rascal::grammar::SyntaxTreeGenerator;
import lang::rascal::grammar::definition::Modules;
import lang::rascal::grammar::definition::Parameters;

import IO;
import Grammar;

private str package = "org.rascalmpl.library.lang.rascal.syntax";
private str rootName = "RascalParser";

public Grammar getRascalGrammar(loc grammarFile) {
  Module \module = parse(#start[Module], grammarFile).top;
  return modules2grammar("lang::rascal::syntax::Rascal", {\module});
}

public void bootstrapRascalParser(list[loc] srcs) {
  rascalLib = srcs[0];
  gr = getRascalGrammar(rascalLib + "lang/rascal/syntax/Rascal.rsc");
  source = newGenerate(package, rootName, gr);
  writeFile(rascalLib + "lang/rascal/syntax/<rootName>.java", source);
}

public void bootstrap(loc rascalHome) {
  println("generating from <rascalHome>");
  gr = getRascalGrammar(rascalHome + "src/org/rascalmpl/library/lang/rascal/syntax/Rascal.rsc");
  bootParser(gr, rascalHome);
  bootAST(gr, rascalHome);
}

public void bootstrapAst(loc rascalHome) {
  println("generating from <rascalHome>");
  gr = getRascalGrammar(rascalHome + "src/org/rascalmpl/library/lang/rascal/syntax/Rascal.rsc");
  bootAST(gr, rascalHome);
}



public void bootParser(Grammar gr, loc rascalHome) {
  source = newGenerate(package, rootName, gr);
  writeFile(rascalHome + "src/org/rascalmpl/library/lang/rascal/syntax/<rootName>.java", source);
}

public void bootAST(Grammar g, loc rascalHome) {
  g = expandParameterizedSymbols(g);
  
  patterns = g.rules[sort("Pattern")];
  //patterns = visit(patterns) { case sort("Pattern") => sort("Expression") }
  
  // extend Expression with the Patterns
  g.rules[sort("Expression")] = choice(sort("Expression"), {patterns, g.rules[sort("Expression")]}); 
  g.rules -= (sort("Pattern"): choice(sort("Pattern"), {}));
  
  // make sure all uses of Pattern have been replaced by Expression
  g = visit(g) { case sort("Pattern") => sort("Expression") }
  
  grammarToJavaAPI(rascalHome + "src/org/rascalmpl/ast", "org.rascalmpl.ast", g);
}

