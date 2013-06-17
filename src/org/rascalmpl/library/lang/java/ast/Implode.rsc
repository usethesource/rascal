module lang::java::ast::Implode

import lang::java::\syntax::Java15;

import experiments::m3::AST;
import experiments::m3::Core;

import lang::java::ast::implode::Modifiers;
import lang::java::ast::implode::Types;

data Decleration = annotationType(list[Modifier] modifiers, str name, list[AstNode] body);

public AstNode implode(cu:(start[CompilationUnit])`<PackageDec d> <ImportDec* imports> <TypeDec* types>`)
	= astNode(compilationUnit(implode(imports), implode(types)), src = cu@\loc);
	
public AstNode implode(cu:(start[CompilationUnit])`<ImportDec* imports> <TypeDec* types>`)
	= astNode(compilationUnit(implode(imports), implode(types)), src = cu@\loc);

public default AstNode implode(start[CompilationUnit] cu) { throw "Not supported <cu>"; }
	
// imports
private list[AstNode] implode(list[ImportDec] imports) = [astNode(implode(imp), src = cu@\loc) | imp <- imports];

private Declaration implode((ImportDec)`import <TypeName t>;`) = \import("<t>");
private Declaration implode((ImportDec)`import <PackageName p>.*;`) = \import("<p>.*");
private Declaration implode((ImportDec)`import static <TypeName t>.*;`) = staticImport("<t>.*");
private Declaration implode((ImportDec)`import static <TypeName t>.<Id n>;`) = staticImport("<t>.<n>");
private default Declaration implode(ImportDec id) { throw "Not supported <id>"; }

// type decls
private list[AstNode] implode(list[TypeDec] types) = [astNode(implode(t), src = t@\loc) | t <- types, (TypeDec)`;` !:= t];

private Declaration implode((TypeDec)`<InterfaceDec id>`) = implode(id);
private Declaration implode((TypeDec)`<ClassDec cd>`) = implode(cd);
private default Declaration implode(TypeDec td) { throw "Not supported <td>"; }

// interface decls
private Declaration implode((InterfaceDec)`<AnnoDecHead adh> { <AnnoElemDec* aed> }`) = annotationType(modifiers(adh), name(adh), decls(aed));
private Declaration implode((InterfaceDec)`<InterfaceDecHead idh> { <InterfaceMemberDec* imd> }`) = interface(modifiers(idh), name(idh), extends(idh), [], decls(imd));

private str name("annoDecHead"(_,i)) = "<i>";
private str name("interfaceDecHead"(_,i,_,_)) = "<i>";

private list[AstNode] extends("interfaceDecHead"(_,_,_,e)) = extends(e);
private list[AstNode] extends("extendsInterfaces"(es)) = [astNode(implode(e), src = e@\loc) | e <- es];

private list[AstNode] decls(list[AnnoElemDec] aed) = [];
private list[AstNode] decls(list[InterfaceMemberDec] imd) = [];

