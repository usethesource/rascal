module lang::rascal::grammar::tests::ParserGeneratorTests
 
import lang::rascal::grammar::ParserGenerator;
import Grammar;
import lang::rascal::grammar::definition::Parameters;
import lang::rascal::grammar::definition::Literals;
import IO;
import ParseTree;
import lang::rascal::grammar::tests::TestGrammars;
import String;

// -------- Examples and tests -------------------

test bool tstEsc1() = esc(sort("S")) == "sort(\\\"S\\\")";
test bool tstEsc2() = esc(lit(":")) == "lit(\\\":\\\")";
test bool tstEsc3() = esc(lit("\"")) == "lit(\\\"\\\\\\\"\\\")";

test bool tstEsc4() = "<esc(sort("S"))>" == "sort(\\\"S\\\")";
test bool tstEsc5() = "<esc(lit("\""))>" == "lit(\\\"\\\\\\\"\\\")";

test bool tstEsc6() = "<esc(sort("S"))>" == "<"sort(\\\"S\\\")">";
test bool tstEsc7() = "<esc(lit(":"))>" == "<"lit(\\\":\\\")">";
test bool tstEsc8() = "<esc(lit("\""))>" == "<"lit(\\\"\\\\\\\"\\\")">";

test bool tstEsc9() = "<for(s <- [sort("S"), lit("\"")]){><esc(s)><}>" ==  "sort(\\\"S\\\")lit(\\\"\\\\\\\"\\\")";
test bool tstEsc10() = "\"<for(s <- [sort("S"), lit("\"")]){>\"<esc(s)>\"<}>\"" ==  "\"\"sort(\\\"S\\\")\"\"lit(\\\"\\\\\\\"\\\")\"\"";

test bool tstExpandParameterizedSymbols1() = expandParameterizedSymbols(GEMPTY) == GEMPTY;
test bool tstExpandParameterizedSymbols2() = expandParameterizedSymbols(G0) == G0;
test bool tstExpandParameterizedSymbols3() = expandParameterizedSymbols(GEXP) == GEXP;
test bool tstExpandParameterizedSymbols4() = expandParameterizedSymbols(GEXPPRIO) == GEXPPRIO;
    
test bool tstLiterals1() = literals(GEMPTY) == GEMPTY;
test bool tstLiterals2() = literals(G0) == G0;
test bool tstLiterals3() = literals(GEXP) == GEXP;
test bool tstLiterals4() = literals(GEXPPRIO) == GEXPPRIO;

test bool tstUnique0() = makeUnique(GEMPTY) == grammar(
  {sort("S")},
  ());
  
 test bool tstUnique1() = makeUnique(G0) == grammar(
  {sort("S")},
  (
    sort("S"):choice(
      sort("S",id=2),
      {prod(
          sort("S",id=3),
          [lit("0",id=4)],
          {})}),
    lit("0"):choice(
      lit("0",id=5),
      {prod(
          lit("0",id=6),
          [\char-class(
              [range(48,48)],
              id=7)],
          {})})
  ));
  
test bool tstUnique2() = makeUnique(GEXP)== grammar(
  {sort("E")},
  (
    lit("+"):choice(
      lit("+",id=2),
      {prod(
          lit("+",id=3),
          [\char-class(
              [range(43,43)],
              id=4)],
          {})}),
    lit("*"):choice(
      lit("*",id=5),
      {prod(
          lit("*",id=6),
          [\char-class(
              [range(42,42)],
              id=7)],
          {})}),
    sort("B"):choice(
      sort("B",id=8),
      {
        prod(
          sort("B",id=11),
          [lit("1",id=12)],
          {}),
        prod(
          sort("B",id=9),
          [lit("0",id=10)],
          {})
      }),
    lit("0"):choice(
      lit("0",id=13),
      {prod(
          lit("0",id=14),
          [\char-class(
              [range(48,48)],
              id=15)],
          {})}),
    sort("E"):choice(
      sort("E",id=16),
      {
        prod(
          sort("E",id=23),
          [
            sort("E",id=24),
            lit("*",id=25),
            sort("B",id=26)
          ],
          {}),
        prod(
          sort("E",id=17),
          [sort("B",id=18)],
          {}),
        prod(
          sort("E",id=19),
          [
            sort("E",id=20),
            lit("+",id=21),
            sort("B",id=22)
          ],
          {})
      }),
    lit("1"):choice(
      lit("1",id=27),
      {prod(
          lit("1",id=28),
          [\char-class(
              [range(49,49)],
              id=29)],
          {})})
  ));

test bool tstGenerateNewItems1() = generateNewItems(makeUnique(GEMPTY)) == ();

test bool tstGenerateNewItems2() = generateNewItems(makeUnique(G0)) == 
(
  sort("S"):(item(
      prod(
        sort("S"),
        [lit("0")],
        {}),
      0):<"new LiteralStackNode\<IConstructor\>(4, 0, cHJvZChsaXQoIjAiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ4LDQ4KV0pXSx7fSk00, new int[] {48}, null, null)",4>),
  lit("0"):(item(
      prod(
        lit("0"),
        [\char-class([range(48,48)])],
        {}),
      0):<"new CharStackNode\<IConstructor\>(7, 0, new int[][]{{48,48}}, null, null)",7>)
);

test bool tstGenerateNewItems3() = generateNewItems(makeUnique(GEXP)) == 
(
  lit("+"):(item(
      prod(
        lit("+"),
        [\char-class([range(43,43)])],
        {}),
      0):<"new CharStackNode\<IConstructor\>(4, 0, new int[][]{{43,43}}, null, null)",4>),
  lit("*"):(item(
      prod(
        lit("*"),
        [\char-class([range(42,42)])],
        {}),
      0):<"new CharStackNode\<IConstructor\>(7, 0, new int[][]{{42,42}}, null, null)",7>),
  sort("B"):(
    item(
      prod(
        sort("B"),
        [lit("0")],
        {}),
      0):<"new LiteralStackNode\<IConstructor\>(10, 0, cHJvZChsaXQoIjAiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ4LDQ4KV0pXSx7fSk00, new int[] {48}, null, null)",10>,
    item(
      prod(
        sort("B"),
        [lit("1")],
        {}),
      0):<"new LiteralStackNode\<IConstructor\>(12, 0, cHJvZChsaXQoIjEiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ5LDQ5KV0pXSx7fSk00, new int[] {49}, null, null)",12>
  ),
  lit("0"):(item(
      prod(
        lit("0"),
        [\char-class([range(48,48)])],
        {}),
      0):<"new CharStackNode\<IConstructor\>(15, 0, new int[][]{{48,48}}, null, null)",15>),
  sort("E"):(
    item(
      prod(
        sort("E"),
        [sort("B")],
        {}),
      0):<"new NonTerminalStackNode\<IConstructor\>(18, 0, \"B\", null, null)",18>,
    item(
      prod(
        sort("E"),
        [
          sort("E"),
          lit("*"),
          sort("B")
        ],
        {}),
      1):<"new LiteralStackNode\<IConstructor\>(25, 1, cHJvZChsaXQoIioiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQyLDQyKV0pXSx7fSk00, new int[] {42}, null, null)",25>,
    item(
      prod(
        sort("E"),
        [
          sort("E"),
          lit("+"),
          sort("B")
        ],
        {}),
      1):<"new LiteralStackNode\<IConstructor\>(21, 1, cHJvZChsaXQoIisiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQzLDQzKV0pXSx7fSk00, new int[] {43}, null, null)",21>,
    item(
      prod(
        sort("E"),
        [
          sort("E"),
          lit("+"),
          sort("B")
        ],
        {}),
      0):<"new NonTerminalStackNode\<IConstructor\>(20, 0, \"E\", null, null)",20>,
    item(
      prod(
        sort("E"),
        [
          sort("E"),
          lit("*"),
          sort("B")
        ],
        {}),
      0):<"new NonTerminalStackNode\<IConstructor\>(24, 0, \"E\", null, null)",24>,
    item(
      prod(
        sort("E"),
        [
          sort("E"),
          lit("*"),
          sort("B")
        ],
        {}),
      2):<"new NonTerminalStackNode\<IConstructor\>(26, 2, \"B\", null, null)",26>,
    item(
      prod(
        sort("E"),
        [
          sort("E"),
          lit("+"),
          sort("B")
        ],
        {}),
      2):<"new NonTerminalStackNode\<IConstructor\>(22, 2, \"B\", null, null)",22>
  ),
  lit("1"):(item(
      prod(
        lit("1"),
        [\char-class([range(49,49)])],
        {}),
      0):<"new CharStackNode\<IConstructor\>(29, 0, new int[][]{{49,49}}, null, null)",29>)
);

test bool tstComputeDontNests1() = computeDontNests(generateNewItems(makeUnique(GEMPTY)), GEMPTY, makeUnique(GEMPTY)) == {};
test bool tstComputeDontNests2() = computeDontNests(generateNewItems(makeUnique(G0)), G0, makeUnique(G0)) == {};
test bool tstComputeDontNests3() = computeDontNests(generateNewItems(makeUnique(GEXP)), GEXP, makeUnique(GEXP)) == {};
test bool tstComputeDontNests4() = computeDontNests(generateNewItems(makeUnique(GEXPPRIO)), GEXPPRIO, makeUnique(GEXPPRIO)) == {};

test bool tstExpandParameterizedSymbols5() = expandParameterizedSymbols(G0) == 
grammar(
  {sort("S")},
  (
    sort("S"):choice(
      sort("S"),
      {prod(
          sort("S"),
          [lit("0")],
          {})}),
    lit("0"):choice(
      lit("0"),
      {prod(
          lit("0"),
          [\char-class([range(48,48)])],
          {})})
  ));
  
loc ParserBaseLoc = |project://rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/generated_parsers/|;

void generateParsers() {
    writeFile(ParserBaseLoc + "GEMPTYParser.java.gz", newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "GEMPTYParser", GEMPTY));
    writeFile(ParserBaseLoc + "G0Parser.java.gz", newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "G0Parser", G0));
    writeFile(ParserBaseLoc + "GEXPParser.java.gz", newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "GEXPParser", GEXP));
    writeFile(ParserBaseLoc + "GEXPPRIOParser.java.gz", newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "GEXPPRIOParser", GEXPPRIO));
}
 
private list[str] removeEmptyLines(str s) =
    [ line | line <- split("\n", s), /^[ \t]*$/ !:= line];

bool sameLines(str s1, str s2) = size(removeEmptyLines(s1) - removeEmptyLines(s2)) == 0;
 
test bool tstNewGenerateGEMPTY() = 
	sameLines(newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "GEMPTYParser", GEMPTY), 
		      readFile(|std:///lang/rascal/grammar/tests/generated_parsers/GEMPTYParser.java.gz|));
		      
test bool tstNewGenerateG0() = 
	sameLines(newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "G0Parser", G0), 
	          readFile(|std:///lang/rascal/grammar/tests/generated_parsers/G0Parser.java.gz|));
	          
test bool tstNewGenerateGEXP() = 
	sameLines(newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "GEXPParser", GEXP), 
	readFile(|std:///lang/rascal/grammar/tests/generated_parsers/GEXPParser.java.gz|));
	
test bool tstNewGenerateGEXPPRIO() = 
	sameLines(newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "GEXPPRIOParser", GEXPPRIO), 
		      readFile(|std:///lang/rascal/grammar/tests/generated_parsers/GEXPPRIOParser.java.gz|));
