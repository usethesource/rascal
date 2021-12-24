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

// ambiguity in this syntax must be resolved first
//test bool differenceCC() = (#[a-zA-Z] - [A-Z]).symbol == (#[a-z]).symbol;
//test bool unionCC()      = (#[a-z] || [A-Z]).symbol == (#[A-Za-z]).symbol;
//test bool intersectCC()  = (#[A-Za-z] && [A-Z]).symbol == (#[A-Z]).symbol;
