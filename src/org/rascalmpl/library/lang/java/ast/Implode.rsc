module lang::java::ast::Implode

import lang::java::\syntax::Java15;

import experiments::m3::AST;
import experiments::m3::Core;

public AstNode implode(cu:(start[CompilationUnit])`<PackageDec d> <ImportDec* imports> <TypeDec* types>`)
	= astNode(compilationUnit(implode(imports), implode(types)), src = cu@\loc);

public default AstNode implode(CompilationUnit cu) { throw "Not supported <cu>"; }
	
private list[AstNode] implode(list[ImportDec] imports) = [astNode(implode(imp), src = cu@\loc) | imp <- imports];

private Declaration implode((ImportDec)`import <TypeName t>`) = \import("<t>");
private Declaration implode((ImportDec)`import <TypeName t>.*`) = \import("<t>.*");
private Declaration implode((ImportDec)`import static <TypeName t>.*`) = staticImport("<t>.*");
private Declaration implode((ImportDec)`import static <TypeName t>.<Id n>`) = staticImport("<t>.<n>");
private Declaration implode(ImportDec id) { throw "Not supported <id>"; }


