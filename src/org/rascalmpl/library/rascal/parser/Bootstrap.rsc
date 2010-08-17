module rascal::parser::Bootstrap

import rascal::conversion::sdf2::SDF2Grammar;
import rascal::conversion::sdf2::Load;
import rascal::conversion::grammar::Grammar2Rascal;
import rascal::parser::Definition;
import rascal::parser::Grammar;
import rascal::parser::Generator;
import rascal::syntax::RascalForImportExtraction;
import IO;

public void bootFromSDF() {
  rascaldef = loadSDF2Module("languages::rascal::syntax::Rascal", [|file:///Users/jurgenv/Sources/Rascal/rascal-grammar/spec|,|file:///Users/jurgenv/Sources/Rascal/sdf-library/library|]);
  gr = sdf2grammar(rascaldef);
  source = grammar2rascal(gr);
  writeFile(|project://RascalLibrary/src/rascal/syntax/RascalRascal.rsc|, "module rascal::syntax::RascalRascal\n\n" + source); 
}

public void bootFromRascal() {
  println("parsing the rascal definition of rascal");
  Module \module = parse(#Module, |project://RascalLibrary/src/rascal/syntax/RascalRascal.rsc|);
  println("imploding the syntax definition and normalizing and desugaring it");
  Grammar gr = module2grammar(\module);
  println("generating Java source code");
  str source = generate("org.rascalmpl.library.rascal.parser","RascalRascal", gr);
  println("writing a file");
  writeFile(|project://RascalLibrary/src/rascal/parser/RascalRascal.java|, source);
  println("rascal parser has been generated");  
}