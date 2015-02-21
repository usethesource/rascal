module experiments::Compiler::Examples::Tst1
	
import lang::rascal::grammar::ParserGenerator;
import Grammar;
import ParseTree;
import IO;
import String;

alias Items = map[Symbol,map[Item item, tuple[str new, int itemId] new]];

anno int Symbol@id;
	
Grammar G0U = 	grammar(
  {sort("S")[
      @id=2
    ]},
  (
    sort("S")[
      @id=3
    ]:choice(
      sort("S")[
        @id=4
      ],
      {prod(
          sort("S")[
            @id=5
          ],
          [lit("0")[
              @id=6
            ]],
          {})}),
    lit("0")[
      @id=7
    ]:choice(
      lit("0")[
        @id=8
      ],
      {prod(
          lit("0")[
            @id=9
          ],
          [\char-class([range(48,48)])[
              @id=10
            ]],
          {})})
  ));

Items GNIG0 =
(
  sort("S")[
    @id=5
  ]:(item(
      prod(
        sort("S")[
          @id=5
        ],
        [lit("0")[
            @id=6
          ]],
        {}),
      0):<"new LiteralStackNode\<IConstructor\>(6, 0, prod__lit_0__char_class___range__48_48_, new int[] {48}, null, null)",6>),
  lit("0")[
    @id=9
  ]:(item(
      prod(
        lit("0")[
          @id=9
        ],
        [\char-class([range(48,48)])[
            @id=10
          ]],
        {}),
      0):<"new CharStackNode\<IConstructor\>(10, 0, new int[][]{{48,48}}, null, null)",10>)
);

list[str] removeEmptyLines1(str s) =
	[ line | line <- split("\n", s), /^[ \t]*$/ !:= line];

bool sameLines1(str s1, str s2) { 
	r1 = removeEmptyLines1(s1); 
	r2 = removeEmptyLines(s2); 
	d = r1 - r2;
	n = size(d); 
	println("size(r1) = <size(r1)>, size(r2) = <size(r2)>, size of diff == <n>: <d>"); return n == 0; }

value main(list[value]args) = sameLines1(newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "GEXPParser", GEXP), 
	readFile(|project://rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/generated_parsers/GEXPParser.java.gz|));

