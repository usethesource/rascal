module experiments::Compiler::Examples::Tst5

test bool interpolateEsc1() { A = "A"; return "\t<A>" == "\tA"; }


//value main(){ map [int, str] m = (1:"A", 2:"B", 3:"C", 1:"D"); return m; }

test bool interpolateEsc2() { A = "A"; return "<A>\t" == "A\t"; }

test bool interpolateEsc3() { A = "A"; return "\n<A>" == "\nA"; }
test bool interpolateEsc4() { A = "A"; return "<A>\n" == "A\n"; }

test bool interpolateEsc5() { A = "A"; return "\u0000<A>" == "\u0000A"; }
value main() { A = "A"; return "\u0000<A>"/* == "\u0000A"*/; }

test bool interpolateEsc6() { A = "A"; return "<A>\u0000" == "A\u0000"; }

test bool interpolateEsc7() { A = "A"; return "\a20<A>" == " A"; }
test bool interpolateEsc8() { A = "A"; return "<A>\a20" == "A "; }

test bool interpolateEsc9() { A = "A"; return "\U01F35D<A>" == "🍝A"; }
test bool interpolateEsc10() { A = "A"; return "<A>\U01F35D " == "A🍝 "; }

test bool interpolateEsc11() { A = "A"; return "\u2713<A>" == "✓A"; }
test bool interpolateEsc12() { A = "A"; return "<A>\u2713" == "A✓"; }

test bool interpolateEsc13() { A = "A\tB"; return "x<A>z" == "xA\tBz"; }
test bool interpolateEsc14() { A = "A\\tB"; return "x<A>z" == "xA\\tBz"; }

