module lang::java::ast::implode::Modifiers

import lang::java::\syntax::Java15;

import experiments::m3::AST;
import experiments::m3::Core;

private map[str, Modifier] modifier =
	(
		"private": \private()
		, "public":  \public()
		, "protected" : \protected()
		, "static" : \static()
		, "final" : \final()
		, "synchronized" : \synchronized()
		, "transient" : \transient()
		, "abstract" : \abstract()
		, "native" : \native()
		, "volatile" : \volatile()
		, "strictfp" : \strictfp()
	);
	
public list[Modifier] modifiers("annoDecHead"(ms,_)) = [ modifier["<m>"] | InterfaceMod m <- ms];
public list[Modifier] modifiers("interfaceDecHead"(ms,_,_,_)) = [ modifier["<m>"] | InterfaceMod m <- ms];