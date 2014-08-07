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
import List;
import Set;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

import lang::java::style::Utils;

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
MagicNumber						DONE
MissingSwitchDefault			DONE
ModifiedControlVariable			TBD
RedundantThrows					TBD
SimplifyBooleanExpression		DONE
SimplifyBooleanReturn			DONE
StringLiteralEquality			DONE
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
ReturnCount						DONE
IllegalType						TBD
DeclarationOrder				TBD
ParameterAssignment				TBD
ExplicitInitialization			TBD
DefaultComesLast				DONE
MissingCtor						TBD
FallThrough						DONE
MultipleStringLiterals			DONE
MultipleVariableDeclarations	TBD
RequireThis						TBD
UnnecessaryParentheses			TBD
OneStatementPerLine				TBD
*/

list[Message] codingChecks(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations) {
	return 
		  avoidInlineConditionals(ast, model, classDeclarations, methodDeclarations)
		+ magicNumber(ast, model, classDeclarations, methodDeclarations)
		+ missingSwitchDefault(ast, model, classDeclarations, methodDeclarations)
		+ simplifyBooleanExpression(ast, model, classDeclarations, methodDeclarations)
		+ simplifyBooleanReturn(ast, model, classDeclarations, methodDeclarations)
		+ stringLiteralEquality(ast, model, classDeclarations, methodDeclarations)
		+ nestedForDepth(ast, model, classDeclarations, methodDeclarations)
		+ nestedIfDepth(ast, model, classDeclarations, methodDeclarations)
		+ nestedTryDepth(ast, model, classDeclarations, methodDeclarations)
		+ noClone(ast, model, classDeclarations, methodDeclarations)
		+ noFinalizer(ast, model, classDeclarations, methodDeclarations)
		+ returnCount(ast, model, classDeclarations, methodDeclarations)
		+ defaultComesLast(ast, model, classDeclarations, methodDeclarations)
		+ fallThrough(ast, model, classDeclarations, methodDeclarations)
		+ multipleStringLiterals(ast, model, classDeclarations, methodDeclarations)
		;
}

list[Message] avoidInlineConditionals(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations) =
	[coding("AvoidInlineConditionals", c@src) | /c:\conditional(_, _, _) := ast];

list[Message] magicNumber(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){
	msgs = [];
 	for( /s: \number(str numberValue) := ast){
 		if(numberValue notin {"-1", "0", "1", "2"}){
 				msgs += coding("MagicNumber", s@src);
 		}
 	}
 	return msgs;
}

list[Message] missingSwitchDefault(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){
	msgs = [];
	
	top-down-break visit(ast){
    	case s: \switch(_, list[Statement] statements):
    		if(!(\defaultCase() in statements)){
    			msgs += coding("MissingSwitchDefault", s@src);
    		}
	}
	return msgs;
}

list[Message] simplifyBooleanExpression(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){
	msgs = [];
	
	void checkBoolConstant(operand){
		if("<operand>" in {"true", "false"}){
			msgs += coding("SimplifyBooleanExpression", operand@src);
		}
	}
	visit(ast){
		case \infix(lhs, "&&", rhs): { checkBoolConstant(lhs); checkBoolConstant(rhs); }
    	case \infix(lhs, "||", rhs): { checkBoolConstant(lhs); checkBoolConstant(rhs); }
   		case \prefix("!",operand): 	 { checkBoolConstant(operand); }
	}
	return msgs;
}

list[Message] simplifyBooleanReturn(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){
	msgs = [];
	
	bool isBooleanReturn (stat) =
		\return(expr) := stat && "<expr>" in {"true", "false"};
		
	visit(ast){
	 	case s: \if(Expression condition, Statement thenBranch, Statement elseBranch): {
	 		if(isBooleanReturn(thenBranch) && isBooleanReturn(elseBranch)){
	 			msgs += coding("SimplifyBooleanReturn", s@src);
	 		}
	 	}
	}
	return msgs;
}

list[Message] stringLiteralEquality(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){
	msgs = [];
	
	bool checkStringLiteral(operand) = stringLiteral(_) := operand;
	
	visit(ast){
		case \infix(lhs, "==", rhs): { checkStringLiteral(lhs); checkStringLiteral(rhs); }
    	case \infix(lhs, "!=", rhs): { checkStringLiteral(lhs); checkStringLiteral(rhs); }
	}
	return msgs;
}

