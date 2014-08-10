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


list[Message] avoidInlineConditionals(Expression exp: \conditional(_, _, _),  list[Expression] parents, node ast, M3 model) =
	[coding("AvoidInlineConditionals", exp@src)];
	
default list[Message] avoidInlineConditionals(Expression exp,  list[Expression] parents, node ast, M3 model) = [];

//list[Message] avoidInlineConditionals(node ast, M3 model, OuterDeclarations decls) =
//	[coding("AvoidInlineConditionals", c@src) | /c:\conditional(_, _, _) := ast];

list[Message] magicNumber(Expression exp: \number(str numberValue),  list[Expression] parents, node ast, M3 model) =
	(numberValue notin {"-1", "0", "1", "2"}) ? [coding("MagicNumber", exp@src)] : [];

default list[Message] magicNumber(Expression exp,  list[Expression] parents, node ast, M3 model) = [];

//list[Message] magicNumber(node ast, M3 model, OuterDeclarations decls){
//	msgs = [];
// 	for( /s: \number(str numberValue) := ast){
// 		if(numberValue notin {"-1", "0", "1", "2"}){
// 				msgs += coding("MagicNumber", s@src);
// 		}
// 	}
// 	return msgs;
//}

list[Message] missingSwitchDefault(Statement stat: \switch(_, list[Statement] statements),  list[Statement] parents, node ast, M3 model) =
	(!(\defaultCase() in statements)) ? [coding("MissingSwitchDefault", stat@src)] : [];
    	
default list[Message] missingSwitchDefault(Statement stat: \switch(_, list[Statement] statements),  list[Statement] parents, node ast, M3 model) = [];
//list[Message] missingSwitchDefault(node ast, M3 model, OuterDeclarations decls){
//	msgs = [];
//	
//	top-down-break visit(ast){
//    	case s: \switch(_, list[Statement] statements):
//    		if(!(\defaultCase() in statements)){
//    			msgs += coding("MissingSwitchDefault", s@src);
//    		}
//	}
//	return msgs;
//}

bool isBooleanLiteral(Expression e) = \booleanLiteral(_) := e;

list[Message] simplifyBooleanExpression(Expression exp: \infix(lhs, "&&", rhs),  list[Expression] parents, node ast, M3 model) =
	(isBooleanLiteral(lhs) ? [coding("SimplifyBooleanExpression", lhs@src)] : []) +
	(isBooleanLiteral(rhs) ? [coding("SimplifyBooleanExpression", rhs@src)] : []);

list[Message] simplifyBooleanExpression(Expression exp: \infix(lhs, "||", rhs),  list[Expression] parents, node ast, M3 model) =
	(isBooleanLiteral(lhs) ? [coding("SimplifyBooleanExpression", lhs@src)] : []) +
	(isBooleanLiteral(rhs) ? [coding("SimplifyBooleanExpression", rhs@src)] : []);

list[Message] simplifyBooleanExpression(Expression exp: \prefix("|", operand),  list[Expression] parents, node ast, M3 model) =
	(isBooleanLiteral(operand) ? [coding("SimplifyBooleanExpression", operand@src)] : []);

default list[Message] simplifyBooleanExpression(Expression exp,  list[Expression] parents, node ast, M3 model) = [];

//list[Message] simplifyBooleanExpression(node ast, M3 model, OuterDeclarations decls){
//	msgs = [];
//	
//	void checkBoolConstant(operand){
//		if("<operand>" in {"true", "false"}){
//			msgs += coding("SimplifyBooleanExpression", operand@src);
//		}
//	}
//	visit(ast){
//		case \infix(lhs, "&&", rhs): { checkBoolConstant(lhs); checkBoolConstant(rhs); }
//    	case \infix(lhs, "||", rhs): { checkBoolConstant(lhs); checkBoolConstant(rhs); }
//   		case \prefix("!",operand): 	 { checkBoolConstant(operand); }
//	}
//	return msgs;
//}

bool isBooleanReturn (stat) = \return(booleanLiteral(_)) := stat;

list[Message] simplifyBooleanReturn(Statement stat: \if(Expression condition, Statement thenBranch, Statement elseBranch),  list[Statement] parents, node ast, M3 model) =
	(isBooleanReturn(thenBranch) && isBooleanReturn(elseBranch)) ? [coding("SimplifyBooleanReturn", stat@src)] : [];

default list[Message] simplifyBooleanReturn(Statement stat, list[Expression] parents, node ast, M3 model) = [];

