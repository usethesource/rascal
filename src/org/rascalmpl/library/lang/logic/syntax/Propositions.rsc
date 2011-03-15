module lang::logic::syntax::Propositions

import lang::logic::syntax::Booleans;
import lang::std::Whitespace;
import lang::std::Layout;
import lang::std::Comment;

syntax Formula 
  = lex id: [A-Za-z][A-Za-z0-9]*
  ;