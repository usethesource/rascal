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
module analysis::text::search::Lucene

import IO;

@synopsis{A Lucene document has a src and an open set of keyword fields which are also indexed}
@description{
A lucene document has a `src` origin and an open set of keyword fields. 
Add as many keyword fields to a document as you want. They will be added to the Lucene document as "Fields".

* fields of type `str` will be stored and indexed as-is
* fields of type `loc` will be indexed but not stored
}
data Document = document(loc src, real score=.0, list[loc] matches = []);

data Analyzer 
  = analyzerClass(str analyzerClassName) 
  | analyzer(Tokenizer tokenizer, list[Filter] pipe)
  ;

@synopsis{A fieldsAnalyzer declares using keyword fields which Analyzers to use for which Document field.}
@description{
The `src` parameter of `fieldsAnalyzer` aligns with the `src` parameter of a Document: this analyzer is used
to analyze the `src` field. Any other keyword fields, of type `Analyzer` are applied to the contents of a
`Document` keyword field of type `loc` or `str` with the same name. 
}
data Analyzer  
  = fieldsAnalyzer(Analyzer src) 
  ;

data Term = term(str chars, int offset, str kind);
    
data Tokenizer
  = tokenizer(list[Term] (str input) tokenizerFunction)
  | tokenizerClass(str tokenizerClassName)
  ;
  
data Filter
  = \filter(str (str term) filterFunction)
  | filterClass(str filterClassName)
  ;  

@javaClass{org.rascalmpl.library.analysis.text.search.LuceneAdapter}
@synopsis{Creates a Lucene index at a given folder location from the given set of Documents, using a given set of text analyzers}
java void createIndex(loc index, set[Document] documents, Analyzer analyzer = standardAnalyzer());

@javaClass{org.rascalmpl.library.analysis.text.search.LuceneAdapter}
@synopsis{Searches a Lucene index indicated by the indexFolder by analyzing a query with a given set of text analyzers and then matching the query to the index.}
java list[Document] searchIndex(loc index, str query, Analyzer analyzer = standardAnalyzer(), int max = 10, bool spans=true);

@javaClass{org.rascalmpl.library.analysis.text.search.LuceneAdapter}
@synopsis{Inspect the terms stored in an index for debugging purposes (what did the analyzers do to the content of the documents?)}
java rel[str text, int frequency] inspectTerms(loc index, str field, int max = 10);

 @javaClass{org.rascalmpl.library.analysis.text.search.LuceneAdapter}
@synopsis{Inspect the fields stored in an index for debugging purposes (which fields have been indexed, for how many documents, and how many terms?)}
java rel[str field, int docCount, int sumTotalTermFreq] inspectFields(loc index); 


Analyzer classicAnalyzer()    = analyzerClass("org.apache.lucene.analysis.standard.ClassicAnalyzer");
Analyzer simpleAnalyzer()     = analyzerClass("org.apache.lucene.analysis.core.SimpleAnalyzer");
Analyzer standardAnalyzer()   = analyzerClass("org.apache.lucene.analysis.standard.StandardAnalyzer");
Analyzer whitespaceAnalyzer() = analyzerClass("org.apache.lucene.analysis.core.WhitespaceAnalyzer");  

Tokenizer classicTokenizer()   = tokenizerClass("org.apache.lucene.analysis.standard.ClassicTokenizer");
Tokenizer lowerCaseTokenizer() = tokenizerClass("org.apache.lucene.analysis.core.LowerCaseTokenizer");

Filter lowerCaseFilter() = filterClass("org.apache.lucene.analysis.LowerCaseFilter");