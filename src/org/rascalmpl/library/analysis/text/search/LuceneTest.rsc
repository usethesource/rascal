module analysis::text::Test

import analysis::text::Grammars;
import lang::pico::\syntax::Main;
import lang::rascal::\syntax::Rascal;
import util::FileSystem;
import IO;

public set[loc] programs = find(|home:///git/rascal/src/|, "pico");
public set[loc] modules =  find(|home:///git/rascal/src/|, "rsc");

data Document(loc comments = |unknown:///|, str extra = "");
data Analyzer(Analyzer comments = standardAnalyzer());

void picoSearch(str term) {
  pi = |home:///picoIndex|;
  
  remove(pi);
  
  an = analyzer(identifierTokenizerFromGrammar(#start[Program]), []);
  commentAnalyzer = analyzer(commentTokenizerFromGrammar(#start[Program]), [lowerCaseFilter()]);
  
  docs = {document(p, comments=p, extra="joepie") | p <- programs};
  
  createIndex(pi, docs, analyzer=fieldsAnalyzer(an, comments=commentAnalyzer));
  
  iprintln(searchIndex(pi, term, analyzer=fieldsAnalyzer(standardAnalyzer(), comments=standardAnalyzer())));
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