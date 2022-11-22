module lang::std::tests::Layout

import lang::std::Layout;

test bool whitespaceOrComment1() = WhitespaceOrComment _ := [WhitespaceOrComment] " ";

test bool whitespaceOrComment2() = WhitespaceOrComment _ := [WhitespaceOrComment] "//xxx";
