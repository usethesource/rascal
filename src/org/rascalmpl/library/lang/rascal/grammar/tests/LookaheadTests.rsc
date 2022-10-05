module lang::rascal::grammar::tests::LookaheadTests

import Grammar;
import lang::rascal::grammar::definition::Regular;
import lang::rascal::grammar::definition::Characters;
import lang::rascal::grammar::definition::Productions;
import ParseTree;
extend  lang::rascal::grammar::Lookahead;

// -------- Examples and tests -------------------

public Grammar G0 = simple({sort("S")}, {});

test bool testEmpty1() = first(G0) == ();

test bool testEmpty2() = firstAndFollow(G0) == <(), (sort("S"):{eoi()})>;

private Production pr(Symbol rhs, list[Symbol] lhs) {
  return prod(rhs,lhs,{});
}

public Grammar Lit1 = simple({}, {
  pr(lit("*"),[\char-class([range(42,42)])]),
    pr(lit("+"),[\char-class([range(43,43)])]),
    pr(lit("0"),[\char-class([range(48,48)])]),
    pr(lit("1"),[\char-class([range(49,49)])])
});

public Grammar G1 = simple({sort("E")},
{
    pr(sort("E"), [sort("E"), lit("*"), sort("B")]),
    pr(sort("E"), [sort("E"), lit("+"), sort("B")]),
    pr(sort("E"), [sort("B")]),
    pr(sort("B"), [lit("0")]),
    pr(sort("B"), [lit("1")])
} + Lit1.productions);

test bool testUsed1()  = usedSymbols(G1) >= {lit("0"),lit("1"),sort("E"),sort("B"),lit("*"),lit("+")};

test bool testDefined1() = definedSymbols(G1) == {sort("E"),sort("B"),lit("+"),lit("*"),lit("0"),lit("1")};

test bool testStartsDefined() = G1.starts < definedSymbols(G1);

public SymbolUse firstLit1 = (
  lit("0"):{\char-class([range(48,48)])},
  lit("1"):{\char-class([range(49,49)])},
  lit("*"):{\char-class([range(42,42)])},
  lit("+"):{\char-class([range(43,43)])}
);

test bool first1() = SymbolUse F := first(G1) 
     && F[sort("E")] == {\char-class([range(49,49)]),\char-class([range(48,48)])}
     && F[sort("B")] == {\char-class([range(49,49)]),\char-class([range(48,48)])}
     ;
                       
public Grammar G2 = simple({sort("E")},
{
    pr(sort("E"), [sort("E"), lit("*"), sort("B")]),
    pr(sort("E"), [sort("E"), lit("+"), sort("B")]),
    pr(sort("E"), [sort("B")]),
    pr(sort("B"), [lit("0")]),
    pr(sort("B"), [lit("1")])
} + Lit1.productions);

test bool first2() = SymbolUse F := first(G2)
     && F[sort("E")] == {\char-class([range(48,48)]),\char-class([range(49,49)])}
     && F[sort("B")] == {\char-class([range(48,48)]),\char-class([range(49,49)])}
     ;

public Grammar G3 = simple( {sort("E")},
{
    pr(sort("E"),  [sort("T"), sort("E1")]),
    pr(sort("E1"), [lit("+"), sort("T"), sort("E1")]),
    pr(lit("+"), [\char-class([range(43,43)])]),
    pr(sort("E1"), []),
    pr(sort("T"),  [sort("F"), sort("T1")]),
    pr(sort("T1"), [lit("*"), sort("F"), sort("T1")]),
    pr(lit("*"), [\char-class([range(42,42)])]),
    pr(sort("T1"), []),
    pr(sort("F"),  [lit("("), sort("E"), lit(")")]),
    pr(lit("("), [\char-class([range(40,40)])]),
    pr(lit(")"), [\char-class([range(41,41)])]),
    pr(sort("F"),  [lit("id")]),
    pr(lit("id"), [\char-class([range(105,105)]),\char-class([range(100,100)])])
});

private SymbolUse F3 = first(G3);

