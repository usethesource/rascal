module lang::java::style::Strings

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

data Message = string(str category, loc pos);

bool isStringType(Type t) = simpleType(simpleName("String")) := t;
bool isStringType(TypeSymbol ts) = getTypeName(ts) == "String";

bool isStringBufferType(Type t) = simpleType(simpleName("StringBuffer")) := t;
bool isStringBufferType(TypeSymbol ts) = getTypeName(ts) == "StringBuffer";

bool isStringBuilderType(Type t) = simpleType(simpleName("StringBuilder")) := t;
bool isStringBuilderType(TypeSymbol ts) = getTypeName(ts) == "StringBuilder";

bool isStringBufferOrBuilderType(Type t) = isStringBufferType(t) || isStringBuilderType(t);
bool isStringBufferOrBuilderType(TypeSymbol ts) = isStringBufferType(ts) || isStringBuilderType(ts);

/* --- stringInstantiation --------------------------------------------------*/

list[Message] stringInstantiation(Expression exp: \newObject(Type \type, [Expression arg]),  list[Expression] parents, M3 model) =
	isStringType(\type) && isStringType(arg@typ) 
	? [string("StringInstantiation", exp@src) ] 
	: [];

default list[Message] stringInstantiation(Expression exp,  list[Expression] parents, M3 model) = [];

/* --- stringToString -------------------------------------------------------*/

list[Message] stringToString(Expression exp: \methodCall(_, Expression receiver, "toString", []),  list[Expression] parents, M3 model) =
	isStringType(receiver@typ) ? [string("stringToString", exp@src) ] : [];
	
default list[Message] stringToString(Expression exp,  list[Expression] parents, M3 model) = [];

/* --- inefficientStringBuffering -------------------------------------------*/

list[Message] inefficientStringBuffering(Expression exp: \newObject(Type \type, [Expression arg]),  list[Expression] parents, M3 model) =
	isStringBufferOrBuilderType(\type) && !isStringLiteral(arg) 
	? [string("InefficientStringBuffering", exp@src) ] 
	: [];
	
list[Message] inefficientStringBuffering(Expression exp: \methodCall(_, Expression receiver, "append", [Expression arg]),  list[Expression] parents, M3 model) =
	isStringBufferOrBuilderType(receiver@typ) && !isStringLiteral(arg) 
	? [string("InefficientStringBuffering", exp@src) ] 
	: [];

default list[Message] inefficientStringBuffering(Expression exp,  list[Expression] parents, M3 model) = [];

/* --- unnnessaryCaseChange -------------------------------------------------*/

list[Message] unnnessaryCaseChange(Expression exp: \methodCall(_, Expression receiver, "equals", [Expression arg]),  list[Expression] parents, M3 model) =
	isStringType(receiver@typ) && \methodCall(_, _, "toUpperCase", []) := receiver 
	? [string("UnnnessaryCaseChange", exp@src) ] 
	: [];
	
default list[Message] unnnessaryCaseChange(Expression exp,  list[Expression] parents, M3 model) = [];	

/* --- useStringBufferLength ------------------------------------------------*/

list[Message] useStringBufferLength(Expression exp: \methodCall(_, Expression receiver1, "equals", [Expression arg]),  list[Expression] parents, M3 model) =	
	isEmptyStringLiteral(arg) && isStringType(receiver1@typ) && \methodCall(_, receiver2, "toString", []) := receiver1 && isStringBufferType(receiver2@typ)
	? [string("UseStringBufferLength", exp@src) ] 
	: [];

default list[Message] useStringBufferLength(Expression exp,  list[Expression] parents, M3 model) = [];

/* --- appendCharacterWithChar ----------------------------------------------*/

list[Message] appendCharacterWithChar(Expression exp: \methodCall(_, Expression receiver, "append", [Expression arg]),  list[Expression] parents, M3 model) =
	isStringBufferOrBuilderType(receiver@typ) && isStringLiteral(arg) && size(getStringLiteralValue(arg)) == 1 
	? [string("AppendCharacterWithChar", exp@src) ] 
	: [];

default list[Message] appendCharacterWithChar(Expression exp,  list[Expression] parents, M3 model) = [];

/* --- consecutiveLiteralAppends --------------------------------------------*/

list[Message] consecutiveLiteralAppends(Expression exp: \methodCall(_, Expression receiver1, "append", [Expression arg1]),  list[Expression] parents, M3 model) =	
	isStringLiteral(arg1) && isStringBufferOrBuilderType(receiver1@typ) && \methodCall(_, receiver2, "append", [arg2]) := receiver1 && isStringBufferOrBuilderType(receiver2@typ)
	? [string("ConsecutiveLiteralAppends", exp@src) ] 
	: [];
	
default list[Message] consecutiveLiteralAppends(Expression exp,  list[Expression] parents, M3 model) = [];

/* --- useIndexOfChar -------------------------------------------------------*/

list[Message] useIndexOfChar(Expression exp: \methodCall(_, Expression receiver, "indexOf", [Expression arg]),  list[Expression] parents, M3 model) =
	isStringType(receiver@typ) && isStringLiteral(arg) && size(getStringLiteralValue(arg)) == 1 
	? [string("UseIndexOfChar", exp@src) ] 
	: [];

list[Message] useIndexOfChar(Expression exp,  list[Expression] parents, M3 model) = [];

/* --- inefficientEmptyStringCheck ------------------------------------------*/

list[Message] inefficientEmptyStringCheck(Expression exp: \methodCall(_, Expression receiver1, "length", []),  list[Expression] parents, M3 model) =	
	isStringType(receiver1@typ) && \methodCall(_, receiver2, "trim", []) := receiver1 && isStringType(receiver2@typ)
	? [string("InefficientEmptyStringCheck", exp@src) ] 
	: [];
	
default list[Message] inefficientEmptyStringCheck(Expression exp, list[Expression] parents, M3 model) = [];

/* --- avoidStringBufferField -----------------------------------------------*/

list[Message] avoidStringBufferField(Declaration decl:  \field(Type \type, list[Expression] fragments), list[Declaration] parents, M3 model) =	
	isStringBufferOrBuilderType(\type) && \private() in (decl@modifiers ? {})
	? [string("AvoidStringBufferField", decl@src) ] 
	: [];