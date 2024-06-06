module lang::rascal::grammar::tests::LiteralsTests

import lang::rascal::\syntax::Rascal;
import lang::rascal::grammar::definition::Literals;
import ParseTree;

test bool tstLiteral1() = literal("") == prod(lit(""),[],{});
test bool tstLiteral2() = literal("a") == prod(lit("a"),[\char-class([range(97,97)])],{});
test bool tstLiteral3() = literal("ab") == 
	prod(lit("ab"),[\char-class([range(97,97)]),\char-class([range(98,98)])],{});
	
test bool tstCiLiteral1() = ciliteral("") == prod(cilit(""),[],{});
test bool tstCiLiteral2() = ciliteral("a") == prod(cilit("a"),[\char-class([range(97,97), range(65,65)])],{});
test bool tstCiLiteral3() = ciliteral("ab") == 
	prod(cilit("ab"),[\char-class([range(97,97), range(65,65)]),\char-class([range(98,98),range(66,66)])],{});

test bool tstStr2Syms1() = str2syms("") == [];
test bool tstStr2Syms2() = str2syms("a") == [\char-class([range(97,97)])];
test bool tstStr2Syms3() = str2syms("ab") == [\char-class([range(97,97)]),\char-class([range(98,98)])];

test bool tsCistr2syms1() = cistr2syms("") == [];
test bool tsCistr2syms2() = cistr2syms("a") == [\char-class([range(97,97), range(65,65)])];
test bool tsCistr2syms3() = cistr2syms("A") == [\char-class([range(65,65), range(97,97)])];

test bool tstUnescapeSC1() = unescapeLiteral((StringConstant) `"a"`) == "a";
test bool tstUnescapeSC2() = unescapeLiteral((StringConstant) `"\\t"`) == "\t";
test bool tstUnescapeSC3() = unescapeLiteral((StringConstant) `"a\\tb"`) == "a\tb";
test bool tstUnescapeSC4() = unescapeLiteral((StringConstant) `"\\'"`) == "\'";
test bool tstUnescapeSC5() = unescapeLiteral((StringConstant) `"a\\tb\\'c"`) == "a\tb\'c";

test bool tstUnescapeCI1() = unescapeLiteral((CaseInsensitiveStringConstant) `'a'`) == "a";
test bool tstUnescapeCI2() = unescapeLiteral((CaseInsensitiveStringConstant) `'\\t'`) == "\t";
test bool tstUnescapeCI3() = unescapeLiteral((CaseInsensitiveStringConstant) `'a\\tb'`) == "a\tb";
test bool tstUnescapeCI4() = unescapeLiteral((CaseInsensitiveStringConstant) `'\\''`) == "\'";
test bool tstUnescapeCI5() = unescapeLiteral((CaseInsensitiveStringConstant) `'a\\tb\\'c'`) == "a\tb\'c";

test bool tstUnescape1() = unescapeLiteral("a") == "a";
test bool tstUnescape2() = unescapeLiteral("\\t") == "\t";
test bool tstUnescape3() = unescapeLiteral("a\\tb") == "a\tb";
test bool tstUnescape4() = unescapeLiteral("\\\'") == "\'";
test bool tstUnescape5() = unescapeLiteral("a\\tb\\\'c") == "a\tb\'c";

test bool tstCharacter1() = character((StringCharacter) `a`) == "a";
test bool tstCharacter2() = character((StringCharacter) `\\t`) == "\t";
test bool tstCharacter3() = character((StringCharacter) `\\\<`) == "\<";
test bool tstCharacter4() = character((StringCharacter) `\\'`) == "\'";

