@doc{
Synopsis: a callback framework for externally registering M3 extractors.

Description:

This module facilitates extension for different kinds of M3 extractors (e.g. for different languages)  
}
module analysis::m3::Extractors

extend analysis::m3::Core;
import util::FileSystem;
import String;

data Language(str version = "")
  = generic()
  ;
  
data \AST(loc file = |unknown:///|)
  = \genericAST(str contents)
  | noAST(Message msg)
  ;

alias M3Extractor = set[M3] (loc project, set[loc] files);
alias ASTExtractor = set[\AST] (loc project, set[loc] files);

private rel[Language, M3Extractor] M3Registry = {};
private rel[Language, ASTExtractor] ASTRegistry = {};

void registerExtractor(Language language, M3Extractor extractor) {
  M3Registry += {<language, extractor>};
}

void registerExtractor(Language language, ASTExtractor extractor) {
  ASTRegistry += {<language, extractor>};
}

rel[Language, M3] extractM3(loc project) = extractM3(project, {project});
rel[Language, \AST] extractAST(loc project) = extractAST(project, {project});

@doc{
Synopsis: runs all extractors on a project to return one M3 model per file in the project
}
rel[Language, M3] extractM3(loc project, set[loc] roots) {
  allFiles = { *files(r) | r <- roots };
  
  return {<e<0>, f> |  e<-M3Registry, f <- e<1>(project, allFiles)}; 
}

rel[Language, \AST] extractAST(loc project, set[loc] roots) {
  allFiles = { *files(r) | r <- roots };
  
  return {<e<0>, f> |  e<-ASTRegistry, f <- e<1>(project, allFiles)}; 
}
