module experiments::Compiler::Examples::Tst5

import List;
//import lang::rascal::grammar::ParserGenerator;


value main(list[value] args) = " <for(y <- [100..102]){>aaa
                               '    <for(x <- [0..5]){>bbb
                               '       ccccccccccccc;
                               '    <}>;ddddddddd
                               '  <}>eeeeeeee";


//value main(list[value] args)  = newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "G0Parser", G0);

	//sameLines(newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "G0Parser", G0), 
	//          readFile(|project://rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/generated_parsers/G0Parser.java.gz|));

//public str intercalate(str sep, list[value] l) = 
//	(isEmpty(l)) ? "" : ( "<head(l)>" | it + "<sep><x>" | x <- tail(l) );
	
//str x = "XXX
//        'YYY
//        'ZZZ";
//
//
//str inc(int n) = "inc: <n>";

//value main(list[value] args) = intercalate("xx", [0,1,2]);

	//"AAA
	//'  <x>
	//'  BBB";
	
	//"AAAA<inc(13)>zzz
 //         ' BBBB
 //         ' <for(i <- [0..2]){>
 //         '   i = <i>;<}>...
 //         ' CCCC<x>DDD";
 
 	      //" <for(i <- [0..1]){>
        //  '   i = <i>;<}>...
        //  ' CCCC<x>DDD";