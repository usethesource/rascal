module analysis::text::Grammars

extend analysis::text::Lucene;
import ParseTree;
import String;

Analyzer analyzerFromGrammar(type[&T <: Tree] grammar) = analyzer(tokenizerFromGrammar(grammar), [lowercaseFilter()]);

Analyzer identifierAnalyzerFromGrammar(type[&T <: Tree] grammar) = analyzer(identifierTokenizerFromGrammar(grammar), [lowercaseFilter()]);

Analyzer commentAnalyzerFromGrammar(type[&T <: Tree] grammar) = analyzer(commentTokenizerFromGrammar(grammar), [lowercaseFilter()]);

@synopsis{Use a generate parser as a Lucene tokenizer. Skipping nothing.}
Tokenizer tokenizerFromGrammar(type[&T <: Tree] grammar) = tokenizer(list[str] (str input) {
   try {
     return ["<token>" | token <- tokens(parse(grammar, input, |lucene:///|, allowAmbiguity=true), isToken) ];
   }
   catch ParseError(_, _):
     return [input];
});

@synopsis{Use a generated parser as a Lucene tokenizer, and skip all keywords and punctuation.}
Tokenizer identifierTokenizerFromGrammar(type[&T <: Tree] grammar) = tokenizer(list[str] (str input) {
   try {
     return ["<token>" | token <- tokens(parse(grammar, input, |lucene:///|, allowAmbiguity=true), isToken), isLexical(token)];
   }
   catch ParseError(_):
     return [input];
});

@synopsis{Use a generated parser as a Lucene tokenizer, and collect only source code comments.}
Tokenizer commentTokenizerFromGrammar(type[&T <: Tree] grammar) = tokenizer(list[str] (str input) {
   try {
     return [ "<comment>" | comment <- tokens(parse(grammar, input, |lucene:///|, allowAmbiguity=true), isComment)];
   }
   catch ParseError(_):
     return [input];
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
bool isTokenType(label(Symbol s, str _)) = isTokenType(s);
default bool isTokenType(Symbol _) = false;

bool isToken(appl(prod(Symbol s, _, _), _)) = true when isTokenType(s);
bool isToken(char(_)) = true;
default bool isToken(Tree _) = false;

bool isLexical(appl(prod(Symbol s, _, _), _)) = true when lex(_) := s || label(lex(_), _) := s;
default bool isLexical(Tree _) = false;

bool isComment(Tree t) = true when t has prod, /category("Comment") := t.prod;
default bool isComment(Tree _) = false;