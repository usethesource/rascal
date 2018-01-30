module lang::rascalcore::grammar::tests::ParserGeneratorTests
 
import lang::rascalcore::grammar::ParserGenerator;
import lang::rascalcore::grammar::definition::Grammar;
import lang::rascalcore::grammar::definition::Parameters;
import lang::rascalcore::grammar::definition::Literals;
import IO;
import String;
import lang::rascalcore::check::AType;
import util::Reflective;

import lang::rascalcore::grammar::tests::TestGrammars;

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

AGrammar makeUnique(AGrammar gr) {
    int uniqueItem = 1; // -1 and -2 are reserved by the SGTDBF implementation
    int newItem() { uniqueItem += 1; return uniqueItem; };
    
    return visit(gr) { case AType s => s[id=newItem()] }
} 

test bool tstUnique0() = makeUnique(GEMPTY) == grammar(
  {sort("S")[
      id=2
    ]},
  ());
  
 test bool tstUnique0() = makeUnique(G0) == grammar(
  {sort("S")[
      id=2
    ]},
  (
    sort("S")[
      id=3
    ]:choice(
      sort("S")[
        id=4
      ],
      {prod(
          sort("S")[
            id=5
          ],
          [lit("0")[
              id=6
            ]])}),
    lit("0")[
      id=7
    ]:choice(
      lit("0")[
        id=8
      ],
      {prod(
          lit("0")[
            id=9
          ],
          [\char-class([range(48,48)])[
              id=10
            ]])})
  ));
  
test bool tstUnique1() = makeUnique(GEXP)== grammar(
  {sort("E")[
      id=2
    ]},
  (
    lit("+")[
      id=3
    ]:choice(
      lit("+")[
        id=4
      ],
      {prod(
          lit("+")[
            id=5
          ],
          [\char-class([range(43,43)])[
              id=6
            ]])}),
    lit("*")[
      id=7
    ]:choice(
      lit("*")[
        id=8
      ],
      {prod(
          lit("*")[
            id=9
          ],
          [\char-class([range(42,42)])[
              id=10
            ]])}),
    sort("B")[
      id=11
    ]:choice(
      sort("B")[
        id=12
      ],
      {
        prod(
          sort("B")[
            id=13
          ],
          [lit("0")[
              id=14
            ]]),
        prod(
          sort("B")[
            id=15
          ],
          [lit("1")[
              id=16
            ]])
      }),
    lit("0")[
      id=17
    ]:choice(
      lit("0")[
        id=18
      ],
      {prod(
          lit("0")[
            id=19
          ],
          [\char-class([range(48,48)])[
              id=20
            ]])}),
    sort("E")[
      id=21
    ]:choice(
      sort("E")[
        id=22
      ],
      {
        prod(
          sort("E")[
            id=23
          ],
          [sort("B")[
              id=24
            ]]),
        prod(
          sort("E")[
            id=25
          ],
          [
            sort("E")[
              id=26
            ],
            lit("+")[
              id=27
            ],
            sort("B")[
              id=28
            ]
          ]),
        prod(
          sort("E")[
            id=29
          ],
          [
            sort("E")[
              id=30
            ],
            lit("*")[
              id=31
            ],
            sort("B")[
              id=32
            ]
          ])
      }),
    lit("1")[
      id=33
    ]:choice(
      lit("1")[
        id=34
      ],
      {prod(
          lit("1")[
            id=35
          ],
          [\char-class([range(49,49)])[
              id=36
            ]])})
  ));


test bool tstGenerateNewItems1() = generateNewItems(makeUnique(GEMPTY)) == ();

test bool tstGenerateNewItems2() = generateNewItems(makeUnique(G0)) == 
(
  sort("S")[
    id=5
  ]:(item(
      prod(
        sort("S")[
          id=5
        ],
        [lit("0")[
            id=6
          ]]),
      0):<"new LiteralStackNode\<IConstructor\>(6, 0, cHJvZChsaXQoIjAiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ4LDQ4KV0pXSx7fSk00, new int[] {48}, null, null)",6>),
  lit("0")[
    id=9
  ]:(item(
      prod(
        lit("0")[
          id=9
        ],
        [\char-class([range(48,48)])[
            id=10
          ]]),
      0):<"new CharStackNode\<IConstructor\>(10, 0, new int[][]{{48,48}}, null, null)",10>)
);

