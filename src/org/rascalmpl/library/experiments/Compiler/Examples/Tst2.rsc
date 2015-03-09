@bootstrapParser
module experiments::Compiler::Examples::Tst2

import IO;
import Relation;
import ParseTree;
//import lang::rascal::types::Util;
//import lang::rascal::types::AbstractName;
//import lang::rascal::types::AbstractType;
import lang::rascal::\syntax::Rascal;

//test bool tstGetPatternNames5() =  domain(getPatternNames((Pattern) `/\<x:[a-z]+\>/`)) == {RSimpleName("x")};
//test bool tstGetPatternNames6() =  domain(getPatternNames((Pattern) `/^\<x:[a-z]+\>aaa\<y:[0-9]+\>$/`)) == {RSimpleName("x"),RSimpleName("y")};

value main(list[value] args) = regExpPatternNames((RegExpLiteral) `/\<x:[a-z]+\>/`) ;

public set[str] regExpPatternNames(RegExpLiteral rl) {
    set[str] names = { };
        
    top-down visit(rl) {
        case \appl(\prod(lex("RegExp"),[_,\lex("Name"),_,_,_],_),list[Tree] prds) : {
        	println("matches!");
        	if (Name regExpVarName := prds[1]) { 
        		names += "<regExpVarName>";
        	}
        }
    }
    
    return names;
}