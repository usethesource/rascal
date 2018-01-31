module lang::rascalcore::grammar::tests::ParserGeneratorTests
 
import lang::rascalcore::grammar::ParserGenerator;
import lang::rascalcore::grammar::definition::Grammar;
import lang::rascalcore::grammar::definition::Parameters;
import lang::rascalcore::grammar::definition::Literals;
import IO;
import String;
import List;
import Set;
import lang::rascalcore::check::AType;
import util::Reflective;

import lang::rascalcore::grammar::tests::TestGrammars;

// -------- Examples and tests -------------------


public AGrammar GEMPTY = grammar({sort("S")}, ());

private AProduction pr(AType rhs, list[AType] lhs) {
  return prod(rhs,lhs);
}

public AGrammar G0 = grammar({sort("S")}, (
    sort("S"): choice(sort("S"), { pr(sort("S"), [ lit("0") ]) }),
    lit("0"): choice(lit("0"), { pr(lit("0"),[\char-class([range(48,48)])]) })
));

public map[AType sort, AProduction def] Lit1 = (
  lit("*"): choice(lit("*"), { pr(lit("*"),[\char-class([range(42,42)])]) }),
  lit("+"): choice(lit("+"), { pr(lit("+"),[\char-class([range(43,43)])]) }),
  lit("0"): choice(lit("0"), { pr(lit("0"),[\char-class([range(48,48)])]) }),
  lit("1"): choice(lit("1"), { pr(lit("1"),[\char-class([range(49,49)])]) })
);

public AGrammar GEXP = grammar({sort("E")}, (
    sort("E"): choice(sort("E"), { pr(sort("E"), [sort("E"), lit("*"), sort("B")]),
                                   pr(sort("E"), [sort("E"), lit("+"), sort("B")]),
                                   pr(sort("E"), [sort("B")])
                                 }),
    sort("B"): choice(sort("B"), { pr(sort("B"), [lit("0")]),
                                  pr(sort("B"), [lit("1")])
                                 })
) + Lit1);

public AGrammar GEXPPRIO = grammar( {sort("E")},
(
    sort("E"):  choice(sort("E"), { pr(sort("E"),  [sort("T"), sort("E1")])}),
    sort("E1"): choice(sort("E1"),{ pr(sort("E1"), [lit("+"), sort("T"), sort("E1")]),
                                    pr(sort("E1"), [])
                                  }),
    
    
    sort("T"):  choice(sort("T"), { pr(sort("T"),  [sort("F"), sort("T1")]) }),
   
    sort("T1"): choice(sort("T1"),{ pr(sort("F"), [lit("*"), sort("F"), sort("T1")]),
                                   pr(sort("T1"), []) }),
                                    
    sort("F"): choice(sort("F"),  { pr(sort("F"),  [lit("("), sort("E"), lit(")")]),
                                    pr(sort("F"),  [lit("id")])
                                  }),
    
    lit("+"): choice(lit("+"), { pr(lit("+"),[\char-class([range(43,43)])]) }),
    lit("*"): choice(lit("*"), { pr(lit("*"),[\char-class([range(42,42)])]) }),
    
    lit("("): choice(lit("("), { pr(lit("("), [\char-class([range(40,40)])]) }),
    lit(")"): choice(lit(")"), { pr(lit(")"), [\char-class([range(41,41)])]) }),
    
    lit("id"): choice(lit("id"), { pr(lit("id"), [\char-class([range(105,105)]),\char-class([range(100,100)])]) })
));

//test bool tstEsc1() = esc(sort("S")) == "sort(\\\"S\\\")";
test bool tstEsc2() = esc(lit(":")) == "lit(\\\":\\\")";
test bool tstEsc3() = esc(lit("\"")) == "lit(\\\"\\\\\\\"\\\")";

//test bool tstEsc4() = "<esc(sort("S"))>" == "sort(\\\"S\\\")";
test bool tstEsc5() = "<esc(lit("\""))>" == "lit(\\\"\\\\\\\"\\\")";

//test bool tstEsc6() = "<esc(sort("S"))>" == "<"sort(\\\"S\\\")">";
test bool tstEsc7() = "<esc(lit(":"))>" == "<"lit(\\\":\\\")">";
test bool tstEsc8() = "<esc(lit("\""))>" == "<"lit(\\\"\\\\\\\"\\\")">";

//test bool tstEsc9() = "<for(s <- [sort("S"), lit("\"")]){><esc(s)><}>" ==  "sort(\\\"S\\\")lit(\\\"\\\\\\\"\\\")";
//test bool tstEsc10() = "\"<for(s <- [sort("S"), lit("\"")]){>\"<esc(s)>\"<}>\"" ==  "\"\"sort(\\\"S\\\")\"\"lit(\\\"\\\\\\\"\\\")\"\"";

