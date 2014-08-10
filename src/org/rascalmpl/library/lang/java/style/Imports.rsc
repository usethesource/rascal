module lang::java::style::Imports

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;
import List;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

import lang::java::style::Utils;

import IO;

data Message = imports(str category, loc pos);

/*
AvoidStarImports	DONE
AvoidStaticImport	DONE
IllegalImport		DONE
RedundantImport		DONE
UnusedImports		TBD
ImportOrder			TBD
ImportControl		TBD
*/


list[Message] avoidStarImport(Declaration imp: \import(_),  list[Declaration] parents, node ast, M3 model) =
	\onDemand() in (imp@modifiers?{}) ? [imports("AvoidStarImport", imp@src)] : [];

default list[Message] avoidStarImport(Declaration imp,  list[Declaration] parents, node ast, M3 model) = [];

//list[Message] avoidStarImport(node ast, M3 model, OuterDeclarations decls) =
//	[ imports("AvoidStarImport", imp@src) | /Declaration imp: \import(_) := ast, \onDemand() in (imp@modifiers?{})];
//	

list[Message] avoidStaticImport(Declaration imp: \import(_),  list[Declaration] parents, node ast, M3 model) =
	\static() in (imp@modifiers?{}) ? [ imports("AvoidStaticImport", imp@src)] : [];
	
default list[Message] avoidStaticImport(Declaration imp,  list[Declaration] parents, node ast, M3 model) = [];	
	
//list[Message] avoidStaticImport(node ast, M3 model, OuterDeclarations decls) =
//	[ imports("AvoidStaticImport", imp@src) | /Declaration imp: \import(_) := ast, \static() in (imp@modifiers?{})];
//	

list[Message] illegalImport(Declaration imp: \import(str name),  list[Declaration] parents, node ast, M3 model) =
	startsWith(name, "sun") ? [imports("IllegalImport", imp@src)] : [];

list[Message] illegalImport(Declaration imp,  list[Declaration] parents, node ast, M3 model) = [];
			

//list[Message] illegalImport(node ast, M3 model, OuterDeclarations decls) =
//	[ imports("IllegalImport", imp@src) | /Declaration imp: \import(name) := ast, startsWith(name, "sun")];
//	

// TODO:

//list[Message] redundantImport(node ast, M3 model, OuterDeclarations decls) {
//	msgs = [];
//	
//	packageName = getPackageName(ast);
//   
//	imported = [ imp | /Declaration imp: \import(str name) := ast];
//	
//	for(imp: \import(str name) <- imported){
//		if(indexOf(imported, name) != lastIndexOf(imported, name)){
//			msgs += imports("UnusedImports", imp@src);
//		} else
//		if(startsWith(name, "java.lang")){
//			msgs += imports("UnusedImports", imp@src);
//		} else
//		if(startsWith(name, packageName)){
//			msgs += imports("UnusedImports", imp@src);
//		}
//	}
//	
//	return msgs;
//}