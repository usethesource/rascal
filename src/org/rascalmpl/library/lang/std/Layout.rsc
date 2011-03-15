module lang::std::Layout

import lang::std::Whitespace;
import lang::std::Comment;

layout Standard 
  = WhitespaceOrComment*;
  
syntax WhitespaceOrComment 
  = whitespace: Whitespace
  | comment: Comment
  ; 