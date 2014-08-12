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

/* --- avoidInlineConditionals ----------------------------------------------*/

list[Message] avoidInlineConditionals(Expression exp: \conditional(_, _, _),  list[Expression] parents, M3 model) =
	[coding("AvoidInlineConditionals", exp@src)];
	
default list[Message] avoidInlineConditionals(Expression exp,  list[Expression] parents, M3 model) = [];

/* --- magicNumber ----------------------------------------------------------*/

list[Message] magicNumber(Expression exp: \number(str numberValue),  list[Expression] parents, M3 model) =
	(numberValue notin {"-1", "0", "1", "2"}) ? [coding("MagicNumber", exp@src)] : [];

default list[Message] magicNumber(Expression exp,  list[Expression] parents, M3 model) = [];

/* --- missingSwitchDefault -------------------------------------------------*/

list[Message] missingSwitchDefault(Statement stat: \switch(_, list[Statement] statements),  list[Statement] parents, M3 model) =
	(!(\defaultCase() in statements)) ? [coding("MissingSwitchDefault", stat@src)] : [];
    	
default list[Message] missingSwitchDefault(Statement stat: \switch(_, list[Statement] statements),  list[Statement] parents, M3 model) = [];

/* --- simplifyBooleanExpression --------------------------------------------*/

list[Message] simplifyBooleanExpression(Expression exp: \infix(lhs, "&&", rhs),  list[Expression] parents, M3 model) =
	(isBooleanLiteral(lhs) ? [coding("SimplifyBooleanExpression", lhs@src)] : []) +
	(isBooleanLiteral(rhs) ? [coding("SimplifyBooleanExpression", rhs@src)] : []);

list[Message] simplifyBooleanExpression(Expression exp: \infix(lhs, "||", rhs),  list[Expression] parents, M3 model) =
	(isBooleanLiteral(lhs) ? [coding("SimplifyBooleanExpression", lhs@src)] : []) +
	(isBooleanLiteral(rhs) ? [coding("SimplifyBooleanExpression", rhs@src)] : []);

list[Message] simplifyBooleanExpression(Expression exp: \prefix("|", operand),  list[Expression] parents, M3 model) =
	(isBooleanLiteral(operand) ? [coding("SimplifyBooleanExpression", operand@src)] : []);

default list[Message] simplifyBooleanExpression(Expression exp,  list[Expression] parents, M3 model) = [];

/* --- simplifyBooleanReturn ------------------------------------------------*/

bool isBooleanReturn (stat) = \return(booleanLiteral(_)) := stat;

list[Message] simplifyBooleanReturn(Statement stat: \if(Expression condition, Statement thenBranch, Statement elseBranch),  list[Statement] parents, M3 model) =
	(isBooleanReturn(thenBranch) && isBooleanReturn(elseBranch)) ? [coding("SimplifyBooleanReturn", stat@src)] : [];

default list[Message] simplifyBooleanReturn(Statement stat, list[Expression] parents, M3 model) = [];

/* --- stringLiteralEquality ------------------------------------------------*/

list[Message] stringLiteralEquality(Expression exp: \infix(lhs, "==", rhs),  list[Expression] parents, M3 model) =
	(isStringLiteral(lhs) || isStringLiteral(rhs)) ? [coding("StringLiteralEquality", exp@src)] : [];

list[Message] stringLiteralEquality(Expression exp: \infix(lhs, "!=", rhs),  list[Expression] parents, M3 model) =
	(isStringLiteral(lhs) || isStringLiteral(rhs)) ? [coding("StringLiteralEquality", exp@src)] : [];

default list[Message] stringLiteralEquality(Expression exp,  list[Expression] parents, M3 model) = [];

/* --- nestedForDepth -------------------------------------------------------*/

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

list[Message] nestedForDepth(Statement stat: \foreach(_, _, _),  list[Statement] parents, M3 model) =
	countForNesting(parents) > 0 ? [coding("NestedForDepth", stat@src)] : [];
	
list[Message] nestedForDepth(Statement stat: \for(_, _, _, _),  list[Statement] parents, M3 model) =
	countForNesting(parents) > 0 ? [coding("NestedForDepth", stat@src)] : [];
	
list[Message] nestedForDepth(Statement stat: \for(_, _, _),  list[Statement] parents, M3 model) =
	countForNesting(parents) > 0 ? [coding("NestedForDepth", stat@src)] : [];

default list[Message] nestedForDepth(Statement stat,  list[Statement] parents, M3 model) = [];

/* --- nestedIfDepth --------------------------------------------------------*/

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

list[Message] nestedIfDepth(Statement stat: \if(_, _),  list[Statement] parents, M3 model) =
	countIfNesting(parents) > 0 ? [coding("NestedIfDepth", stat@src)] : [];
	
