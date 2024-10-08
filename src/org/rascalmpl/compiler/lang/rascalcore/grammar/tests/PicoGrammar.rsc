module lang::rascalcore::grammar::tests::PicoGrammar

import IO;
import Grammar;
import ParseTree;
import String;
import List;
import lang::rascal::grammar::ParserGenerator;
import lang::rascal::grammar::Lookahead;
import util::Benchmark;
import util::Reflective;
import lang::rascal::grammar::tests::ParserGeneratorTests;

public Grammar Pico = grammar({sort("Program")},

(
empty(): choice(empty(),{prod(empty(),[],{})}),
lex("Id"): choice(lex("Id"),{prod(lex("Id"),[\char-class([range(97,122)]),conditional(\iter-star(\char-class([range(48,57),range(97,122)])),{\not-follow(\char-class([range(48,57),range(97,122)]))})],{})}),
\start(sort("Program")): choice(\start(sort("Program")),{prod(\start(sort("Program")),[layouts("Layout"),label("top",sort("Program")),layouts("Layout")],{})}),
layouts("Layout"): choice(layouts("Layout"),{prod(layouts("Layout"),[conditional(\iter-star(lex("WhitespaceAndComment")),{\not-follow(\char-class([range(9,10),range(13,13),range(32,32),range(37,37)]))})],{})}),
lex("WhitespaceAndComment"): choice(lex("WhitespaceAndComment"),{prod(label("ws2",lex("WhitespaceAndComment")),[lit("%"),iter(\char-class([range(1,36),range(38,16777215)])),lit("%")],{\tag("category"("Comment"))}),prod(label("ws3",lex("WhitespaceAndComment")),[lit("%%"),conditional(\iter-star(\char-class([range(1,9),range(11,16777215)])),{\end-of-line()})],{	\tag("category"("Comment"))}),prod(lex("WhitespaceAndComment"),[\char-class([range(9,10),range(13,13),range(32,32)])],{})}),
sort("Statement"): choice(sort("Statement"),{prod(label("ifElseStat",sort("Statement")),[lit("if"),layouts("Layout"),label("cond",sort("Expression")),layouts("Layout"),lit("then"),layouts("Layout"),label("thenPart",\iter-star-seps(sort("Statement"),[layouts("Layout"),lit(";"),layouts("Layout")])),layouts("Layout"),lit("else"),layouts("Layout"),label("elsePart",\iter-star-seps(sort("Statement"),[layouts("Layout"),lit(";"),layouts("Layout")])),layouts("Layout"),lit("fi")],{}),prod(label("asgStat",sort("Statement")),[label("var",lex("Id")),layouts("Layout"),lit(":="),layouts("Layout"),label("val",sort("Expression"))],{}),prod(label("whileStat",sort("Statement")),[lit("while"),layouts("Layout"),label("cond",sort("Expression")),layouts("Layout"),lit("do"),layouts("Layout"),label("body",\iter-star-seps(sort("Statement"),[layouts("Layout"),lit(";"),layouts("Layout")])),layouts("Layout"),lit("od")],{})}),
lex("Natural"): choice(lex("Natural"),{prod(lex("Natural"),[iter(\char-class([range(48,57)]))],{})}),
sort("Program"): choice(sort("Program"),{prod(label("program",sort("Program")),[lit("begin"),layouts("Layout"),label("decls",sort("Declarations")),layouts("Layout"),label("body",\iter-star-seps(sort("Statement"),[layouts("Layout"),lit(";"),layouts("Layout")])),layouts("Layout"),lit("end")],{})}),
sort("Declarations"): choice(sort("Declarations"),{prod(sort("Declarations"),[lit("declare"),layouts("Layout"),label("decls",\iter-star-seps(sort("Declaration"),[layouts("Layout"),lit(","),layouts("Layout")])),layouts("Layout"),lit(";")],{})}),
lex("String"): choice(lex("String"),{prod(lex("String"),[lit("\""),\iter-star(\char-class([range(1,33),range(35,16777215)])),lit("\"")],{})}),
sort("Expression"): choice(sort("Expression"),{priority(sort("Expression"),[choice(sort("Expression"),{prod(label("strCon",sort("Expression")),[label("string",lex("String"))],{}),prod(label("id",sort("Expression")),[label("name",lex("Id"))],{}),prod(label("natCon",sort("Expression")),[label("natcon",lex("Natural"))],{}),prod(sort("Expression"),[lit("("),layouts("Layout"),label("e",sort("Expression")),layouts("Layout"),lit(")")],{\bracket()})}),prod(label("conc",sort("Expression")),[label("lhs",sort("Expression")),layouts("Layout"),lit("||"),layouts("Layout"),label("rhs",sort("Expression"))],{}),associativity(sort("Expression"),left(),{prod(label("add",sort("Expression")),[label("lhs",sort("Expression")),layouts("Layout"),lit("+"),layouts("Layout"),label("rhs",sort("Expression"))],{}),prod(label("sub",sort("Expression")),[label("lhs",sort("Expression")),layouts("Layout"),lit("-"),layouts("Layout"),label("rhs",sort("Expression"))],{\assoc(\left())})})])}),
layouts("$default$"): choice(layouts("$default$"),{prod(layouts("$default$"),[],{})}),
sort("Type"): choice(sort("Type"),{prod(label("natural",sort("Type")),[lit("natural")],{}),prod(label("string",sort("Type")),[lit("string")],{})}),
sort("Declaration"): choice(sort("Declaration"),{prod(label("decl",sort("Declaration")),[label("id",lex("Id")),layouts("Layout"),lit(":"),layouts("Layout"),label("tp",sort("Type"))],{})})
)

);