//list[Message] simplifyBooleanReturn(node ast, M3 model, OuterDeclarations decls){
//	msgs = [];
//	
//	bool isBooleanReturn (stat) =
//		\return(expr) := stat && "<expr>" in {"true", "false"};
//		
//	visit(ast){
//	 	case s: \if(Expression condition, Statement thenBranch, Statement elseBranch): {
//	 		if(isBooleanReturn(thenBranch) && isBooleanReturn(elseBranch)){
//	 			msgs += coding("SimplifyBooleanReturn", s@src);
//	 		}
//	 	}
//	}
//	return msgs;
//}

bool isStringLiteral(Expression e) = stringLiteral(_) := e;

list[Message] stringLiteralEquality(Expression exp: \infix(lhs, "==", rhs),  list[Expression] parents, node ast, M3 model) =
	(isStringLiteral(lhs) || isStringLiteral(rhs)) ? [coding("StringLiteralEquality", exp@src)] : [];

list[Message] stringLiteralEquality(Expression exp: \infix(lhs, "!=", rhs),  list[Expression] parents, node ast, M3 model) =
	(isStringLiteral(lhs) || isStringLiteral(rhs)) ? [coding("StringLiteralEquality", exp@src)] : [];

default list[Message] stringLiteralEquality(Expression exp,  list[Expression] parents, node ast, M3 model) = [];

//list[Message] stringLiteralEquality(node ast, M3 model, OuterDeclarations decls){
//	msgs = [];
//	
//	bool checkStringLiteral(operand) = stringLiteral(_) := operand;
//	
//	visit(ast){
//		case \infix(lhs, "==", rhs): { checkStringLiteral(lhs); checkStringLiteral(rhs); }
//    	case \infix(lhs, "!=", rhs): { checkStringLiteral(lhs); checkStringLiteral(rhs); }
//	}
//	return msgs;
//}
//
int countForNesting(list[Statement] parents){
	int nesting = 0;
	for(parent <- parents){
		switch(parent){
			case \foreach(_, _, _): 	
				nesting += 1;
			case \for(_, _, _, _) : 	
		    	nesting += 1;
			case \for(_, _, _): 		
		    	nesting += 1;
		 }
	}
	return nesting;
}

list[Message] nestedForDepth(Statement stat: \foreach(_, _, _),  list[Statement] parents, node ast, M3 model) =
	countForNesting(parents) > 0 ? [coding("NestedForDepth", stat@src)] : [];
	
list[Message] nestedForDepth(Statement stat: \for(_, _, _, _),  list[Statement] parents, node ast, M3 model) =
	countForNesting(parents) > 0 ? [coding("NestedForDepth", stat@src)] : [];
	
list[Message] nestedForDepth(Statement stat: \for(_, _, _),  list[Statement] parents, node ast, M3 model) =
	countForNesting(parents) > 0 ? [coding("NestedForDepth", stat@src)] : [];

default list[Message] nestedForDepth(Statement stat,  list[Statement] parents, node ast, M3 model) = [];

//list[Message] nestedForDepth(node ast, M3 model, OuterDeclarations decls){
//	msgs = [];
//	int limit = 2;
//	
//	void findViolations(ast2, int nesting){
//		if(nesting == limit){
//			msgs += coding("NestedForDepth", ast2@src);
//		} else {
//			top-down-break visit(ast2){
//				case \foreach(_, _, body): 	
//					findViolations(body, nesting + 1);
//		    	case \for(_, _, _, body) : 	
//		    		findViolations(body, nesting + 1);
//		    	case \for(_, _, body): 		
//		    		findViolations(body, nesting + 1);
//		    }
//	    }
//	}
//    findViolations(ast, 0);
//    return msgs;
//}

int countIfNesting(list[Statement] parents){
	int nesting = 0;
	for(parent <- parents){
		switch(parent){
			case  \if(_, _): 	
				nesting += 1;
			case \if(_, _, _) : 	
		    	nesting += 1;
		 }
	}
	return nesting;
}

list[Message] nestedIfDepth(Statement stat: \if(_, _),  list[Statement] parents, node ast, M3 model) =
	countIfNesting(parents) > 0 ? [coding("NestedIfDepth", stat@src)] : [];
	
list[Message] nestedIfDepth(Statement stat: \if(_, _, _),  list[Statement] parents, node ast, M3 model) =
	countIfNesting(parents) > 0 ? [coding("NestedIfDepth", stat@src)] : [];
	
default list[Message] nestedIfDepth(Statement stat,  list[Statement] parents, node ast, M3 model) = [];	

//list[Message] nestedIfDepth(node ast, M3 model, OuterDeclarations decls){
//	msgs = [];
//	int limit = 2;
//	
//	void findViolations(ast2, int nesting){
//		if(nesting == limit){
//			msgs += coding("NestedIfDepth", ast2@src);
//		} else {
//			top-down-break visit(ast2){
//				case \if(_, thenPart): 			
//					findViolations(thenPart, nesting + 1);
//    			case \if(_, thenPart, elsePart): { 
//    				findViolations(thenPart, nesting + 1);
//    				findViolations(elsePart, nesting + 1);
//    			}
//		    }
//	    }
//	}
//    findViolations(ast, 0);
//    return msgs;
//}

