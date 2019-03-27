module analysis::text::Test

import analysis::text::Grammars;
import lang::pico::\syntax::Main;
import util::FileSystem;
import IO;

public set[loc] programs = find(|home:///git/rascal/src/|, "pico");

void picoSearch(str term) {
  remove(|home:///picoIndex|);
  
  pi = index(|home:///picoIndex|, analyzers={<"src", analyzer(identifierTokenizerFromGrammar(#start[Program]), [])>});
  
  createIndex(pi, {document(p) | p <- programs});
  
  iprintln(searchIndex(pi, term));
}