test bool tstExpandParameterizedSymbols1() = expandParameterizedSymbols(GEMPTY) == GEMPTY;
test bool tstExpandParameterizedSymbols2() = expandParameterizedSymbols(G0) == G0;
test bool tstExpandParameterizedSymbols3() = expandParameterizedSymbols(GEXP) == GEXP;
test bool tstExpandParameterizedSymbols4() = expandParameterizedSymbols(GEXPPRIO) == GEXPPRIO;
    
test bool tstLiterals1() = literals(GEMPTY) == GEMPTY;
test bool tstLiterals2() = literals(G0) == G0;
test bool tstLiterals3() = literals(GEXP) == GEXP;
test bool tstLiterals4() = literals(GEXPPRIO) == GEXPPRIO;

AGrammar makeUnique(AGrammar gr) {
    int uniqueItem = 1; // -1 and -2 are reserved by the SGTDBF implementation
    int newItem() { uniqueItem += 1; return uniqueItem; };
    AProduction rewrite(AProduction p) = 
      visit(p) { 
        case AType s => s[id=newItem()] 
      }; 
    
    return gr[rules = (s : rewrite(gr.rules[s]) | s <- gr.rules)];
} 

test bool tstUniqueGEMPTY() = makeUnique(GEMPTY) == grammar(
  {sort("S")},
  ());
  
 test bool tstUniqueG0() = makeUnique(G0) == 
 grammar(
  {sort("S")},
  ( lit("0"):   choice(lit("0",id=2),   {prod(lit("0",id=3), [\char-class([range(48,48)], id=4)])}),
    sort("S"):  choice(sort("S")[id=5], {prod(sort("S")[id=6], [lit("0",id=7)])})
  ));
  
test bool tstUniqueGEXP() = makeUnique(GEXP)== 
grammar(
  {sort("E")},
  (
    sort("E"):  choice(sort("E")[id=2],
                  { prod(sort("E")[id=3], [sort("B")[id=4]]),
                    prod(sort("E")[id=5], [sort("E")[id=6], lit("+",id=7), sort("B")[id=8]]),
                    prod(sort("E")[id=9], [sort("E")[id=10],lit("*",id=11), sort("B")[id=12]])
                  }),
    lit("0"):   choice(lit("0",id=13), {prod(lit("0",id=14), [\char-class([range(48,48)], id=15)])}),
    lit("*"):   choice(lit("*",id=16), {prod(lit("*",id=17), [\char-class([range(42,42)], id=18)])}),
    lit("+"):   choice(lit("+",id=19), {prod(lit("+",id=20), [\char-class([range(43,43)], id=21)])}),
    lit("1"):   choice(lit("1",id=22), {prod(lit("1",id=23), [\char-class([range(49,49)], id=24)])}),
    sort("B"):  choice(sort("B")[id=25],
                  { prod(sort("B")[id=28], [lit("1",id=29)]),
                    prod(sort("B")[id=26], [lit("0",id=27)])
                  })
  ));


test bool tstGenerateNewItemsGEMPTY() = generateNewItems(makeUnique(GEMPTY)) == ();

test bool tstGenerateNewItemsG0() = generateNewItems(makeUnique(G0)) == 

 (lit("0"):(
    item(prod(lit("0"),[\char-class([range(48,48)])]), 0):
        <"new CharStackNode\<IConstructor\>(4, 0, new int[][]{{48,48}}, null, null)",4>),
  sort("S"):(
    item(prod(sort("S"),[lit("0")]),0):
        <"new LiteralStackNode\<IConstructor\>(7, 0, cHJvZChsaXQoIjAiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ4LDQ4KV0pXSk00, new int[] {48}, null, null)",7>)
);


