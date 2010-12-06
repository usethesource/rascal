@bootstrapParser
module rascal::syntax::Bootstrap

import rascal::syntax::Grammar2Rascal;
import rascal::syntax::Definition;
import rascal::syntax::Grammar;
import rascal::syntax::Generator;
import rascal::syntax::RascalRascal; 
import rascal::syntax::ASTGen;
import rascal::syntax::Parameters;
import ParseTree;
import IO;
import ValueIO;  

private str package = "org.rascalmpl.library.rascal.syntax";
private loc inputFolder = |rascal:///rascal/syntax|;
//private loc outputFolder = |project://RascalLibrary/src/rascal/syntax|;
//private loc astFolder = |project://RascalLibrary/src/org/rascalmpl/ast|;
private loc outputFolder = |boot:///src/org/rascalmpl/library/rascal/syntax|;
private loc astFolder = |boot:///src/org/rascalmpl/ast|;
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
  return module2grammar(\module);
}

public void bootAST(Grammar g) {
  g = expandParameterizedSymbols(g);
  
  patterns = g.rules[sort("Pattern")];
  patterns = visit(patterns) { case sort("Pattern") => sort("Expression") }
  
  // extend Expression with the Patterns
  g.rules[sort("Expression")] += patterns;
  g.rules -= (sort("Pattern"): {}, sort("RascalReservedKeywords"): {});
  
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
  println("generating assimilated rascal for rascal parser");
  source = generateMetaParser(package, metaName, objectName, gr);
  println("writing assimilated parser");
  writeFile(outputFolder + "/<metaName>.java", source);
}