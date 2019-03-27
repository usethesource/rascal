module analysis::text::Grammars

extend analysis::text::Lucene;
import ParseTree;
import String;

@synopsis{Use a generate parser as a Lucene tokenizer. Skipping nothing.}
Tokenizer tokenizerFromGrammar(type[&T <: Tree] grammar) = tokenizer(list[str] (str input) {
   try {
     return ["<token>" | token <- tokens(parse(grammar, input, |lucene:///|)) ];
   }
   catch ParseError(_, _):
     return [];
});

@synopsis{Use a generated parser as a Lucene tokenizer, and collect only the open lexicals as separate tokens, skipping al keywords and punctuation.}
Tokenizer identifierTokenizerFromGrammar(type[&T <: Tree] grammar) = tokenizer(list[str] (str input) {
   try {
     return ["<token>" | token <- tokens(parse(grammar, input, |lucene:///|)), isLexical(token)];
   }
   catch ParseError(_, _):
     return [];
});

Tokenizer commentTokenizerFromGrammar(type[&T <: Tree] grammar) = tokenizer(list[str] (str input) {
   try {
     // first collect the layout rules which contain comments (marked with @category="Comment" in the grammar)
     return ["<a>" | token <- tokens(parse(grammar, input, |lucene:///|)), isComment(token), a <- token.args];
     // then collect the non-literal, non-whitespace words from those
     //return [ "<child>" | c <- comments, /Tree child := c, c has prod, /category("Comment") := c.prod];
   }
   catch ParseError(_, _):
     return [];
});

list[Tree] tokens(amb({Tree x, *_})) = tokens(x);
list[Tree] tokens(Tree tok:appl(prod(Symbol s, _, _), list[Tree] args)) = [tok]
  when isTokenType(s);

list[Tree] tokens(Tree tok:char(_)) = [tok];

default list[Tree] tokens(appl(_, list[Tree] args)) = [ *tokens(a) | a <- args];

bool isTokenType(lit(_)) = true;
bool isTokenType(cilit(_)) = true;    
bool isTokenType(lex(_)) = true;  
bool isTokenType(layouts(_)) = true;
bool isTokenType(label(Symbol s)) = isTokenType(s);
default bool isTokenType(Symbol _) = false;

bool isLexical(appl(prod(Symbol s, _, _), _)) = true when lex(_) := s || label(lex(_), _) := s;
default bool isLexical(Tree _) = false;

bool isComment(Tree t) = true when /Tree child := t, child has prod, /category("Comment") := child.prod;
default bool isComment(Tree _) = false;