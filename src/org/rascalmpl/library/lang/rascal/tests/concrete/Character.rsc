module lang::rascal::tests::concrete::Character

import ParseTree; 
import String;

lexical Example = ([A-Z] head [a-z]* tail)+ words;

test bool fieldsFromLexicals() = ["C", "C", "B", "F"] == [ "<w.head>" |  w <- t.words ]
  when Example t := [Example] "CamelCaseBaseFeest";
  
test bool fieldsFromLexicals2() = ["amel", "ase", "ase", "eest"] == [ "<w.tail>" |  w <- t.words ]
  when Example t := [Example] "CamelCaseBaseFeest";  

// even though the dynamic type would be list[[CCBF]] == list[[BCF]], the static type is list[[A-Z]]:
test bool staticFieldProjectType() = list[[A-Z]] _ := [ w.head |  w <- t.words ]
  when Example t := [Example] "CamelCaseBaseFeest";

private bool check(type[&T] _, value x) = &T _ := x;

test bool singleA() = check(#[A], char(65));
test bool singleB() = check(#[B], char(66));
test bool notSingleB() = !check(#[A], char(66));
test bool singleAB1() = check(#[A-B], char(65));
test bool singleAB2() = check(#[A-B], char(66));
test bool notSingleAB() = !check(#[A-B], char(67));

test bool charclassLUB() = set[[A-D]] _ := {char(65), char(66), char(67), char(68)};
test bool charclassLUB2() = set[[a-z]] _ := {char(i) | i <- [97..122]};

list[Tree] characters(str x) = [char(i) | i <- chars(x)];

list[![]] produceCharClass() = [ w.head |  w <- t.words ]
   when Example t := [Example] "CamelCaseBaseFeest";

test bool characterClassSubType() {
  [A-Z] head = char(65);
  [A-Za-z] tmp = head; // assignment into bigger class: always allowed
  
  if ([A-Z] _ := tmp) { // binding to smaller class should match if it fits
    return true;
  }
  
  return false;
}

test bool shortestRangesArePrinted() = "<#![]>" == "![]";
test bool complementOfNothingIsEverything() = (#![]).symbol == \char-class([range(1,0x10FFFF)]);
test bool charClassOrderedRanges() = (#[a-z A-Z]).symbol == \char-class([range(65,90),range(97,122)]);
test bool charClassMergedRanges() = (#[A-Z F-G]).symbol == \char-class([range(65,90)]);
test bool charClassExtendedRanges() = (#[A-M N-Z]).symbol == \char-class([range(65,90)]);

test bool asciiEscape() = \char-class([range(0,127)]) == #[\a00-\a7F].symbol;
test bool utf16Escape() = \char-class([range(0,65535)]) == #[\u0000-\uFFFF].symbol;
test bool utf24Escape() = \char-class([range(0,1114111)]) == #[\U000000-\U10FFFF].symbol;
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
test bool literalUtf16Escape() = lit("\n") == #"\u000A".symbol;
test bool literalUtf24Escape1() = lit("\n") == #"\U00000A".symbol;
test bool literalUtf24Escape2() = lit("🍕") == #"\U01F355".symbol;

// ambiguity in this syntax must be resolved first
//test bool differenceCC() = (#[a-zA-Z] - [A-Z]).symbol == (#[a-z]).symbol;
//test bool unionCC()      = (#[a-z] || [A-Z]).symbol == (#[A-Za-z]).symbol;
//test bool intersectCC()  = (#[A-Za-z] && [A-Z]).symbol == (#[A-Z]).symbol;
