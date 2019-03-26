@synopsis{Simple interface to the Lucene text analysis library}
@description{
This module wraps the Apache Lucene framework for text analysis. 

* It integrates deeply by
providing the interfaces for the analysis extension points of Lucene via Rascal callback functions: Analyzers, Tokenizers, Filters.
* It provides access to the full library of Lucene's text analyzers via their class names.
* It is a work in progress. Some configurability of Lucene is not yet exposed, for example
programmable weights for fields and the definition of similarity functions per document field. Also Query expressions are not yet exposed.
* This wrapper provides full abstraction over source locations. Both the directory of the index
as well as the locations of input documents are expressed using any existing rascal `loc`. 
}
module analysis::text::Lucene

import IO;

@synopsis{A Lucene document has a src and an open set of keyword fields which are also indexed}
@description{
A lucene document has a `src` origin and an open set of keyword fields. 
Add as many keyword fields to a document as you want. They will be added to the Lucene document as "Fields".

* fields of type `str` will be stored and indexed as-is
* fields of type `loc` will be indexed but not stored
}
data Document = document(loc src);
  
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
java void createIndex(loc indexFolder, set[Document] documents, rel[str field, Analyzer analyzer] analyzers = {<"src", standardAnalyzer()>});

@synopsis{The score relates to how well the query matched the document}
data Document(real score=.0);

@javaClass{org.rascalmpl.library.analysis.text.LuceneAdapter}
java list[Document] searchIndex(loc indexFolder, str query, int max = 10, rel[str field, Analyzer analyzer] analyzers = {<"src", standardAnalyzer()>});