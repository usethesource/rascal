module analysis::text::search::Grammars

extend analysis::text::search::Lucene;
import ParseTree;
import String;

Analyzer analyzerFromGrammar(type[&T <: Tree] grammar) = analyzer(tokenizerFromGrammar(grammar), [lowerCaseFilter()]);

Analyzer identifierAnalyzerFromGrammar(type[&T <: Tree] grammar) = analyzer(identifierTokenizerFromGrammar(grammar), [lowerCaseFilter()]);

Analyzer commentAnalyzerFromGrammar(type[&T <: Tree] grammar) = analyzer(commentTokenizerFromGrammar(grammar), [lowerCaseFilter()]);

@synopsis{Use a generate parser as a Lucene tokenizer. Skipping nothing.}
Tokenizer tokenizerFromGrammar(type[&T <: Tree] grammar) = tokenizer(list[Term] (str input) {
   try {
     tr = parse(grammar, input, |lucene:///|, allowAmbiguity=true);
     return [term("<token>", token.src, "<type(token.prod.def,())>") | token <- tokens(tr, isToken) ];
   }
   catch ParseError(_):
     return [term(input, |lucene:///|(0, size(input)), "entire input")];
});

@synopsis{Use a generated parser as a Lucene tokenizer, and skip all keywords and punctuation.}
Tokenizer identifierTokenizerFromGrammar(type[&T <: Tree] grammar) = tokenizer(list[Term] (str input) {
   try {
     tr = parse(grammar, input, |lucene:///|, allowAmbiguity=true);
     return [term("<token>", token.src, "<type(token.prod.def, ())>") | token <- tokens(tr, isToken), isLexical(token)];
   }
   catch ParseError(_):
     return [term(input, |lucene:///|(0, size(input)), "entire input")];
});

@synopsis{Use a generated parser as a Lucene tokenizer, and skip all keywords and punctuation.}
Tokenizer commentTokenizerFromGrammar(type[&T <: Tree] grammar) = tokenizer(list[Term] (str input) {
   try {
     tr = parse(grammar, input, |lucene:///|, allowAmbiguity=true);
     return [term("<comment>", comment.src, "<type(comment.prod.def,())>") | comment <- tokens(tr, isComment)];
   }
   catch ParseError(_):
     return [term(input, |lucene:///|(0, size(input)), "entire input")];
});

list[Tree] tokens(amb({Tree x, *_}), bool(Tree) isToken) = tokens(x, isToken);

default list[Tree] tokens(Tree tok, bool(Tree) isToken) {
  if (isToken(tok)) {
    return [tok];
  }
  else {
    return [*tokens(a, isToken) | tok has args, a <- tok.args];
  }
}

bool isTokenType(lit(_)) = true;
bool isTokenType(cilit(_)) = true;    
bool isTokenType(lex(_)) = true;  
bool isTokenType(layouts(_)) = true;
bool isTokenType(label(str _, Symbol s)) = isTokenType(s);
default bool isTokenType(Symbol _) = false;

bool isToken(appl(prod(Symbol s, _, _), _)) = true when isTokenType(s);
bool isToken(char(_)) = true;
default bool isToken(Tree _) = false;

bool isLexical(appl(prod(Symbol s, _, _), _)) = true when lex(_) := s || label(_, lex(_)) := s;
default bool isLexical(Tree _) = false;

bool isComment(Tree t) = true when t has prod, /"category"("Comment") := t.prod;
default bool isComment(Tree _) = false;
