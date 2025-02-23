@license{
Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::agrammar::tests::LiteralsTests

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::agrammar::definition::Literals;
import lang::rascalcore::check::AType;

test bool tstLiteral1() = literal("") == prod(alit(""),[]);
test bool tstLiteral2() = literal("a") == prod(alit("a"),[\achar-class([arange(97,97)])]);
test bool tstLiteral3() = literal("ab") == 
	prod(alit("ab"),[\achar-class([arange(97,97)]),\achar-class([arange(98,98)])]);
	
test bool tstCiLiteral1() = ciliteral("") == prod(acilit(""),[]);
test bool tstCiLiteral2() = ciliteral("a") == prod(acilit("a"),[\achar-class([arange(97,97)])]);
test bool tstCiLiteral3() = ciliteral("ab") == 
	prod(acilit("ab"),[\achar-class([arange(97,97)]),\achar-class([arange(98,98)])]);

test bool tstStr2Syms1() = str2syms("") == [];
test bool tstStr2Syms2() = str2syms("a") == [\achar-class([arange(97,97)])];
test bool tstStr2Syms3() = str2syms("ab") == [\achar-class([arange(97,97)]),\achar-class([arange(98,98)])];

test bool tsCistr2syms1() = cistr2syms("") == [];
test bool tsCistr2syms2() = cistr2syms("a") == [\achar-class([arange(97,97)])];
test bool tsCistr2syms3() = cistr2syms("A") == [\achar-class([arange(65,65)])];

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

