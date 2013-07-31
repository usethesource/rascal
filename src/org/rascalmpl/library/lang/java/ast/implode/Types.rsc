module lang::java::ast::implode::Types

import lang::java::\syntax::Java15;

import experiments::m3::AST;
import experiments::m3::Core;

public Type implode("interfaceType"(s, a)) = size(a) == 0 ? simpleType("<s>") : parameterizedType(simpleType("<s>"), typeArgs(a));
public Type implode("classOrInterfaceType"(s, a)) = size(a) == 0 ? simpleType("<s>") : parameterizedType(simpleType("<s>"), typeArgs(a));

public Type implode("wildCard"(w)) = size(w) == 0 ? wildcard() : implode(w);
private Type implode((WildcardBound)`super <RefType t>`) = lowerbound(astNode(implode(t), src = t@\loc));
private Type implode((WildcardBound)`extends <RefType t>`) = upperbound(astNode(implode(t), src = t@\loc));

public Type implode((RefType)`<ArrayType at>`) = implode(at);
public Type implode((RefType)`<ClassOrInterfaceType at>`) = implode(at);

public Type implode((ArrayType)`<Type t>[]`) = arrayType(astNode(implode(t), src = t@\loc));

public Type implode((Type)`<RefType r>`) = implode(r);
public Type implode((Type)`<PrimType p>`) = primType["<p>"];

private map[str, Type] primType =
	(
		"boolean" : \boolean()
		, "float" : \float()
		, "double" : \double()
		, "long" : \long()
		, "short" : \short()
		, "char" : \char()
		, "int" : \int()
		, "byte" : \byte()
	);


private list[AstNode] typeArgs("typeArgs"(ata)) = [astNode(implode(a), src = a@\loc) | a<- ata];