module analysis::text::search::LuceneTest

import analysis::text::search::Grammars;

import lang::pico::\syntax::Main;
import lang::rascal::\syntax::Rascal;
import util::FileSystem;
import IO;
import List;
import util::Math;

public set[loc] programs = find(|std:///|, "pico");

data Document(loc comments = |unknown:///|, str extra = "");

data Analyzer(Analyzer comments = standardAnalyzer(), Analyzer extra = standardAnalyzer());

str abFilter(str token) = visit(token) {
  case /a/ => "b"
};

list[str] extraWords = ["ut", "desint", "vires", "tamen", "est", "laudanda", "voluntas"];

// the first analyzer is for the `src` document, parser the program and extracts all identifiers
Analyzer  an = analyzer(identifierTokenizerFromGrammar(#start[Program]), []);
  
// the second parses the program again, and lists all the tokens in source code comments, then maps them to lowercase.
Analyzer  commentAnalyzer = analyzer(commentTokenizerFromGrammar(#start[Program]), [lowerCaseFilter()]);
  
// the final analyzer analyses the extra field by splitting the words, mapping to lowercase and changing all a's to b's
Analyzer  extraAnalyzer = analyzer(classicTokenizer(), [lowerCaseFilter(), \filter(abFilter)]);
 
loc pi = |tmp:///picoIndex|;
 
void picoIndex() {
  remove(pi);
  
  docs = {document(p, comments=p, extra=extraWords[arbInt(size(extraWords))]) | p <- programs};
  
  // createIndex does not have access to default parameters yet, so each field has to be set explicitly.
  createIndex(pi, docs, analyzer=fieldsAnalyzer(an, comments=commentAnalyzer, extra=extraAnalyzer));
}

void picoSearch(str term) {  
  println("\'<term>\' results in identifiers:"); 
  iprintln(searchIndex(pi, "src:<term>", analyzer=fieldsAnalyzer(standardAnalyzer(), comments=standardAnalyzer())));
  
  println("\'<term>\' results in comments:");
  iprintln(searchIndex(pi, "comments:<term>", analyzer=fieldsAnalyzer(standardAnalyzer(), comments=standardAnalyzer())));
}  
 
void extraSearch() {
  searchAll = "<for (t <- extraWords) {><t> || <}>"[..-4];
  println("\'<searchAll>\' results in extra:");
  iprintln(searchIndex(pi, searchAll, analyzer=fieldsAnalyzer(standardAnalyzer(), comments=standardAnalyzer())));
}

void main() {
  picoIndex();
  picoSearch("x");
  picoSearch("input || x");
  picoSearch("input && output");
  Search("bst");
}
