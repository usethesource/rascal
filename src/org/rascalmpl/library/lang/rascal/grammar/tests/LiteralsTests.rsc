module lang::rascal::grammar::tests::LiteralsTests

test bool tstStr2Syms0() = str2syms("") == [];
test bool tstStr2Syms1() = str2syms("a") == [\char-class([range(97,97)])];
test bool tstStr2Syms2() = str2syms("ab") == [\char-class([range(97,97)]),\char-class([range(98,98)])];

test bool tsCistr2syms0() = cistr2syms("") == [];
test bool tsCistr2syms1() = cistr2syms("a") == [\char-class([range(97,97)])];
test bool tsCistr2syms2() = cistr2syms("A") == [\char-class([range(65,65)])];

test bool tstUnescape1() = unescape("a\\tb") == "a\tb";
test bool tstUnescape2() = unescape("a\\tb\\\'c") == "a\tb\'c";