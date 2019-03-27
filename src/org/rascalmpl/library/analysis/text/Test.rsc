module analysis::text::Test

import analysis::text::Grammars;
import lang::pico::\syntax::Main;
import lang::rascal::\syntax::Rascal;
import util::FileSystem;
import IO;

public set[loc] programs = find(|home:///git/rascal/src/|, "pico");
public set[loc] modules =  find(|home:///git/rascal/src/|, "rsc");

void picoSearch(str term) {
  remove(|home:///picoIndex|);
  
  pi = index(|home:///picoIndex|, analyzers={<"src", analyzer(identifierTokenizerFromGrammar(#start[Program]), [])>});
  
  createIndex(pi, {document(p) | p <- programs});
  
  iprintln(searchIndex(pi, term));
}

void rascalIndex() {
  remove(|home:///rascalIndex|);
  
  pi = index(|home:///rascalIndex|, analyzers={<"src", analyzer(identifierTokenizerFromGrammar(#start[Module]), [lowerCaseFilter()])>});
  
  createIndex(pi, {document(p) | p <- modules});
}

void rascalSearch(str term) {
  pi = index(|home:///rascalIndex|, analyzers={<"src", standardAnalyzer()>});
  
  iprintln(searchIndex(pi, term));
}