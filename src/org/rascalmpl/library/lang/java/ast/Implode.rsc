module lang::java::ast::Implode

import lang::java::\syntax::Java15;

import experiments::m3::AST;
import experiments::m3::Core;

data Decleration = annotationType(list[Modifier] modifiers, str name, list[AstNode] body);

public AstNode implode(cu:(start[CompilationUnit])`<PackageDec d> <ImportDec* imports> <TypeDec* types>`)
	= astNode(compilationUnit(implode(imports), implode(types)), src = cu@\loc);

public default AstNode implode(CompilationUnit cu) { throw "Not supported <cu>"; }
	
// imports
private list[AstNode] implode(list[ImportDec] imports) = [astNode(implode(imp), src = cu@\loc) | imp <- imports];

private Declaration implode((ImportDec)`import <TypeName t>`) = \import("<t>");
private Declaration implode((ImportDec)`import <TypeName t>.*`) = \import("<t>.*");
private Declaration implode((ImportDec)`import static <TypeName t>.*`) = staticImport("<t>.*");
private Declaration implode((ImportDec)`import static <TypeName t>.<Id n>`) = staticImport("<t>.<n>");
private default Declaration implode(ImportDec id) { throw "Not supported <id>"; }

// type decls
private list[AstNode] implode(list[TypeDec] types) = [astNode(implode(t), src = t@\loc) | t <- types, (TypeDec)`;` !:= t];

private Declaration implode((TypeDec)`<InterfaceDec id>`) = implode(id);
private Declaration implode((TypeDec)`<ClassDec cd>`) = implode(cd);
private default Declaration implode(TypeDec td) { throw "Not supported <td>"; }

// interface decls
private Declartion implode((InterfaceDec)`<AnnoDecHead adh> { <AnnoElemDec* aed> }`) = annotationType(modifiers(adh), name(adh), decls(aed));
private Declartion implode((InterfaceDec)`<InterfaceDecHead idh> { <InterfaceMemberDec* imd> }`) = interface(modifiers(idh), name(idh), extends(idh), [], decls(imd));

private str name(annoDecHead(_,i)) = "<i>";
private str name(interfaceDecHead(_,i,_,_)) = "<i>";

private list[AstNode] extends(interfaceDecHead(_,_,_,e)) = extends(e);
private list[AstNode] extends(extendsInterfaces(es)) = [astNode(implode(e), src = e@\loc) | e <- es];


private list[AstNode] decls(list[AnnoElemDec] aed) = [];
private list[AstNode] decls(list[InterfaceMemberDec] imd) = [];

// modifiers
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
	
private list[Modifier] modifiers(annoDecHead(ms,_)) = [ modifier["<m>"] | InterfaceMod m <- ms];
private list[Modifier] modifiers(interfaceDecHead(ms,_,_,_)) = [ modifier["<m>"] | InterfaceMod m <- ms];

// types
private Type implode(interfaceType(s, a)) = size(a) == 0 ? simpleType("<s>") : parameterizedType(simpleType("<s>"), typeArgs(a));
private Type implode(classOrInterfaceType(s, a)) = size(a) == 0 ? simpleType("<s>") : parameterizedType(simpleType("<s>"), typeArgs(a));

private Type implode(wildCard(w)) = size(w) == 0 ? wildcard() : implode(w);
private Type implode((WildcardBound)`super <RefType t>`) = lowerbound(astNode(implode(t), src = t@\loc));
private Type implode((WildcardBound)`extends <RefType t>`) = upperbound(astNode(implode(t), src = t@\loc));

private Type implode((RefType)`<ArrayType at>`) = implode(at);
private Type implode((RefType)`<ClassOrInterfaceType at>`) = implode(at);

private Type implode((ArrayType)`<Type t>[]`) = arrayType(astNode(implode(t), src = t@\loc));

private Type implode((Type)`<RefType r>`) = implode(r);
private Type implode((Type)`<PrimType p>`) = primType["<p>"];

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


private list[AstNode] typeArgs(typeArgs(ata)) = [astNode(implode(a), src = a@\loc) | a<- ata];
