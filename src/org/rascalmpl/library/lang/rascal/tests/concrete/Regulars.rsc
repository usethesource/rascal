module lang::rascal::tests::concrete::Regulars

import IO;

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

lexical Name = [a-z]+;

import ParseTree;

// null hypothesis, normal non-terminals still work
test bool normalSort()
    = A _ := (A) `a`;

test bool normalLex()
    = Name _ := (Name) `daffyduck`;

// star list

test bool starListEmpty() 
    = A* _ := (A*) ``;

test bool starListSingle() 
    = A* _ := (A*) `a`;

test bool starListMany() 
    = A* _ := (A*) `aa`;

test bool starListCompanionSingle() 
    = A+ _ := (A+) `a`;

test bool starListCompanionMany() 
    = A+ _ := (A+) `aa`;

test bool starListWithElementHole()
    = (A*) `a<A _>a` := (A*) `aaa`;

test bool starListWithSubListHoleEmpty()
    = (A*) `a<A* _>a` := (A*) `aa`;

// @ignore{buggy}
// test bool starListWithSubListHoleNonEmpty()
//     = (A*) `a<A+ _>a` := (A*) `aaa`;

// plus list 

test bool plusListSingle() 
    = A+ _ := (A+) `a`;

test bool plusListMany() 
    = A+ _ := (A+) `aa`;

test bool plusListCompanionEmpty() 
    = A* _ := (A*) ``;

test bool plusListCompanionSingle() 
    = A+ _ := (A*) `a`;

test bool plusListCompanionMany() 
    = A+ _ := (A*) `aa`;

// opt

test bool optAbsent()
    = (A?) _ := (A?) ``;

test bool optPresent()
    = (A?) _ := (A?) `a`;

test bool optHole()
    = (A?) `<A _>` := (A?) `a`;

// empty

// test bool empty()
//     = (()) _ := (()) ``;

// alt
test bool altA() 
    = (A|B) _ := ((A|B)) `a`;

test bool altB() 
    = (A|B) _ := ((A|B)) `b`;

test bool altHoleA()
    = ((A|B)) `<A _>` := ((A|B)) `a`;

test bool altHoleB()
    = ((A|B)) `<B _>` := ((A|B)) `b`;

// seq

// test bool seqAB() 
//     = (A B) _ := ((A B)) `ab`;

// test bool seqHoleAB()
//     = ((A B)) `<A _><B _>` := ((A B)) `ab`;

