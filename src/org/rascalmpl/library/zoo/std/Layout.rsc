module Layout

import zoo::std::Whitespace;
import zoo::std::Comment;

layout Standard = WhitespaceOrComment*;

syntax WhitespaceOrComment = whitespace: Whitespace
                           | comment: Comment
                           ; 