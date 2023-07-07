module lang::std::tests::Id

import lang::std::Id;
import Exception;

test bool id1() = Id _ := [Id] "a";
test bool id2() = Id _ := [Id] "A";
test bool id3() = Id _ := [Id] "aA1";

@expected{
ParseError
}
test bool id4() = Id _ := [Id] "1aA";
