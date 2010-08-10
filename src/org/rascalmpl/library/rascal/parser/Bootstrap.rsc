module rascal::parser::Bootstrap

import rascal::conversion::sdf2::SDF2Grammar;
import rascal::conversion::sdf2::Load;
import rascal::conversion::grammar::Grammar2Rascal;
import IO;

public void bootFromSDF() {
  rascaldef = loadSDF2Module("languages::rascal::syntax::Rascal", [|file:///Users/jurgenv/Sources/Rascal/rascal-grammar/spec|,|file:///Users/jurgenv/Sources/Rascal/sdf-library/library|]);
  grammar = sdf2grammar(rascaldef);
  source = grammar2rascal(grammar);
  writeFile(|project://RascalLibrary/src/rascal/syntax/RascalRascal.rsc|, "module rascal::syntax::RascalRascal\n\n" + source); 
}