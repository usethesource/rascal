module lang::java::style::Utils

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;
import List;
import Set;
import IO;
import Node;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

/* --- various utilities ----------------------------------------------------*/

str getTypeName(TypeSymbol tp){
	if(tp has decl){
		p = tp.decl.path;
		res = p[findLast(p, "/")+1 .. ];
		//println("getName: <tp>, <res>");
		return res;
	} else {
		res = getName(tp);
		//println("getName: <tp>, <res>");
		return res;
	}
}

str getTypeName(loc s) = s.path[1..];

bool isBooleanExpression(Expression e: \infix(_, str operator, _)) = operator in {"&&", "&", "||", "|", "^"};
bool isBooleanExpression(Expression e: \prefix("!", _)) = true;
default bool isBooleanExpression(Expression e) = false;

bool isBooleanLiteral(Expression e) = \booleanLiteral(_) := e;

bool isStringLiteral(Expression e) = stringLiteral(_) := e;
bool isEmptyStringLiteral(Expression e) = stringLiteral("\"\"") := e;

str getStringLiteralValue(stringLiteral(s)) = s[1 .. -1]; // strip quotes

bool isEmptyStatement(empty()) = true;
bool isEmptyStatement(block([])) = true;
default bool isEmptyStatement(Statement _) = false;

// Create a unique name for a constructor
str getConstructor(node nd) = "<getName(nd)><arity(nd)>";

// Convert structured packgae name to string

str getPackageName(p: \package(_)) = packageName2String(p);
str getPackageName(p: \package(_, _)) = packageName2String(p);
default str getPackageName(Declaration d){
	throw "cannot get packgae name from <d>";
}

str packageName2String(package(str name)) = name;
str packageName2String(package(Declaration parentPackage, str name)) = "<packageName2String(parentPackage)>.<name>";