test bool tstGenerateNewItemsGEXP() = generateNewItems(makeUnique(GEXP)) == 
(
  sort("E"):(
    item(prod(sort("E"), [sort("B")]),0):
        <"new NonTerminalStackNode\<IConstructor\>(4, 0, \"B\", null, null)",4>,
    item(prod(sort("E"), [sort("E"), lit("*"), sort("B")]), 0):
        <"new NonTerminalStackNode\<IConstructor\>(10, 0, \"E\", null, null)",10>,
    item(prod(sort("E"),[sort("E"),lit("+"),sort("B")]), 0):
        <"new NonTerminalStackNode\<IConstructor\>(6, 0, \"E\", null, null)",6>,
    item(prod(sort("E"),[sort("E"), lit("+"),sort("B")]), 2):
        <"new NonTerminalStackNode\<IConstructor\>(8, 2, \"B\", null, null)",8>,
    item(prod(sort("E"),[sort("E"),lit("*"),sort("B")]),2):
        <"new NonTerminalStackNode\<IConstructor\>(12, 2, \"B\", null, null)",12>,
    item(prod(sort("E"),[sort("E"),lit("*"),sort("B")]),1):
        <"new LiteralStackNode\<IConstructor\>(11, 1, cHJvZChsaXQoIioiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQyLDQyKV0pXSk00, new int[] {42}, null, null)",11>,
    item(prod(sort("E"),[sort("E"),lit("+"),sort("B")]),1):
        <"new LiteralStackNode\<IConstructor\>(7, 1, cHJvZChsaXQoIisiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQzLDQzKV0pXSk00, new int[] {43}, null, null)",7>
  ),
  lit("0"):(
    item(prod(lit("0"),[\char-class([range(48,48)])]),0):
        <"new CharStackNode\<IConstructor\>(15, 0, new int[][]{{48,48}}, null, null)",15>
  ),
  lit("*"):(
    item(prod(lit("*"),[\char-class([range(42,42)])]),0):
        <"new CharStackNode\<IConstructor\>(18, 0, new int[][]{{42,42}}, null, null)",18>
  ),
  lit("+"):(
    item(prod(lit("+"),[\char-class([range(43,43)])]),0):
        <"new CharStackNode\<IConstructor\>(21, 0, new int[][]{{43,43}}, null, null)",21>
  ),
  lit("1"):(
    item(prod(lit("1"),[\char-class([range(49,49)])]),0):
        <"new CharStackNode\<IConstructor\>(24, 0, new int[][]{{49,49}}, null, null)",24>),
  sort("B"):(
    item(prod(sort("B"),[lit("0")]),0):
        <"new LiteralStackNode\<IConstructor\>(27, 0, cHJvZChsaXQoIjAiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ4LDQ4KV0pXSk00, new int[] {48}, null, null)",27>,
    item(prod(sort("B"),[lit("1")]),0):
        <"new LiteralStackNode\<IConstructor\>(29, 0, cHJvZChsaXQoIjEiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ5LDQ5KV0pXSk00, new int[] {49}, null, null)",29>
  )
);

test bool tstComputeDontNests1() = computeDontNests(generateNewItems(makeUnique(GEMPTY)), GEMPTY,makeUnique(GEMPTY)) == {};
test bool tstComputeDontNests2() = computeDontNests(generateNewItems(makeUnique(G0)), G0, makeUnique(G0)) == {};
test bool tstComputeDontNests3() = computeDontNests(generateNewItems(makeUnique(GEXP)), GEXP,makeUnique(GEXP)) == {};
test bool tstComputeDontNests4() = computeDontNests(generateNewItems(makeUnique(GEXPPRIO)), GEXPPRIO, makeUnique(GEXPPRIO)) == {};

test bool tstExpandParameterizedSymbols1() = expandParameterizedSymbols(G0) == 
grammar(
  {sort("S")},
  (
    sort("S"):choice(
      sort("S"),
      {prod(
          sort("S"),
          [lit("0")])}),
    lit("0"):choice(
      lit("0"),
      {prod(
          lit("0"),
          [\char-class([range(48,48)])])})
  ));
  
list[str] removeEmptyLines(str s) {
    //println("----<s>");
    res = [ line | line <- split("\n", s), /^[ \t]*$/ !:= line];
    //println("----<res>");
    return res;
}

bool sameLines(str s1, str s2) {
    es1 = toSet(removeEmptyLines(s1));
    es2 = toSet(removeEmptyLines(s2));
    for(ln <- es1 - es2) println("es1 - es2: <ln>");
    //println("es2 - es1: <es2 - es1>");
    

    println("s1: <size(es1)>, s2: <size(es2)>");
    return size(es1 - es2) == 0;
}
 
test bool tstNewGenerateGEMPTY() = 
    sameLines(newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "GEMPTYParser", GEMPTY), 
              readFile(|compressed+project://rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/generated_parsers/GEMPTYParser.java.gz|));
              
test bool tstNewGenerateG0() = 
    sameLines(newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "G0Parser", G0), 
              readFile(|compressed+project://rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/generated_parsers/G0Parser.java.gz|));
              
test bool tstNewGenerateGEXP() = 
    sameLines(newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "GEXPParser", GEXP), 
    readFile(|compressed+project://rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/generated_parsers/GEXPParser.java.gz|));
    
test bool tstNewGenerateGEXPPRIO() = 
    sameLines(newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "GEXPPRIOParser", GEXPPRIO), 
              readFile(|compressed+project://rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/generated_parsers/GEXPPRIOParser.java.gz|));
