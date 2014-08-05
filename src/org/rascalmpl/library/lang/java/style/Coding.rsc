module lang::java::style::Coding

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

import IO;

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

import IO;

data Message = coding(str category, loc pos);



/*
ArrayTrailingComma				TBD
AvoidInlineConditionals			DONE
CovariantEquals					TBD
EmptyStatement					TBD
EqualsAvoidNull					TBD
EqualsHashCode					TBD
FinalLocalVariable				TBD
HiddenField						TBD
IllegalInstantiation			TBD
IllegalToken					TBD
IllegalTokenText				TBD
InnerAssignment					TBD
MagicNumber						TBD
MissingSwitchDefault			TBD
ModifiedControlVariable			TBD
RedundantThrows					TBD
SimplifyBooleanExpression		TBD
SimplifyBooleanReturn			TBD
StringLiteralEquality			TBD
NestedForDepth					DONE
NestedIfDepth					DONE
NestedTryDepth					DONE
NoClone							DONE
NoFinalizer						DONE
SuperClone						TBD
SuperFinalize					TBD
IllegalCatch					TBD
IllegalThrows					TBD
PackageDeclaration				TBD
JUnitTestCase					TBD
ReturnCount						TBD
IllegalType						TBD
DeclarationOrder				TBD
ParameterAssignment				TBD
ExplicitInitialization			TBD
DefaultComesLast				DONE
MissingCtor						TBD
FallThrough						TBD
MultipleStringLiterals			DONE
MultipleVariableDeclarations	TBD
RequireThis						TBD
UnnecessaryParentheses			TBD
OneStatementPerLine				TBD
*/

list[Message] coding(node ast, M3 model) {
	return 
		  avoidInlineConditionals(ast, model)
		+ nestedForDepth(ast, model)
		+ nestedIfDepth(ast, model)
		+ nestedTryDepth(ast, model)
		+ noClone(ast, model)
		+ noFinalizer(ast, model)
		+ returnCount(ast, model)
		+ defaultComesLast(ast, model)
		+ multipleStringLiterals(ast, model)
		;
}

list[Message] avoidInlineConditionals(node ast, M3 model) =
	[coding("AvoidInlineConditionals", c@src) | /c:\conditional(_, _, _) := ast];

list[Message] nestedForDepth(node ast, M3 model){
	msgs = [];
	int limit = 2;
	
	void findViolations(ast2, int nesting){
		if(nesting == limit){
			msgs += coding("NestedForDepth", ast2@src);
		} else {
			top-down-break visit(ast){
				case \foreach(_, _, body): 	
					findViolations(body, nesting + 1);
		    	case \for(_, _, _, body) : 	
		    		findViolations(body, nesting + 1);
		    	case \for(_, _, body): 		
		    		findViolations(body, nesting + 1);
		    }
	    }
	}
    findViolations(ast, 0);
    return msgs;
}

list[Message] nestedIfDepth(node ast, M3 model){
	msgs = [];
	int limit = 2;
	
	void findViolations(ast2, int nesting){
		if(nesting == limit){
			msgs += coding("NestedIfDepth", ast2@src);
		} else {
			top-down-break visit(ast){
				case \if(_, thenPart): 			
					findViolations(thenPart, nesting + 1);
    			case \if(_, thenPart, elsePart): { 
    				findViolations(thenPart, nesting + 1);
    				findViolations(elsePart, nesting + 1);
    			}
		    }
	    }
	}
    findViolations(ast, 0);
    return msgs;
}

list[Message] nestedTryDepth(node ast, M3 model){
	msgs = [];
	int limit = 2;
	
	void findViolations(ast2, int nesting){
		if(nesting == limit){
			msgs += coding("NestedTryDepth", ast2@src);
		} else {
			top-down-break visit(ast){
				case \try(Statement body, list[Statement] catchClauses): {
			 		findViolations(body, nesting + 1);
			 		for(stat <- catchClauses){
			 			findViolations(stat, nesting + 1);
			 		}
			 	}
    		 	case \try(Statement body, list[Statement] catchClauses, Statement \finally): {
    				findViolations(body, nesting + 1);
			 		for(stat <- catchClauses){
			 			findViolations(stat, nesting + 1);
			 		}
			 		findViolations(\finally, nesting + 1);
		    	}
	        }
	   }
	}
    findViolations(ast, 0);
    return msgs;
}

list[Message] noClone(node ast, M3 model){
	overrides = model@methodOverrides;
	msgs = [];
	top-down-break visit(ast){
    	case m: \method(_, "clone", _, _, _): 
    		if(!isEmpty(overrides[m@src])) { msgs += coding("NoClone", m@src); }
    	case m: \method(_, "clone", _, _):	
    		if(!isEmpty(overrides[m@src])) { msgs += coding("NoClone", m@src); }
	}
	return msgs;
}

list[Message] noFinalizer(node ast, M3 model){
	msgs = [];
	top-down-break visit(ast){
    	case m: \method(_, "finalizer", _, _, _): 
    		msgs += coding("NoFinalizer", m@src);
    	case m: \method(_, "finalizer", _, _):	
    		msgs += coding("NoFinalizer", m@src);
	}
	return msgs;
}

list[Message] multipleStringLiterals(node ast, M3 model){
	seen = {};
	msg = [];
 	for( /s: stringLiteral(str stringValue) := ast){
 		if(stringValue != ""){
 			if(stringValue in seen){
 				msg += coding("MultipleStringLiterals", s@src);
 			}
 			seen += s;
 		}
 	}
 	return msgs;
}

list[Message] returnCount(node ast, M3 model){
	int limit = 2;
	msgs = [];
	
	int countReturns(ast2){
		cnt = 0;
		visit(ast){
			case \return(Expression expression): cnt += 1;
   		 	case \return():	cnt +=1;
   		 }
   		 return cnt;
	}
	top-down-break visit(ast){
    	case m: \method(_, _, _, _, body): 
    		if(countReturns(body) > limit) {
    			msgs += coding("ReturnCount", m@src);
    		}
	}
	return msgs;
}

list[Message] defaultComesLast(node ast, M3 model){
	msgs = [];
	
	bool defComesLast(list[Statement] statements){
		for(i <- index(statements)){
			if(\defaultCase() := statements[i]){
				return i == size(statements) - 2;
			}
		}
		return true;
	}
	
	top-down-break visit(ast){
    	case s: \switch(_, list[Statement] statements):
    		if(!defComesLast(statements)){
    			msgs += coding("defaultComesLast", s@src);
    		}
	}
	return msgs;
}
/*
 | \switch(Expression expression, list[Statement] statements)
    | \case(Expression expression)
    | \defaultCase()
*/