loc PicoParserLoc = |project://rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/| + "generated_parsers/PicoParser.java.gz";

str generatePicoParser() = newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "PicoParser", Pico);

void generateAndWritePicoParser(){
	writeFile(PicoParserLoc, generatePicoParser());
}

int generateAndTimePicoParser() { 
	t = cpuTime(); 
	generatePicoParser();
	return (cpuTime() - t)/1000000;
}	

//value main() = generateAndTimePicoParser();
value main() {cnt = 0; visit(Pico){ case {*value s}: cnt += 1; }; return cnt; }

test bool tstgeneratePicoParser() = sameLines(generatePicoParser(), readFile(PicoParserLoc));

test bool cntChoice1()      {cnt = 0; visit(Pico){ case choice(_,_): cnt += 1; }; return cnt == 15; }
test bool cntChoice2()      = size([x | /x:choice(_,_) := Pico]) == 15;

test bool cntLex1()         {cnt = 0; visit(Pico){ case lex(_): cnt += 1; }; return cnt == 20; }
test bool cntLex2()         = size([x | /x:lex(_) := Pico]) == 20;

test bool cntEmpty1()       {cnt = 0; visit(Pico){ case empty(): cnt += 1; }; return cnt == 3; }
test bool cntEmpty2()       = size([x | /x:empty() := Pico]) == 3;

test bool cntSort1()        {cnt = 0; visit(Pico){ case sort(_): cnt += 1; }; return cnt == 52; }
test bool cntSort2()        = size([x | /x:sort(_) := Pico]) == 52;

test bool cntLit1()         {cnt = 0; visit(Pico){ case lit(_): cnt += 1; }; return cnt == 30; }
test bool cntLit2()         = size([x | /x:lit(_) := Pico]) == 30;

test bool cntLabel1()       {cnt = 0; visit(Pico){ case label(_,_): cnt += 1; }; return cnt == 38; }
test bool cntLabel2()       = size([x | /x:label(_,_) := Pico]) == 38;

test bool cntCharClass1()   {cnt = 0; visit(Pico){ case \char-class(_): cnt += 1; }; return cnt == 9; }
test bool cntCharClass2()   = size([x | /x:\char-class(_) := Pico]) == 9;
                        
test bool cntProd1()        {cnt = 0; visit(Pico){ case \prod(_,_,_): cnt += 1; }; return cnt == 25; }
test bool cntProd2()        = size([x | /x:\prod(_,_,_) := Pico]) == 25;

test bool cntEmptyList1()   {cnt = 0; visit(Pico){ case []: cnt += 1; }; return cnt == 2; }
test bool cntEmptyList2()   = size([x | /x:[] := Pico]) == 2;
                         
test bool cntList1()        {cnt = 0; visit(Pico){ case [*value _s]: cnt += 1; }; return cnt == 40; }
test bool cntList2()        = size([x | /x:[*value _s] := Pico]) == 40;

test bool cntEmptySet1()    {cnt = 0; visit(Pico){ case {}: cnt += 1; }; return cnt == 20; }
test bool cntEmptySet2()    = size([x | /x:{} := Pico]) == 20;

test bool cntSet1()         {cnt = 0; visit(Pico){ case {*value _s}: cnt += 1; }; return cnt == 45; }
test bool cntSet2()         = size([x | /x:{*value _s} := Pico]) == 45;
@ignoreInterpreter{gives wrong answer 1186}
test bool cntStr1()         {cnt = 0; visit(Pico){ case str _s: cnt += 1; }; return cnt == 187; }
test bool cntStr2()         = size([x | /x:str _s := Pico]) == 187;

test bool cntInt1()         {cnt = 0; visit(Pico){ case int _n: cnt += 1; }; return cnt == 38; }
test bool cntInt2()         = size([x | /x:int _n := Pico]) == 38;

test bool cntIter1()        {cnt = 0; visit(Pico){ case \iter(_): cnt += 1; }; return cnt == 2; }
test bool cntIter2()        = size([x | /x:\iter(_) := Pico]) == 2;

test bool cntIterStar1()    {cnt = 0; visit(Pico){ case \iter-star(_): cnt += 1; }; return cnt == 4; }
test bool cntIterStar2()    = size([x | /x:\iter-star(_) := Pico]) == 4;

test bool cntIterSeps1()    {cnt = 0; visit(Pico){ case \iter-seps(_,_): cnt += 1; }; return cnt == 0; }
test bool cntIterSeps2()    = size([x | /x:\iter-seps(_,_) := Pico]) == 0;

test bool cntIterStarSeps1(){cnt = 0; visit(Pico){ case \iter-star-seps(_,_): cnt += 1; }; return cnt == 5; }
test bool cntIterStarSeps2()= size([x | /x: \iter-star-seps(_,_) := Pico]) == 5;

test bool cntConditional1() {cnt = 0; visit(Pico){ case \conditional(_,_): cnt += 1; }; return cnt == 3; }
test bool cntConditional2() = size([x | /x:\conditional(_,_) := Pico]) == 3;

test bool cntRange1()       {cnt = 0; visit(Pico){ case \range(_,_): cnt += 1; }; return cnt == 19; }
test bool cntRange2()       = size([x | /x:\range(_,_) := Pico]) == 19;
