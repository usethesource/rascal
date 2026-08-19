module lang::rascal::tests::functionality::ParsingRegulars

syntax A = "a";
syntax B = "b";

syntax AStarList = A*;
syntax BPlusList = B+;

syntax ASepStarList = {A ","}*;
syntax BSepPlusList = {B ","}*;

syntax AOpt = A?;

syntax Empty = () ();

syntax AAltB = (A | B);

syntax ASeqB = (A B);

import ParseTree;

// star list

test bool starListEmpty() 
    = A* _ := parse(#(A*), "");

test bool starListSingle() 
    = A* _ := parse(#(A*), "a");

test bool starListMany() 
    = A* _ := parse(#(A*), "aa");

test bool starListCompanionSingle() 
    = A+ _ := parse(#(A+), "a");

test bool starListCompanionMany() 
    = A+ _ := parse(#(A+), "a");

// plus list 

test bool plusListSingle() 
    = A+ _ := parse(#(A+), "a");

test bool plusListMany() 
    = A+ _ := parse(#(A+), "aa");

test bool plusListCompanionEmpty() 
    = A* _ := parse(#(A*), "");

test bool plusListCompanionSingle() 
    = A+ _ := parse(#(A*), "a");

test bool plusListCompanionMany() 
    = A+ _ := parse(#(A*), "a");

// opt

test bool optAbsent()
    = (A?) _ := parse(#(A?), "");

test bool optPresent()
    = (A?) _ := parse(#(A?), "a");

// empty

test bool empty()
    = (()) _ := parse(#(), "");

// alt
test bool altA() 
    = (A|B) _ := parse(#(A|B), "a");

test bool altB() 
    = (A|B) _ := parse(#(A|B), "b");

// seq

test bool seqAB() 
    = (A B) _ := parse(#(A B), "ab");

