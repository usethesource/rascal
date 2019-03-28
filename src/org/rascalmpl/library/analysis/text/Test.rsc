module analysis::text::Test

import analysis::text::Grammars;
import lang::pico::\syntax::Main;
import lang::rascal::\syntax::Rascal;
import util::FileSystem;
import IO;

public set[loc] programs = find(|home:///git/rascal/src/|, "pico");
public set[loc] modules =  find(|home:///git/rascal/src/|, "rsc");

void picoSearch(str term) {
  pi = |home:///picoIndex|;
  
  remove(pi);
  
  an = analyzer(identifierTokenizerFromGrammar(#start[Program]), []);
  
  docs = {document(p) | p <- programs};
  
  createIndex(pi, docs, analyzer=an);
  
  iprintln(searchIndex(pi, term));
}

void rascalIndex() {
  pi = |home:///rascalIndex|;
  
  remove(pi);
  
  an = analyzer(identifierTokenizerFromGrammar(#start[Module]), [lowerCaseFilter()]);
  
  docs = {document(p) | p <- modules};
  
  createIndex(pi, docs, analyzer=an);
}

void rascalSearch(str term) {
  pi = |home:///rascalIndex|;
  
  iprintln(searchIndex(pi, term));
}