test bool tstGenerateNewItems3() = generateNewItems(makeUnique(GEXP)) == 
(
  lit("+")[
    id=5
  ]:(item(
      prod(
        lit("+")[
          id=5
        ],
        [\char-class([range(43,43)])[
            id=6
          ]]),
      0):<"new CharStackNode\<IConstructor\>(6, 0, new int[][]{{43,43}}, null, null)",6>),
  lit("*")[
    id=9
  ]:(item(
      prod(
        lit("*")[
          id=9
        ],
        [\char-class([range(42,42)])[
            id=10
          ]]),
      0):<"new CharStackNode\<IConstructor\>(10, 0, new int[][]{{42,42}}, null, null)",10>),
  sort("B")[
    id=13
  ]:(
    item(
      prod(
        sort("B")[
          id=13
        ],
        [lit("0")[
            id=14
          ]]),
      0):<"new LiteralStackNode\<IConstructor\>(14, 0, cHJvZChsaXQoIjAiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ4LDQ4KV0pXSx7fSk00, new int[] {48}, null, null)",14>,
    item(
      prod(
        sort("B")[
          id=15
        ],
        [lit("1")[
            id=16
          ]]),
      0):<"new LiteralStackNode\<IConstructor\>(16, 0, cHJvZChsaXQoIjEiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ5LDQ5KV0pXSx7fSk00, new int[] {49}, null, null)",16>
  ),
  lit("0")[
    id=19
  ]:(item(
      prod(
        lit("0")[
          id=19
        ],
        [\char-class([range(48,48)])[
            id=20
          ]]),
      0):<"new CharStackNode\<IConstructor\>(20, 0, new int[][]{{48,48}}, null, null)",20>),
  sort("E")[
    id=23
  ]:(
    item(
      prod(
        sort("E")[
          id=23
        ],
        [sort("B")[
            id=24
          ]]),
      0):<"new NonTerminalStackNode\<IConstructor\>(24, 0, \"B\", null, null)",24>,
    item(
      prod(
        sort("E")[
          id=29
        ],
        [
          sort("E")[
            id=30
          ],
          lit("*")[
            id=31
          ],
          sort("B")[
            id=32
          ]
        ]),
      1):<"new LiteralStackNode\<IConstructor\>(31, 1, cHJvZChsaXQoIioiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQyLDQyKV0pXSx7fSk00, new int[] {42}, null, null)",31>,
    item(
      prod(
        sort("E")[
          id=25
        ],
        [
          sort("E")[
            id=26
          ],
          lit("+")[
            id=27
          ],
          sort("B")[
            id=28
          ]
        ]),
      1):<"new LiteralStackNode\<IConstructor\>(27, 1, cHJvZChsaXQoIisiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQzLDQzKV0pXSx7fSk00, new int[] {43}, null, null)",27>,
    item(
      prod(
        sort("E")[
          id=25
        ],
        [
          sort("E")[
            id=26
          ],
          lit("+")[
            id=27
          ],
          sort("B")[
            id=28
          ]
        ]),
      0):<"new NonTerminalStackNode\<IConstructor\>(26, 0, \"E\", null, null)",26>,
    item(
      prod(
        sort("E")[
          id=29
        ],
        [
          sort("E")[
            id=30
          ],
          lit("*")[
            id=31
          ],
          sort("B")[
            id=32
          ]
        ]),
      0):<"new NonTerminalStackNode\<IConstructor\>(30, 0, \"E\", null, null)",30>,
    item(
      prod(
        sort("E")[
          id=29
        ],
        [
          sort("E")[
            id=30
          ],
          lit("*")[
            id=31
          ],
          sort("B")[
            id=32
          ]
        ]),
      2):<"new NonTerminalStackNode\<IConstructor\>(32, 2, \"B\", null, null)",32>,
    item(
      prod(
        sort("E")[
          id=25
        ],
        [
          sort("E")[
            id=26
          ],
          lit("+")[
            id=27
          ],
          sort("B")[
            id=28
          ]
        ]),
      2):<"new NonTerminalStackNode\<IConstructor\>(28, 2, \"B\", null, null)",28>
  ),
  lit("1")[
    id=35
  ]:(item(
      prod(
        lit("1")[
          id=35
        ],
        [\char-class([range(49,49)])[
            id=36
          ]]),
      0):<"new CharStackNode\<IConstructor\>(36, 0, new int[][]{{49,49}}, null, null)",36>)
);

test bool tstComputeDontNests1() = computeDontNests(generateNewItems(makeUnique(GEMPTY)), makeUnique(GEMPTY)) == {};
test bool tstComputeDontNests2() = computeDontNests(generateNewItems(makeUnique(G0)), makeUnique(G0)) == {};
test bool tstComputeDontNests3() = computeDontNests(generateNewItems(makeUnique(GEXP)), makeUnique(GEXP)) == {};
test bool tstComputeDontNests4() = computeDontNests(generateNewItems(makeUnique(GEXPPRIO)), makeUnique(GEXPPRIO)) == {};

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
  
loc ParserBaseLoc = |compressed+file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/generated_parsers/|;

void generateParsers() {
    writeFile(ParserBaseLoc + "GEMPTYParser.java.gz", newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "GEMPTYParser", GEMPTY));
    writeFile(ParserBaseLoc + "G0Parser.java.gz", newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "G0Parser", G0));
    writeFile(ParserBaseLoc + "GEXPParser.java.gz", newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "GEXPParser", GEXP));
    writeFile(ParserBaseLoc + "GEXPPRIOParser.java.gz", newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "GEXPPRIOParser", GEXPPRIO));
}
 
test bool tstNewGenerateGEMPTY() = 
	sameLines(newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "GEMPTYParser", GEMPTY), 
		      readFile(|compressed+std:///lang/rascal/grammar/tests/generated_parsers/GEMPTYParser.java.gz|));
		      
test bool tstNewGenerateG0() = 
	sameLines(newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "G0Parser", G0), 
	          readFile(|compressed+std:///lang/rascal/grammar/tests/generated_parsers/G0Parser.java.gz|));
	          
test bool tstNewGenerateGEXP() = 
	sameLines(newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "GEXPParser", GEXP), 
	readFile(|compressed+std:///lang/rascal/grammar/tests/generated_parsers/GEXPParser.java.gz|));
	
test bool tstNewGenerateGEXPPRIO() = 
	sameLines(newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "GEXPPRIOParser", GEXPPRIO), 
		      readFile(|compressed+std:///lang/rascal/grammar/tests/generated_parsers/GEXPPRIOParser.java.gz|));
