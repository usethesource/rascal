module lang::std::tests::ASCII

import lang::std::ASCII;
import Exception;


lexical Ascii = [\a00-\a7f];

test bool ascii1() = Ascii _ := [Ascii] "a";

test bool alnum1() = Alnum _ := [Alnum] "A";
test bool alnum2() = Alnum _ := [Alnum] "a";
test bool alnum3() = Alnum _ := [Alnum] "0";

test bool alpha1() = Alpha _ := [Alpha] "A";
test bool alpha2() = Alpha _ := [Alpha] "a";

test bool upper1() = Upper _ := [Upper] "A";

test bool lower1() = Lower _ := [Lower] "a";

test bool blank1() = Blank _ := [Blank] " ";
test bool blank2() = Blank _ := [Blank] "\t";

test bool ctrl1() = Ctrl _ := [Ctrl] "\a00";
test bool ctrl2() = Ctrl _ := [Ctrl] "\a1f";

@ignore{}
test bool ctrl3() = Ctrl c := [Ctrl] "\a7f";

test bool digit1() = Digit _ := [Digit] "0";

//lexical Graph = [~-!];

test bool hexnumber1() = HexNumber _ := [HexNumber] "A";
test bool hexnumber2() = HexNumber _ := [HexNumber] "F";
test bool hexnumber3() = HexNumber _ := [HexNumber] "a";
test bool hexnumber4() = HexNumber _ := [HexNumber] "f";

test bool hexnumber5() = HexNumber _ := [HexNumber] "0";
test bool hexnumber6() = HexNumber _ := [HexNumber] "9";

@expected{ParseError}
test bool hexnumber7() = HexNumber _ := [HexNumber] "G";

@expected{ParseError}
test bool hexnumber8() = HexNumber _ := [HexNumber] "g";

//lexical Print = [\ ~-!];
//
//lexical Punt = [!-/ :-@ \[-~];

test bool space1() = Space _ := [Space] "\t";
test bool space2() = Space _ := [Space] "\n";
test bool space3() = Space _ := [Space] "\a0B";
test bool space4() = Space _ := [Space] "\a0C";
test bool space5() = Space _ := [Space] "\r";
test bool space6() = Space _ := [Space] " ";
