module lang::basic::Layout

import zoo::basic::Whitespace;
import zoo::basic::Comment;

layout Standard = WhitespaceOrComment*;

syntax WhitespaceOrComment = whitespace: Whitespace
                           | comment: Comment
                           ; 