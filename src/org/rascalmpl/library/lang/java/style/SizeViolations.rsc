module lang::java::style::SizeViolations


import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;
import List;
import IO;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

import lang::java::style::Utils;
import lang::java::style::CheckStates;

data Message = sizeViolation(str category, loc pos);

/*
ExecutableStatementCount	DONE
FileLength					DONE
LineLength					TBD
MethodLength				DONE
AnonInnerLength				TBD
ParameterNumber				DONE
OuterTypeNumber				TBD
MethodCount					DONE
*/

/* --- executableStatementCount ---------------------------------------------*/

//list[Message] executableStatementCount(node ast, M3 model, OuterDeclarations decls){
//	msgs = [];
//	void checkSize(Statement ast){
//		if(size([s | /Statement s := ast]) > 30){
//			msgs += sizeViolation("ExecutableStatementCount", ast@src);
//		}
//	}
//	top-down-break visit(ast){
//		case \initializer(Statement initializerBody): checkSize(initializerBody);
//    	case \method(_, _, _, _, Statement impl): 	checkSize(impl);
//    	case \constructor(_, _, _, Statement impl): checkSize(impl);
//	}
//	return msgs;
//}

/* --- fileLength -----------------------------------------------------------*/

list[Message] fileLength(Declaration m,  list[Declaration] parents, M3 model) =
	(m@src.end.line > 2000) ?  [sizeViolation("FileLength", m@src)] : [];

/* --- methodLength ---------------------------------------------------------*/

int getSize(Declaration d) = d@src.end.line - d@src.begin.line;

list[Message] methodLength(Declaration m: \method(_,_,_,_,_),  list[Declaration] parents, M3 model) =
	(getSize(m) > 150) ? [sizeViolation("MethodLength", m@src)] : [];

    
/* --- parameterNumber ------------------------------------------------------*/

list[Message] parameterNumber(Declaration m: \method(_,_,parameters,_,_),  list[Declaration] parents, M3 model) =
	size(parameters) > 7 ? [sizeViolation("ParameterNumber", m@src)] : [];

list[Message] parameterNumber(Declaration m: \method(_,_,parameters,_),  list[Declaration] parents, M3 model) =
	size(parameters) > 7 ? [sizeViolation("ParameterNumber", m@src)] : [];

default list[Message] parameterNumber(Declaration m,  list[Declaration] parents, M3 model) = [];
	
/* --- methodCount ----------------------------------------------------------*/

// TODO: this check should be refined per method category: maxTotal, maxPrivate, maxPackage, maxProtected, maxPublic

list[Message] methodCount(Declaration d: \method(_, _, _, _, _), list[Declaration] parents, M3 model) {
	updateCheckState("methodCount", 1);
	return [];
}

list[Message] methodCount(Declaration d: \method(_, _, _, _), list[Declaration] parents, M3 model) {
	updateCheckState("methodCount", 1);
	return [];
}

default list[Message] methodCount(Declaration d, list[Declaration] parents, M3 model) = [];

// update/finalize

value updateMethodCount(value current, value delta) { if(int n := current && int d := delta) return n + d; }

list[Message] finalizeMethodCount(Declaration d, value current) =
	(int n := current && n > 100) ? [sizeViolation("MethodCount", d@src) ] : [];
	
/* --- fieldCount ----------------------------------------------------------*/

list[Message] fieldCount(Declaration d:  \field(Type \type, list[Expression] fragments), list[Declaration] parents, M3 model) {
	updateCheckState("fieldCount", 1);
	return [];
}
default list[Message] fieldCount(Declaration d, list[Declaration] parents, M3 model) = [];

// update/finalize

value updateFieldCount(value current, value delta) { if(int n := current && int d := delta) return n + d; }

list[Message] finalizeFieldCount(Declaration d, value current) =
	(int n := current && n > 15) ? [sizeViolation("FieldCount", d@src) ] : [];
	
/* --- publicCount ----------------------------------------------------------*/

list[Message] publicCount(Declaration d, list[Declaration] parents, M3 model) {
	if(\public() in (d@modifiers ? {})){
		updateCheckState("publicCount", 1);
	}
	return [];
}

// update/finalize

value updatePublicCount(value current, value delta) { if(int n := current && int d := delta) return n + d; }

list[Message] finalizePublicCount(Declaration d, value current) =
	(int n := current && n > 45) ? [sizeViolation("PublicCount", d@src) ] : [];