int countTryNesting(list[Statement] parents){
	int nesting = 0;
	for(parent <- parents){
		if(\try(_,_) := parent || \try(_,_,_) := parent){
		    nesting += 1;
		}
	}
	return nesting;
}

list[Message] nestedTryDepth(Statement stat: \try(_, _),  list[Statement] parents, node ast, M3 model) =
	countTryNesting(parents) > 0 ? [coding("NestedTryDepth", stat@src)] : [];

list[Message] nestedTryDepth(Statement stat: \try(_, _, _),  list[Statement] parents, node ast, M3 model) =
	countTryNesting(parents) > 0 ? [coding("NestedTryDepth", stat@src)] : [];

default list[Message] nestedTryDepth(Statement stat,  list[Statement] parents, node ast, M3 model) = [];

//list[Message] nestedTryDepth(node ast, M3 model, OuterDeclarations decls){
//	msgs = [];
//	int limit = 2;
//	
//	void findViolations(ast2, int nesting){
//		if(nesting == limit){
//			msgs += coding("NestedTryDepth", ast2@src);
//		} else {
//			top-down-break visit(ast2){
//				case \try(Statement body, list[Statement] catchClauses): {
//			 		findViolations(body, nesting + 1);
//			 		for(stat <- catchClauses){
//			 			findViolations(stat, nesting);
//			 		}
//			 	}
//    		 	case \try(Statement body, list[Statement] catchClauses, Statement \finally): {
//    				findViolations(body, nesting + 1);
//			 		for(stat <- catchClauses){
//			 			findViolations(stat, nesting);
//			 		}
//			 		findViolations(\finally, nesting);
//		    	}
//	        }
//	   }
//	}
//    findViolations(ast, 0);
//    return msgs;
//}

list[Message] checkNoClone(Declaration m, M3 model){
	overrides = model@methodOverrides;
	entity = m@decl;
	return (m.name == "clone" && !isEmpty(overrides[entity])) ? [coding("NoClone", m@src)] : [];
}

list[Message] noClone(Declaration m: \method(_, _, _, _),  list[Declaration] parents, node ast, M3 model) =
	checkNoClone(m, model);
	
list[Message] noClone(Declaration m: \method(_, _, _, _, _),  list[Declaration] parents, node ast, M3 model) =
	checkNoClone(m, model); 
	
default list[Message] noClone(Declaration m,  list[Declaration] parents, node ast, M3 model) = [];	
	
//list[Message] noClone(node ast, M3 model, OuterDeclarations decls){
//	overrides = model@methodOverrides;
//	msgs = [];
//	for(m <- decls.allMethods){	// the loc of the method with and without @Override differ
//		if(m.name == "clone"){
//			entity = m@decl; //getDeclaredEntity(m@src, model);
//			if(!isEmpty(overrides[entity])) { 
//				msgs += coding("NoClone", m@src); 
//			}
//		}
//	}
//	return msgs;
//}

list[Message] noFinalizer(Declaration m: \method(_, _, _, _),  list[Declaration] parents, node ast, M3 model) =
	(m.name == "finalizer") ? [coding("NoFinalizer", m@src)] : [];
	
list[Message] noFinalizer(Declaration m: \method(_, _, _, _, _),  list[Declaration] parents, node ast, M3 model) =
	(m.name == "finalizer") ? [coding("NoFinalizer", m@src)] : [];
	
default list[Message] noFinalizer(Declaration decl,  list[Declaration] parents, node ast, M3 model) = [];	
	
//list[Message] noFinalizer(node ast, M3 model, OuterDeclarations decls){
//	msgs = [];
//	for(m <- decls.allMethods){
//		if(m.name == "finalizer"){
//			msgs += coding("NoFinalizer", m@src);
//		}
//	}
//	return msgs;
//}

set[str] seenStringLiterals = {};	// TODO do proper reset

list[Message] multipleStringLiterals(Expression exp: stringLiteral(str stringValue),  list[Expression] parents, node ast, M3 model) {
	if(stringValue != ""){
 		if(stringValue in seenStringLiterals){
 				return [coding("MultipleStringLiterals", exp@src)];
 			}
 		seenStringLiterals += stringValue;
 		return [];
 	}
}
default list[Message] multipleStringLiterals(Expression exp,  list[Expression] parents, node ast, M3 model) = [];

