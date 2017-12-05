module lang::std::tests::Layout

import lang::std::Layout;

test bool whitespaceOrComment1() = WhitespaceOrComment s := [WhitespaceOrComment] " ";

test bool whitespaceOrComment2() = WhitespaceOrComment s := [WhitespaceOrComment] "//xxx";