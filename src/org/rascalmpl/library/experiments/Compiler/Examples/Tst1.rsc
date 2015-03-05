module experiments::Compiler::Examples::Tst1
	
//import lang::rascal::grammar::ParserGenerator;
import Grammar;
import ParseTree;
import IO;
import String;

alias Items = map[Symbol,map[Item item, tuple[str new, int itemId] new]];

anno int Symbol@id;
	
Grammar before2 = grammar({sort("E")},
    (sort("B"):choice(
      sort("B"),
      {
        prod(
          label(
            "$MetaHole",
            sort("B")),
          [
            \char-class([range(0,0)]),
            lit("sort(\"B\")"),
            lit(":"),
            iter(\char-class([range(48,57)])),
            \char-class([range(0,0)])
          ],
          {\tag("holeType"(sort("B")))}),
        prod(
          sort("B"),
          [lit("0")],
          {}),
        prod(
          sort("B"),
          [lit("1")],
          {})
      })
      )
      );


value main(list[value]args) {
	int uniqueItem = 1; // -1 and -2 are reserved by the SGTDBF implementation
    int newItem() { uniqueItem += 1; return uniqueItem; };
    res = visit(before2) { case Symbol s => s[@id=newItem()] };
    iprintln(res);
    return true;
   } 



//sameLines1(newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "GEXPParser", GEXP), 
//	readFile(|project://rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/generated_parsers/GEXPParser.java.gz|));

