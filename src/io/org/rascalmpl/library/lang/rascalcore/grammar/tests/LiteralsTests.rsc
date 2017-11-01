module lang::rascalcore::grammar::tests::LiteralsTests

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::grammar::definition::Literals;
import ParseTree;

test bool tstLiteral1() = literal("") == prod(lit(""),[],{});
test bool tstLiteral2() = literal("a") == prod(lit("a"),[\char-class([range(97,97)])],{});
test bool tstLiteral3() = literal("ab") == 
	prod(lit("ab"),[\char-class([range(97,97)]),\char-class([range(98,98)])],{});
	
test bool tstCiLiteral1() = ciliteral("") == prod(cilit(""),[],{});
test bool tstCiLiteral2() = ciliteral("a") == prod(cilit("a"),[\char-class([range(97,97)])],{});
test bool tstCiLiteral3() = ciliteral("ab") == 
	prod(cilit("ab"),[\char-class([range(97,97)]),\char-class([range(98,98)])],{});

test bool tstStr2Syms1() = str2syms("") == [];
test bool tstStr2Syms2() = str2syms("a") == [\char-class([range(97,97)])];
test bool tstStr2Syms3() = str2syms("ab") == [\char-class([range(97,97)]),\char-class([range(98,98)])];

test bool tsCistr2syms1() = cistr2syms("") == [];
test bool tsCistr2syms2() = cistr2syms("a") == [\char-class([range(97,97)])];
test bool tsCistr2syms3() = cistr2syms("A") == [\char-class([range(65,65)])];

test bool tstUnescapeSC1() = unescape((StringConstant) `"a"`) == "a";
test bool tstUnescapeSC2() = unescape((StringConstant) `"\\t"`) == "\t";
test bool tstUnescapeSC3() = unescape((StringConstant) `"a\\tb"`) == "a\tb";
test bool tstUnescapeSC4() = unescape((StringConstant) `"\\'"`) == "\'";
test bool tstUnescapeSC5() = unescape((StringConstant) `"a\\tb\\'c"`) == "a\tb\'c";

test bool tstUnescapeCI1() = unescape((CaseInsensitiveStringConstant) `'a'`) == "a";
test bool tstUnescapeCI2() = unescape((CaseInsensitiveStringConstant) `'\\t'`) == "\t";
test bool tstUnescapeCI3() = unescape((CaseInsensitiveStringConstant) `'a\\tb'`) == "a\tb";
test bool tstUnescapeCI4() = unescape((CaseInsensitiveStringConstant) `'\\''`) == "\'";
test bool tstUnescapeCI5() = unescape((CaseInsensitiveStringConstant) `'a\\tb\\'c'`) == "a\tb\'c";

test bool tstUnescape1() = unescape("a") == "a";
test bool tstUnescape2() = unescape("\\t") == "\t";
test bool tstUnescape3() = unescape("a\\tb") == "a\tb";
test bool tstUnescape4() = unescape("\\\'") == "\'";
test bool tstUnescape5() = unescape("a\\tb\\\'c") == "a\tb\'c";

test bool tstCharacter1() = character((StringCharacter) `a`) == "a";
test bool tstCharacter2() = character((StringCharacter) `\\t`) == "\t";
test bool tstCharacter3() = character((StringCharacter) `\\\<`) == "\<";
test bool tstCharacter4() = character((StringCharacter) `\\'`) == "\'";

