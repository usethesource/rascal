@bootstrapParser
module experiments::Compiler::Examples::Tst5

import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;
import lang::rascal::\syntax::Rascal;
import lang::rascal::types::Util;
import Relation;

public rel[RName,loc] regExpPatternNames(RegExpLiteral rl) {
    rel[RName,loc] names = { };
        
    top-down visit(rl) {
        case \appl(\prod(lex("RegExp"),[_,\lex("Name"),_,_,_],_),list[Tree] prds) : {
        	if (Name regExpVarName := prds[1]) { 
        		names += < convertName(regExpVarName), prds[1]@\loc >;
        	}
        }
    }
    
    return names;
}

value main(list[value] args) = domain(getPatternNames((Pattern) `/\<x:[a-z]+\>/`)) ;