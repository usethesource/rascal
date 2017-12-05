module lang::std::tests::ASCII

import lang::std::ASCII;


lexical Ascii = [\a00-\a7f];

test bool ascii1() = Ascii a := [Ascii] "a";

test bool alnum1() = Alnum a := [Alnum] "A";
test bool alnum2() = Alnum a := [Alnum] "a";
test bool alnum3() = Alnum a := [Alnum] "0";

test bool alpha1() = Alpha a := [Alpha] "A";
test bool alpha2() = Alpha a := [Alpha] "a";

test bool upper1() = Upper u := [Upper] "A";

test bool lower1() = Lower l := [Lower] "a";

test bool blank1() = Blank b := [Blank] " ";
test bool blank2() = Blank b := [Blank] "\t";

test bool ctrl1() = Ctrl c := [Ctrl] "\a00";
test bool ctrl2() = Ctrl c := [Ctrl] "\a1f";

@ignore{}
test bool ctrl3() = Ctrl c := [Ctrl] "\a7f";

test bool digit1() = Digit d := [Digit] "0";

//lexical Graph = [~-!];

test bool hexnumber1() = HexNumber h := [HexNumber] "A";
test bool hexnumber2() = HexNumber h := [HexNumber] "F";
test bool hexnumber3() = HexNumber h := [HexNumber] "a";
test bool hexnumber4() = HexNumber h := [HexNumber] "f";

test bool hexnumber5() = HexNumber h := [HexNumber] "0";
test bool hexnumber6() = HexNumber h := [HexNumber] "9";

@expected{ParseError}
test bool hexnumber1() = HexNumber h := [HexNumber] "G";

@expected{ParseError}
test bool hexnumber1() = HexNumber h := [HexNumber] "g";

//lexical Print = [\ ~-!];
//
//lexical Punt = [!-/ :-@ \[-~];

test bool space1() = Space s := [Space] "\t";
test bool space1() = Space s := [Space] "\n";
test bool space1() = Space s := [Space] "\a0B";
test bool space1() = Space s := [Space] "\a0C";
test bool space1() = Space s := [Space] "\r";
test bool space1() = Space s := [Space] " ";