test bool tF31() = F3[sort("F")] == {\char-class([range(105,105)]),\char-class([range(40,40)])};
test bool tF32() = F3[sort("T")] == F3[sort("F")];
test bool tF33() = F3[sort("E")] == F3[sort("T")];
test bool tF34() = F3[lit("*")] == {\char-class([range(42,42)])};
test bool tF35() = F3[lit("+")] == {\char-class([range(43,43)])};
test bool tF36() = F3[lit("id")] == {\char-class([range(105,105)])};
test bool tF37() = F3[sort("E1")] == {empty()} + F3[lit("+")];
test bool tF38() = F3[sort("T1")] == {empty()} + F3[lit("*")];
test bool tF39() = F3[lit("(")] == {\char-class([range(40,40)])};
test bool tF310() = F3[lit(")")] == {\char-class([range(41,41)])};
      
public SymbolUse Fol3() = follow(G3, first(G3));
 
test bool tFol31() = Fol3()[sort("E")] == {\char-class([range(41,41)]), eoi()};
test bool tFol32() = Fol3()[sort("E1")] == {\char-class([range(41,41)]), eoi()};
test bool tFol33() = Fol3()[sort("T")] == {\char-class([range(43,43)]),\char-class([range(41,41)]),eoi()};
test bool tFol34() = Fol3()[sort("T1")] == {\char-class([range(43,43)]),\char-class([range(41,41)]),eoi()};
test bool tFol35() = Fol3()[sort("F")] == {\char-class([range(43,43)]),\char-class([range(42,42)]),\char-class([range(41,41)]),eoi()};
       
public Grammar Session = simple({sort("Session")},
{
    pr(sort("Session"), [sort("Facts"), sort("Question")]),
    pr(sort("Session"), [lit("("), sort("Session"), lit(")"), sort("Session")]),
    pr(lit("("), [\char-class([range(40,40)])]),
    pr(lit(")"), [\char-class([range(41,41)])]),
    pr(sort("Facts"),   [sort("Fact"), sort("Facts")]),
    pr(sort("Facts"),   []),
    pr(sort("Fact"),    [lit("!"), sort("STRING")]),
    pr(lit("!"), [\char-class([range(33,33)])]),
    pr(sort("Question"),[lit("?"), sort("STRING")]),
    pr(lit("?"), [\char-class([range(63,63)])]),
    pr(sort("STRING"),  [lit("a")]),
    pr(lit("a"), [\char-class([range(97,97)])])
});

private SymbolUse SF = first(Session);

test bool tSF1() = SF[sort("Question")] == {\char-class([range(63,63)])};
test bool tSF2() = SF[sort("Session")] == {\char-class([range(33,33)]),\char-class([range(40,40)]),\char-class([range(63,63)])};
test bool tSF3() = SF[sort("Facts")] == {\char-class([range(33,33)]),empty()};
test bool tSF4() = SF[lit("a")] == {\char-class([range(97,97)])};
test bool tSF5() = SF[lit("!")] == {\char-class([range(33,33)])};
test bool tSF6() = SF[lit("?")] == {\char-class([range(63,63)])};
test bool tSF7() = SF[lit("(")] == {\char-class([range(40,40)])};
test bool tSF8() = SF[lit(")")] == {\char-class([range(41,41)])};
test bool tSF9() = SF[sort("STRING")] == {\char-class([range(97,97)])};
test bool tSF10() = SF[sort("Fact")] == {\char-class([range(33,33)])};
     
test bool testFollow() = follow(Session, first(Session)) >=     
     (
  lit(")"):{
    \char-class([range(63,63)]),
    \char-class([range(40,40)]),
    \char-class([range(33,33)])
  },
  lit("("):{
    \char-class([range(63,63)]),
    \char-class([range(40,40)]),
    \char-class([range(33,33)])
  },
  sort("STRING"):{
    \char-class([range(63,63)]),
    eoi(),
    \char-class([range(41,41)]),
    \char-class([range(33,33)])
  },
  sort("Session"):{
    eoi(),
    \char-class([range(41,41)])
  },
  lit("?"):{\char-class([range(97,97)])},
  sort("Fact"):{
    \char-class([range(63,63)]),
    \char-class([range(33,33)])
  },
  sort("Facts"):{\char-class([range(63,63)])},
  sort("Question"):{
    eoi(),
    \char-class([range(41,41)])
  },
  lit("!"):{\char-class([range(97,97)])},
  lit("a"):{
    \char-class([range(63,63)]),
    eoi(),
    \char-class([range(41,41)]),
    \char-class([range(33,33)])
  }
);
