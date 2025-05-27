module lang::rascal::grammar::tests::CharactersTests

import lang::rascal::grammar::definition::Characters;
import ParseTree;
import String;

test bool testFlip() = \new-char-class([range(2,2), range(1,1)]) == \char-class([range(1,2)]);
test bool testMerge() = \new-char-class([range(3,4), range(2,2), range(1,1)]) == \char-class([range(1,4)]);
test bool testEnvelop() = \new-char-class([range(10,20), range(15,20), range(20,30)]) == \char-class([range(10,30)]);
test bool testEnvelop2() = \new-char-class([range(10,20), range(10,19), range(20,30)]) == \char-class([range(10,30)]);

test bool testComp() = complement(\char-class([])) == \char-class([range(1,1114111)]);
test bool testComp2() = complement(\char-class([range(0,0)])) == \char-class([range(1,1114111)]);
test bool testComp3() = complement(\char-class([range(1,1)])) == \char-class([range(2,1114111)]);
test bool testComp4() = complement(\char-class([range(10,20), range(30,40)])) == \char-class([range(1,9),range(21,29),range(41,1114111)]);
test bool testComp5() = complement(\char-class([range(10,35), range(30,40)])) == \char-class([range(1,9),range(41,1114111)]);

test bool testUnion1() = union(\char-class([range(10,20)]), \char-class([range(30, 40)])) == \char-class([range(10,20), range(30,40)]);
test bool testUnion2() = union(\char-class([range(10,25)]), \char-class([range(20, 40)])) == \char-class([range(10,40)]);
 
test bool testInter1() = intersection(\char-class([range(10,20)]), \char-class([range(30, 40)])) == \char-class([]);
test bool testInter2() = intersection(\char-class([range(10,25)]), \char-class([range(20, 40)])) == \char-class([range(20, 25)]);

test bool testDiff1() = difference(\char-class([range(10,30)]), \char-class([range(20,25)])) == \char-class([range(10,19), range(26,30)]);
test bool testDiff2() = difference(\char-class([range(10,30), range(40,50)]), \char-class([range(25,45)])) ==\char-class( [range(10,24), range(46,50)]);


test bool asciiEscape() = \char-class([range(0,127)]) == #[\a00-\a7F].symbol;
test bool utf16Escape() = \char-class([range(0,65535)]) == #[\u0000-\uFFFF].symbol;
test bool utf32Escape() = \char-class([range(0,1114111)]) == #[\U000000-\U10FFFF].symbol;
test bool highLowSurrogateRange1() = \char-class([range(9312,12991)]) == #[①-㊿].symbol;
test bool highLowSurrogateRange2() = \char-class([range(127829,127829)]) == #[🍕].symbol;
test bool differentEscapesSameResult1() = #[\a00-\a7F] == #[\u0000-\u007F];
test bool differentEscapesSameResult2() = #[\a00-\a7F] == #[\U000000-\U00007F];

/* to avoid a known ambiguity */
alias NotAZ = ![A-Z];

test bool unicodeCharacterClassSubtype1() {
  Tree t = char(charAt("⑭", 0));

  if ([①-㊿] circled := t) {
    assert [⑭] _ := circled;
    assert NotAZ _ := circled;
    return true;
  }

  return false;
}

test bool unicodeCharacterClassSubtype2() {
  Tree t = char(charAt("🍕", 0));

  if ([🍕] pizza := t) {
    assert [\a00-🍕] _ := pizza;
    assert NotAZ _ := pizza;
    return true;
  }

  return false;
}

test bool literalAsciiEscape1() = lit("\n") == #"\a0A".symbol;
test bool literalAsciiEscape2() = lit("w") == #"\a77".symbol;
test bool literalAsciiEscape3() = lit("\f") == #"\a0C".symbol;
test bool literalAsciiEscape4() = lit("\n") == #"\n".symbol;
@ignore{vallang must re-introduce the \f notation}
test bool literalAsciiEscape5() = lit("\f") == #"\f".symbol;
test bool literalUtf16Escape() = lit("\n") == #"\u000A".symbol;
test bool literalUtf32Escape1() = lit("\n") == #"\U00000A".symbol;
test bool literalUtf32Escape2() = lit("🍕") == #"\U01F355".symbol;

test bool ciliteralAsciiEscape1() = cilit("\n") == #'\a0A'.symbol;
test bool ciliteralAsciiEscape2() = cilit("w") == #'\a77'.symbol;
test bool ciliteralAsciiEscape3() = cilit("\f") == #'\a0C'.symbol;
test bool ciliteralAsciiEscape4() = cilit("\n") == #'\n'.symbol;
@ignore{vallang must re-introduce the \f notation}
test bool ciliteralAsciiEscape5() = cilit("\f") == #'\f'.symbol;
test bool ciliteralUtf16Escape() = cilit("\n") == #'\u000A'.symbol;
test bool ciliteralUtf32Escape1() = cilit("\n") == #'\U00000A'.symbol;
test bool ciliteralUtf32Escape2() = cilit("🍕") == #'\U01F355'.symbol;
