module analysis::text::search::LuceneTest

import analysis::text::search::Grammars;

import lang::pico::\syntax::Main;
import lang::rascal::\syntax::Rascal;
import util::FileSystem;
import IO;
import util::UUID;

public set[loc] programs = find(|home:///git/rascal/src/|, "pico");

data Document(loc comments = |unknown:///|, str extra = "");

data Analyzer(Analyzer comments = standardAnalyzer(), Analyzer extra = standardAnalyzer());

private str abFilter(str token) = visit(token) {
  case /a/ => "b"
};

set[str] extraWords = {"ut", "desint", "vires", "tamen", "est", "laudanda", "voluntas"};

void picoSearch(str term) {
  pi = |home:///picoIndex|;
  
  remove(pi);
  
  // the first analyzer is for the `src` document, parser the program and extracts all identifiers
  an = analyzer(identifierTokenizerFromGrammar(#start[Program]), []);
  
  // the second parses the program again, and lists all the tokens in source code comments, then maps them to lowercase.
  commentAnalyzer = analyzer(commentTokenizerFromGrammar(#start[Program]), [lowerCaseFilter()]);
  
  // the final analyzer analyses the extra field by splitting the words, mapping to lowercase and changing all a's to b's
  extraAnalyzer = analyzer(classicTokenizer(), [lowerCaseFilter(), \filter(abFilter())]);
  
  docs = {document(p, comments=p, extra="joepie") | p <- programs};
  
  // createIndex does not have access to default parameters yet, so each field has to be set explicitly.
  createIndex(pi, docs, analyzer=fieldsAnalyzer(an, comments=commentAnalyzer, extra=extraAnalyzer));
  
  iprintln(searchIndex(pi, term, analyzer=fieldsAnalyzer(standardAnalyzer(), comments=standardAnalyzer())));
}

