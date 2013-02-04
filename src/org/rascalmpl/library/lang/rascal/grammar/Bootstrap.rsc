@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module lang::rascal::grammar::Bootstrap

import lang::rascal::\syntax::RascalRascal; 

import lang::rascal::format::Grammar;

import lang::rascal::grammar::definition::Productions;
import lang::rascal::grammar::ParserGenerator;
import lang::rascal::grammar::SyntaxTreeGenerator;
import lang::rascal::grammar::definition::Parameters;
import lang::rascal::grammar::definition::Modules;

import Grammar;
import ParseTree;

import IO;
import ValueIO;  

private str package = "org.rascalmpl.library.lang.rascal.syntax";
private loc inputFolder = |rascal:///lang/rascal/syntax|;
//private loc outputFolder = |project://RascalLibrary/src/lang/rascal/syntax|;
//private loc astFolder = |project://RascalLibrary/src/org/rascalmpl/ast|;
private loc outputFolder = |boot:///src/org/rascalmpl/library/lang/rascal/syntax|;
private loc astFolder = |boot:///src/org/rascalmpl/ast|;
 //private loc astFolder = |home:///Workspace/rascal/src/org/rascalmpl/ast|;
//private loc outputFolder = |home:///Workspace/rascal-ngb1/src/org/rascalmpl/library/lang/rascal/syntax|;
//private loc astFolder = |home:///Workspace/rascal-ngb1/src/org/rascalmpl/ast|;

private str grammarName = "RascalRascal";
private str rootName = "RascalRascal";
private str objectName = "ObjectRascalRascal";
private str metaName = "MetaRascalRascal";

public void bootstrap() {
  gr = getRascalGrammar();
  bootRootParser(gr);
  bootObjectParser(gr);
  
 
  bootMetaParser(gr);
  bootAST(gr);
}

public Grammar getRascalGrammar() {
  println("parsing the rascal definition of rascal");
  Module \module = parse(#Module, inputFolder + "/<grammarName>.rsc");
  println("imploding the syntax definition and normalizing and desugaring it");
  return modules2grammar("lang::rascal::syntax::RascalRascal", {\module});
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

public void bootRootParser(Grammar gr) {
  println("generating root parser");
  str source = generateRootParser(package,rootName, gr);
  println("writing rascal root parser");
  writeFile(outputFolder + "/<rootName>.java", source);
}

public void bootObjectParser(Grammar gr) {
  println("generating rascal object parser");
  source = generateObjectParser(package, objectName, gr);
  println("writing rascal object parser");
  writeFile(outputFolder + "/<objectName>.java", source);
}

public void bootMetaParser(Grammar gr) {
  gr.rules -= (lex("Rest") : choice(lex("Rest"),{}));
  gr.rules -= (sort("PreModule") : choice(sort("PreModule"),{}));
  println("generating assimilated rascal for rascal parser");
  source = generateMetaParser(package, metaName, objectName, gr);
  println("writing assimilated parser");
  writeFile(outputFolder + "/<metaName>.java", source);
}