list[Message] nestedIfDepth(Statement stat: \if(_, _, _),  list[Statement] parents, M3 model) =
	countIfNesting(parents) > 0 ? [coding("NestedIfDepth", stat@src)] : [];
	
default list[Message] nestedIfDepth(Statement stat,  list[Statement] parents, M3 model) = [];	

/* --- nestedTryDepth -------------------------------------------------------*/

int countTryNesting(list[Statement] parents){
	int nesting = 0;
	for(parent <- parents){
		if(\try(_,_) := parent || \try(_,_,_) := parent){
		    nesting += 1;
		}
	}
	return nesting;
}

list[Message] nestedTryDepth(Statement stat: \try(_, _),  list[Statement] parents, M3 model) =
	countTryNesting(parents) > 0 ? [coding("NestedTryDepth", stat@src)] : [];

list[Message] nestedTryDepth(Statement stat: \try(_, _, _),  list[Statement] parents, M3 model) =
	countTryNesting(parents) > 0 ? [coding("NestedTryDepth", stat@src)] : [];

default list[Message] nestedTryDepth(Statement stat,  list[Statement] parents, M3 model) = [];

/* --- noClone --------------------------------------------------------------*/

list[Message] checkNoClone(Declaration m, M3 model){
	overrides = model@methodOverrides;
	entity = m@decl;
	return (m.name == "clone" && !isEmpty(overrides[entity])) ? [coding("NoClone", m@src)] : [];
}

list[Message] noClone(Declaration m: \method(_, _, _, _),  list[Declaration] parents, M3 model) =
	checkNoClone(m, model);
	
list[Message] noClone(Declaration m: \method(_, _, _, _, _),  list[Declaration] parents, M3 model) =
	checkNoClone(m, model); 
	
default list[Message] noClone(Declaration m,  list[Declaration] parents, M3 model) = [];	

/* --- noFinalizer ----------------------------------------------------------*/

list[Message] noFinalizer(Declaration m: \method(_, _, _, _),  list[Declaration] parents, M3 model) =
	(m.name == "finalizer") ? [coding("NoFinalizer", m@src)] : [];
	
list[Message] noFinalizer(Declaration m: \method(_, _, _, _, _),  list[Declaration] parents, M3 model) =
	(m.name == "finalizer") ? [coding("NoFinalizer", m@src)] : [];
	
default list[Message] noFinalizer(Declaration decl,  list[Declaration] parents, M3 model) = [];	

/* --- multipleStringLiterals -----------------------------------------------*/

// TODO do proper reset per class or compilationUnit

set[str] seenStringLiterals = {};

list[Message] multipleStringLiterals(Expression exp: stringLiteral(str stringValue),  list[Expression] parents, M3 model) {
	if(stringValue != ""){
 		if(stringValue in seenStringLiterals){
 				return [coding("MultipleStringLiterals", exp@src)];
 			}
 		seenStringLiterals += stringValue;
 		return [];
 	}
}
default list[Message] multipleStringLiterals(Expression exp,  list[Expression] parents, M3 model) = [];

/* --- returnCount ----------------------------------------------------------*/

list[Message] returnCount(Statement stat: \return(_),  list[Statement] parents, M3 model){
	updateCheckState("returnCount", 1);
	return [];
}

list[Message] returnCount(Statement stat: \return(),  list[Statement] parents, M3 model){
	updateCheckState("returnCount", 1);
	return [];
}

default list[Message] returnCount(Statement stat,  list[Statement] parents, M3 model) = [];

// update/finalize

value updateReturnCount(value current, value delta) { if(int c := current && int d := delta) return c + 1; }

list[Message] finalizeReturnCount(Declaration d, value current) =
	(int n := current && n > 1) ? [coding("ReturnCount", d@src)] : [];

/* --- defaultComesLast -----------------------------------------------------*/

bool defComesLast(list[Statement] statements){
	for(i <- index(statements)){
		if(\defaultCase() := statements[i]){
			return i == size(statements) - 2;
		}
	}
	return true;
}

list[Message] defaultComesLast(Statement stat: \switch(_, list[Statement] statements),  list[Statement] parents, M3 model) =
	!defComesLast(statements) ? [coding("DefaultComesLast", stat@src)] : [];
	
default list[Message] defaultComesLast(Statement stat,  list[Statement] parents, M3 model) = [];	

/* --- fallThrough ----------------------------------------------------------*/

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

list[Message] fallThrough(Statement stat: \switch(_, list[Statement] statements),  list[Statement] parents, M3 model) =
	findFallThrough(statements);  

default list[Message] fallThrough(Statement stat,  list[Statement] parents, M3 model) = [];