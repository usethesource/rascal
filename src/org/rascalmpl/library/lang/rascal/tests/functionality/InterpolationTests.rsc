module lang::rascal::tests::functionality::InterpolationTests

test bool interpolateWhile()  { x = 10; return "<while (x > 0) {> <{x -= 1; x; }> <}>" == " 9  8  7  6  5  4  3  2  1  0 "; }
    
test bool interpolateDoWhile() { x = 10; return "<do {> <{x -= 1; x; }> <} while (x > 0)>" == " 9  8  7  6  5  4  3  2  1  0 "; }

test bool interpolateIfThenElse() = "abc <if (1 > 0) {> GT <} else {> LT <}> cde" == "abc  GT  cde"; 
    
test bool interpolateIfThenTrue() = "abc <if (1 > 0) {> GT <}> cde" == "abc  GT  cde";

test bool interpolateIfThenFalse() = "abc <if (0 > 0) {> GT <}> cde" == "abc  cde";

test bool interpolateFor() = "abc <for (i <- [1,2,3]) {> print <i> <}> cde" == "abc  print 1  print 2  print 3  cde";

test bool interpolateForNested() = 
    "<for (x <- [1,2,3]) {>outer <x> <for (y <- [4,5,6]) {>inner <x>,<y> <}><}>" ==
    "outer 1 inner 1,4 inner 1,5 inner 1,6 outer 2 inner 2,4 inner 2,5 inner 2,6 outer 3 inner 3,4 inner 3,5 inner 3,6 ";

test bool interpolatePreFor() = "<for (i <- [1,2,3]) { j = i + 1;> <j> <}>" == " 2  3  4 ";

test bool interpolatePostWhile() { x = 5; return "<while (x > 0) {> <x> < x -= 1; }>" == " 5  4  3  2  1 ";}

test bool interpolateFor2() {
    str s = "<for(i <- [0..10]) {>
            '    <if(i % 2 == 0) {>
            '        i = <i>
            '    <}>
            '<}>";
    return s ==
    "\n    \n        i = 0\n    \n\n    \n\n    \n        i = 2\n    \n\n    \n\n    \n        i = 4\n    \n\n    \n\n    \n        i = 6\n    \n\n    \n\n    \n        i = 8\n    \n\n    \n";    
}

test bool interpolateQuotes() {
    str s = " \" <0 + 0> \" ";
    return s == " \" 0 \" ";   
}

test bool interpolateEsc1() { A = "A"; return "\t<A>" == "\tA"; }
test bool interpolateEsc2() { A = "A"; return "<A>\t" == "A\t"; }
test bool interpolateEsc3() { A = "A"; return "<A>\t<A>" == "A\tA"; }

test bool interpolateEsc4() { A = "A"; return "\n<A>" == "\nA"; }
test bool interpolateEsc5() { A = "A"; return "<A>\n" == "A\n"; }
test bool interpolateEsc6() { A = "A"; return "<A>\n<A>" == "A\n<A>"; }

test bool interpolateEsc7() { A = "A"; return "\"<A>" == "\"A"; }
test bool interpolateEsc8() { A = "A"; return "<A>\"" == "A\""; }
test bool interpolateEsc9() { A = "A"; return "<A>\"<A>" == "A\"<A>"; }

test bool interpolateEsc10() { A = "A"; return "\\<A>" == "\\A"; }
test bool interpolateEsc11() { A = "A"; return "<A>\\" == "A\\"; }
test bool interpolateEsc12() { A = "A"; return "<A>\\<A>" == "A\\<A>"; }

test bool interpolateEsc13() { A = "A"; return "\u0000<A>" == "\u0000A"; }
test bool interpolateEsc14() { A = "A"; return "<A>\u0000" == "A\u0000"; }
test bool interpolateEsc15() { A = "A"; return "<A>\u0000<A>" == "A\u0000A"; }

test bool interpolateEsc16() { A = "A"; return "\a20<A>" == " A"; }
test bool interpolateEsc17() { A = "A"; return "<A>\a20" == "A "; }
test bool interpolateEsc18() { A = "A"; return "<A>\a20<A>" == "A A"; }

test bool interpolateEsc19() { A = "A"; return "\U01F35D<A>" == "🍝A"; }
test bool interpolateEsc20() { A = "A"; return "<A>\U01F35D " == "A🍝 "; }
test bool interpolateEsc21() { A = "A"; return "<A>\U01F35D <A>" == "A🍝 A"; }

test bool interpolateEsc22() { A = "A"; return "\u2713<A>" == "✓A"; }
test bool interpolateEsc23() { A = "A"; return "<A>\u2713" == "A✓"; }
test bool interpolateEsc24() { A = "A"; return "<A>\u2713<A>" == "A✓A"; }

test bool interpolateEsc25() { A = "A\tB"; return "x<A>z" == "xA\tBz"; }
test bool interpolateEsc26() { A = "A\\tB"; return "x<A>z" == "xA\\tBz"; }



