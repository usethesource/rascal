@synopsis{Simple interface to the Lucene text analysis library}
module analysis::text::Lucene

import IO;

@synopsis{A Lucene document has a src and an open set of keyword fields which are also indexed}
@description{
A lucene document has a `src` origin and an open set of keyword fields. 
Add as many keyword fields to a document as you want. They will be added to the Lucene document as "Fields".

* fields of type `str` will be stored and indexed as-is
* fields of type `loc` will be indexed but not stored
}
data Document = document(loc src, str content = readFile(src));
  
data Analyzer 
  = analyzerClass(str analyzerClassName) 
  | analyzer(Tokenizer tokenizer, list[Filter] filters)
  ;
  
data Tokenizer
  = tokenizer(list[str] (str) tokenizerFunction)
  | tokenizerClass(str tokenizerClassName)
  ;
  
data Filter
  = \filter(str (str) filterFunction)
  | filterClass(str filterClassName)
  ;  

Analyzer classicAnalyzer()    = analyzerClass("org.apache.lucene.analysis.standard.ClassicAnalyzer");
Analyzer simpleAnalyzer()     = analyzerClass("org.apache.lucene.analysis.core.SimpleAnalyzer");
Analyzer standardAnalyzer()   = analyzerClass("org.apache.lucene.analysis.standard.StandardAnalyzer");
Analyzer whitespaceAnalyzer() = analyzerClass("org.apache.lucene.analysis.core.WhitespaceAnalyzer");  

Tokenizer classicTokenizer()   = tokenizerClass("org.apache.lucene.analysis.standard.ClassicTokenizer");
Tokenizer lowercaseTokenizer() = tokenizerClass("org.apache.lucene.analysis.core.LowercaseTokenizer");

Filter lowercaseFilter() = filterClass("org.apache.lucene.analysis.LowercaseFilter");

@javaClass{org.rascalmpl.library.analysis.text.LuceneAdapter}
java void createIndex(loc indexFolder, set[Document] documents, rel[str field, Analyzer analyzer] analyzers = {<"src", whitespaceAnalyzer()>});

@javaClass{org.rascalmpl.library.analysis.text.LuceneAdapter}
java list[Document] searchIndex(loc indexFolder, str query, int max = 10, rel[str field, Analyzer analyzer] analyzers = {<"src", whitespaceAnalyzer()>});