module lang::std::tests::Id

import lang::std::Id;

test bool id1() = Id x := [Id] "a";
test bool id2() = Id x := [Id] "A";
test bool id3() = Id x := [Id] "aA1";

@expected{ParseError}
test bool id3() = Id x := [Id] "1aA";