list[Message] nestedForDepth(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){
	msgs = [];
	int limit = 2;
	
	void findViolations(ast2, int nesting){
		if(nesting == limit){
			msgs += coding("NestedForDepth", ast2@src);
		} else {
			top-down-break visit(ast2){
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

list[Message] nestedIfDepth(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){
	msgs = [];
	int limit = 2;
	
	void findViolations(ast2, int nesting){
		if(nesting == limit){
			msgs += coding("NestedIfDepth", ast2@src);
		} else {
			top-down-break visit(ast2){
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

list[Message] nestedTryDepth(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){
	msgs = [];
	int limit = 2;
	
	void findViolations(ast2, int nesting){
		if(nesting == limit){
			msgs += coding("NestedTryDepth", ast2@src);
		} else {
			top-down-break visit(ast2){
				case \try(Statement body, list[Statement] catchClauses): {
			 		findViolations(body, nesting + 1);
			 		for(stat <- catchClauses){
			 			findViolations(stat, nesting);
			 		}
			 	}
    		 	case \try(Statement body, list[Statement] catchClauses, Statement \finally): {
    				findViolations(body, nesting + 1);
			 		for(stat <- catchClauses){
			 			findViolations(stat, nesting);
			 		}
			 		findViolations(\finally, nesting);
		    	}
	        }
	   }
	}
    findViolations(ast, 0);
    return msgs;
}

list[Message] noClone(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){
	overrides = model@methodOverrides;
	msgs = [];
	for(m <- methodDeclarations){	// the loc of the method with and without @Override differ
		if(m.name == "clone"){
			entity = m@decl; //getDeclaredEntity(m@src, model);
			if(!isEmpty(overrides[entity])) { 
				msgs += coding("NoClone", m@src); 
			}
		}
	}
	return msgs;
}

list[Message] noFinalizer(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){
	msgs = [];
	for(m <- methodDeclarations){
		if(m.name == "finalizer"){
			msgs += coding("NoFinalizer", m@src);
		}
	}
	return msgs;
}

list[Message] multipleStringLiterals(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){	// TODO: should exclude strings in annotations
	seen = {};
	msgs = [];
 	for( /s: stringLiteral(str stringValue) := ast){
 		if(stringValue != ""){
 			if(stringValue in seen){
 				msgs += coding("MultipleStringLiterals", s@src);
 			}
 			seen += stringValue;
 		}
 	}
 	return msgs;
}

list[Message] returnCount(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){
	int limit = 2;
	msgs = [];
	
	int countReturns(body){
		cnt = 0;
		visit(body){
			case \return(Expression expression): cnt += 1;
   		 	case \return():	cnt +=1;
   		 }
   		 return cnt;
	}
	for(m <- methodDeclarations){
		if(countReturns(m.impl) > limit) {
    			msgs += coding("ReturnCount", m@src);
    	}
    }
	
	return msgs;
}

list[Message] defaultComesLast(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){
	msgs = [];
	
	bool defComesLast(list[Statement] statements){
		for(i <- index(statements)){
			if(\defaultCase() := statements[i]){
				return i == size(statements) - 2;
			}
		}
		return true;
	}
	
	top-down visit(ast){
    	case s: \switch(_, list[Statement] statements):
    		if(!defComesLast(statements)){
    			msgs += coding("DefaultComesLast", s@src);
    		}
	}
	return msgs;
}

list[Message] fallThrough(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){		// TODO: give no message when fallthru comment is present.
	msgs = [];
	
	bool containsExit(Statement stat){
		visit(stat){
			case \break(): 							return true;
    		case \break(str label): 				return true;
    		case \continue(): 						return true;
    		case \continue(str label): 				return true;
     		case \return(Expression expression): 	return true;
    		case \return(): 						return true;
     		case \throw(Expression expression): 	return true;
		}
		return false;
	}
	
	void findFallThrough(list[Statement] statements){
		for(i <- index(statements)){
			if(\case(Expression expression) := statements[i] && 
			   i+2 < size(statements) &&
			   (\case(Expression expression) := statements[i+2] || \caseDefault() := statements[i+2])){
			   if(!containsExit(statements[i+1])){
			   		msgs += coding("FallThrough", statements[i+1]@src);
			   }
			}
		}
	}
	
	visit(ast){
    	case s: \switch(_, list[Statement] statements):
    		findFallThrough(statements);    		
	}
	return msgs;
}