//list[Message] multipleStringLiterals(node ast, M3 model, OuterDeclarations decls){	// TODO: should exclude strings in annotations
//	seen = {};
//	msgs = [];
// 	for( /s: stringLiteral(str stringValue) := ast){
// 		if(stringValue != ""){
// 			if(stringValue in seen){
// 				msgs += coding("MultipleStringLiterals", s@src);
// 			}
// 			seen += stringValue;
// 		}
// 	}
// 	return msgs;
//}

list[Message] returnCount(Statement stat: \return(_),  list[Statement] parents, node ast, M3 model){
	updateCheckState("returnCount", 1);
	return [];
}

list[Message] returnCount(Statement stat: \return(),  list[Statement] parents, node ast, M3 model){
	updateCheckState("returnCount", 1);
	return [];
}

default list[Message] returnCount(Statement stat,  list[Statement] parents, node ast, M3 model) = [];

value updateReturnCount(value current, value delta) { if(int c := current && int d := delta) return c + 1; }

list[Message] finalizeReturnCount(Declaration d, value current) =
	(int n := current && n > 1) ? [coding("ReturnCount", d@src)] : [];

//list[Message] returnCount(node ast, M3 model, OuterDeclarations decls){
//	int limit = 2;
//	msgs = [];
//	
//	int countReturns(body){
//		cnt = 0;
//		visit(body){
//			case \return(Expression expression): cnt += 1;
//   		 	case \return():	cnt +=1;
//   		 }
//   		 return cnt;
//	}
//	for(m <- decls.allMethods){
//		if(m has impl && countReturns(m.impl) > limit) {
//    			msgs += coding("ReturnCount", m@src);
//    	}
//    }
//	
//	return msgs;
//}

bool defComesLast(list[Statement] statements){
	for(i <- index(statements)){
		if(\defaultCase() := statements[i]){
			return i == size(statements) - 2;
		}
	}
	return true;
}

list[Message] defaultComesLast(Statement stat: \switch(_, list[Statement] statements),  list[Statement] parents, node ast, M3 model) =
	!defComesLast(statements) ? [coding("DefaultComesLast", stat@src)] : [];
	
default list[Message] defaultComesLast(Statement stat,  list[Statement] parents, node ast, M3 model) = [];	

//list[Message] defaultComesLast(node ast, M3 model, OuterDeclarations decls){
//	msgs = [];
//	
//	bool defComesLast(list[Statement] statements){
//		for(i <- index(statements)){
//			if(\defaultCase() := statements[i]){
//				return i == size(statements) - 2;
//			}
//		}
//		return true;
//	}
//	
//	top-down visit(ast){
//    	case s: \switch(_, list[Statement] statements):
//    		if(!defComesLast(statements)){
//    			msgs += coding("DefaultComesLast", s@src);
//    		}
//	}
//	return msgs;
//}



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

list[Message] findFallThrough(list[Statement] statements){
	msgs = [];
	for(i <- index(statements)){
		if(\case(Expression expression) := statements[i] && 
		   i+2 < size(statements) &&
		   (\case(Expression expression) := statements[i+2] || \caseDefault() := statements[i+2])){
		   if(!containsExit(statements[i+1])){
		   		msgs += coding("FallThrough", statements[i+1]@src);
		   }
		}
	}
	return msgs;
}

list[Message] fallThrough(Statement stat: \switch(_, list[Statement] statements),  list[Statement] parents, node ast, M3 model) =
	findFallThrough(statements);  

default list[Message] fallThrough(Statement stat,  list[Statement] parents, node ast, M3 model) = [];

//list[Message] fallThrough(node ast, M3 model, OuterDeclarations decls){		// TODO: give no message when fallthru comment is present.
//	msgs = [];
//	
//	bool containsExit(Statement stat){
//		visit(stat){
//			case \break(): 							return true;
//    		case \break(str label): 				return true;
//    		case \continue(): 						return true;
//    		case \continue(str label): 				return true;
//     		case \return(Expression expression): 	return true;
//    		case \return(): 						return true;
//     		case \throw(Expression expression): 	return true;
//		}
//		return false;
//	}
//	
//	void findFallThrough(list[Statement] statements){
//		for(i <- index(statements)){
//			if(\case(Expression expression) := statements[i] && 
//			   i+2 < size(statements) &&
//			   (\case(Expression expression) := statements[i+2] || \caseDefault() := statements[i+2])){
//			   if(!containsExit(statements[i+1])){
//			   		msgs += coding("FallThrough", statements[i+1]@src);
//			   }
//			}
//		}
//	}
//	
//	visit(ast){
//    	case s: \switch(_, list[Statement] statements):
//    		findFallThrough(statements);    		
//	}
//	return msgs;
//}




