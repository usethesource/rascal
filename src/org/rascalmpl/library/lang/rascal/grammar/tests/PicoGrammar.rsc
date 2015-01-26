module lang::rascal::grammar::tests::PicoGrammar

import IO;
import Grammar;
import ParseTree;
import String;
import List;
import lang::rascal::grammar::ParserGenerator;
import lang::rascal::grammar::Lookahead;
import util::Benchmark;

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
sort("Expression"): choice(sort("Expression"),{priority(sort("Expression"),[choice(sort("Expression"),{prod(label("strCon",sort("Expression")),[label("string",lex("String"))],{}),prod(label("id",sort("Expression")),[label("name",lex("Id"))],{}),prod(label("natCon",sort("Expression")),[label("natcon",lex("Natural"))],{}),prod(sort("Expression"),[lit("("),layouts("Layout"),label("e",sort("Expression")),layouts("Layout"),lit(")")],{\bracket()})}),prod(label("conc",sort("Expression")),[label("lhs",sort("Expression")),layouts("Layout"),lit("||"),layouts("Layout"),label("rhs",sort("Expression"))],{\assoc(left())}),associativity(sort("Expression"),left(),{prod(label("add",sort("Expression")),[label("lhs",sort("Expression")),layouts("Layout"),lit("+"),layouts("Layout"),label("rhs",sort("Expression"))],{\assoc(left())}),prod(label("sub",sort("Expression")),[label("lhs",sort("Expression")),layouts("Layout"),lit("-"),layouts("Layout"),label("rhs",sort("Expression"))],{\assoc(\left())})})])}),
layouts("$default$"): choice(layouts("$default$"),{prod(layouts("$default$"),[],{})}),
sort("Type"): choice(sort("Type"),{prod(label("natural",sort("Type")),[lit("natural")],{}),prod(label("string",sort("Type")),[lit("string")],{})}),
sort("Declaration"): choice(sort("Declaration"),{prod(label("decl",sort("Declaration")),[label("id",lex("Id")),layouts("Layout"),lit(":"),layouts("Layout"),label("tp",sort("Type"))],{})})
)

);

str generatePico() = newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "PicoParser", Pico);

void generateAndWritePico(){
	writeFile(|home:///PicoParser.java.gz|, generatePico());
}

int generateAndTimePico() { 
	t = cpuTime(); 
	n = size(split("\n", generatePico()));
	dur = (cpuTime() - t)/1000000;;
	println("<n> lines, <dur> msec");
	return dur;
}	

value main(list[value] args) = generateAndTimePico();

test bool tstNewGeneratePico() = 
	sameLines(generatePico(), readFile(|project://rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/generated_parsers/PicoParser.java.gz|));