@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::types::TypeSignature

import List;
import Set;
import ParseTree;
import Reflective;
import IO;
import Type;

import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractKind;
import lang::rascal::types::AbstractType;
import lang::rascal::types::ConvertType;

import lang::rascal::syntax::RascalRascal;

//
// TODOs for this module
//
// 1. We need to decide what types of "transitive" information we will
//    add to the signature. Currently imports are not transitive, but
//    this means we can inadvertently merge data types defined in different
//    files that maybe were never intended to be merged. For now, we just
//    ignore the modules imported by this module, marking everything used
//    but not defined here as unknown, and allowing the ultimate importer
//    to figure it out based on its own knowledge.
//
// 2. Add support for transitive aliases. These are aliases defined across
//    more than two modules. For instance, module A defines type T, module B
//    defines alias A = T, module C defines alias B = A, we should still be
//    able to determine that B = T, although this introduces an additional
//    complexity, which is that T may not be in scope.
//
// 3. This code ignores module parameters, since we don't actually support
//    them yet. Once they are supported, they will need to be accounted for
//    in this code as well.
//
// 4. We currently ignore visibility on aliases, ADTs, etc, since this is
//    the behavior of the interpreter. If this changes, we should enable
//    visibility filtering here as well.
//
// 5. We do not currently verify here that abstract functions are java
//    functions. If we want to add that check, we should put it in below
//    in the code that processes the signature item. However, this goes
//    against the working assumption in this code, which is that this module
//    is correct.
// 
// 6. What impact should tags have on the signature (if any?)
//

@doc{Each signature item represents an item visible at the module level.}
data RSignatureItem 
	= AliasSigItem(RName aliasName, UserType aliasType, Type aliasedType, loc at)
	| FunctionSigItem(RName functionName, Signature sig, loc at)
	| VariableSigItem(RName variableName, Type variableType, loc at)
	| ADTSigItem(RName adtName, UserType adtType, loc at)
	| ConstructorSigItem(RName conName, UserType adtType, list[TypeArg] argTypes, loc adtAt, loc at)
	| AnnotationSigItem(RName annName, Type annType, Type onType, loc at)
	| TagSigItem(RName tagName, TagKind tagKind, list[Symbol] taggedTypes, loc at)
	;

@doc{A module signature, made up of the module name, individual signature items, and imports.}
data RSignature = rsignature(
	list[RSignatureItem] datatypes, 
	list[RSignatureItem] aliases,
	list[RSignatureItem] tags,
	list[RSignatureItem] annotations, 
	list[RSignatureItem] publicVariables, 
	list[RSignatureItem] publicFunctions, 
	list[RSignatureItem] publicConstructors, 
	list[RSignatureItem] privateVariables, 
	list[RSignatureItem] privateFunctions, 
	RName moduleName, 
	set[RName] imports);

@doc{Create an empty signature for the given module}
private RSignature emptySignature(RName forName) = rsignature([],[],[],[],[],[],[],[],[],forName,{}); 

@doc{Given a tree, representing a module, create the signature for the module.}
private RSignature createRSignature(Tree t) {
	if ((Module) `<Header h> <Body b>` := t) {
		switch(h) {
			case (Header)`<Tags t> module <QualifiedName n> <Import* i>` : {
				RSignature sig = emptySignature(convertName(n));
				sig = createModuleBodySignature(b,sig,b@\loc);
				return sig;
			}

			case (Header)`<Tags t> module <QualifiedName n> <ModuleParameters p> <Import* i>` : {
				RSignature sig = emptySignature(convertName(n));
				sig = createModuleBodySignature(b,sig,b@\loc);
				return sig;
			}

			default : throw "createRSignature: unexpected module syntax <t>";
		}
	} else {
		throw "createRSignature: unexpected module syntax <t>";
	}
}

@doc{Create the individual signature items in the module body.}
private RSignature createModuleBodySignature(Body b, RSignature sig, loc l) {
	RSignature signatureForSignature(Visibility vis, Signature s, loc sl) {
		switch(s) {
			case (Signature)`<FunctionModifiers ns> <Type typ> <Name n> <Parameters ps>` :
				if ((Visibility)`public` := vis) 
					sig.publicFunctions = sig.publicFunctions + FunctionSigItem(convertName(n), s, sl);
				else
					sig.privateFunctions = sig.privateFunctions + FunctionSigItem(convertName(n), s, sl);
			case (Signature)`<FunctionModifiers ns> <Type typ> <Name n> <Parameters ps> throws <{Type ","}+ thrs>` :
				if ((Visibility)`public` := vis) 
					sig.publicFunctions = sig.publicFunctions + FunctionSigItem(convertName(n), s, sl);
				else
					sig.privateFunctions = sig.privateFunctions + FunctionSigItem(convertName(n), s, sl);
			default: throw "signatureForSignature case not implemented for item <s>";
		}
		return sig;    
	}
 
	RSignature signatureForFunction(FunctionDeclaration fd) {
		switch(fd) {
			// Abstract (i.e., without a body) function declaration
			case (FunctionDeclaration) `<Tags tgs> <Visibility vis> <Signature s> ;` : 
				return signatureForSignature(vis,s,fd@\loc);

			// Concrete (i.e., with a body) function declaration
			case (FunctionDeclaration) `<Tags tgs> <Visibility vis> <Signature s> <FunctionBody fb>` :
				return signatureForSignature(vis,s,fd@\loc);

			// Concrete (i.e., with a body) function declaration, expression form
			case (FunctionDeclaration) `<Tags tgs> <Visibility vis> <Signature s> = <Expression exp>;` :
				return signatureForSignature(vis,s,fd@\loc);

			// Concrete (i.e., with a body) function declaration, expression form, with condition
			case (FunctionDeclaration) `<Tags tgs> <Visibility vis> <Signature s> = <Expression exp> when <{Expression ","}+ conds>;` :
				return signatureForSignature(vis,s,fd@\loc);

			default: throw "signatureForFunction case not implemented for item <fd>";
		}
	}

	if ((Body)`<Toplevel* ts>` := b) {
		for (Toplevel t <- ts) {
			switch(t) {
				// Variable declaration
				case (Toplevel) `<Tags tgs> <Visibility vis> <Type typ> <{Variable ","}+ vs> ;` : {
					for (v <- vs) {
						switch(v) {
							case (Variable)`<Name n>` :
								if ((Visibility)`public` := vis) 
									sig.publicVariables = sig.publicVariables + VariableSigItem(convertName(n), typ, t@\loc);
								else
									sig.privateVariables = sig.privateVariables + VariableSigItem(convertName(n), typ, t@\loc);
							case (Variable)`<Name n> = <Expression e>` :
								if ((Visibility)`public` := vis) 
									sig.publicVariables = sig.publicVariables + VariableSigItem(convertName(n), typ, t@\loc);
								else
									sig.privateVariables = sig.privateVariables + VariableSigItem(convertName(n), typ, t@\loc);
						}
					}
				}

				// Annotation declaration
				case (Toplevel) `<Tags tgs> <Visibility vis> anno <Type typ> <Type otyp> @ <Name n> ;` : {
					sig.annotations = sig.annotations + AnnotationSigItem(convertName(n), typ, otyp, t@\loc);
				}

				// Tag declaration
				case (Toplevel) `<Tags tgs> <Visibility vis> tag <Kind k> <Name n> on <{Type ","}+ typs> ;` : {
					sig.tags = sig.tags + TagSigItem(convertName(n), convertKind(k), [ convertType(typ) | typ <- typs ], t@\loc);
				}

				// ADT without variants
				case (Toplevel) `<Tags tgs> <Visibility vis> data <UserType typ> ;` : {
					sig.datatypes = sig.datatypes + ADTSigItem(convertName(getUserTypeRawName(typ)), typ, t@\loc);
				}

				// ADT with variants
				case (Toplevel) `<Tags tgs> <Visibility vis> data <UserType typ> = <{Variant "|"}+ vars> ;` : {
					sig.datatypes = sig.datatypes + ADTSigItem(convertName(getUserTypeRawName(typ)), typ, t@\loc);
					for (var <- vars) {
						if (`<Name n> ( <{TypeArg ","}* args> )` := var) {
							sig.publicConstructors = sig.publicConstructors + ConstructorSigItem(convertName(n), typ, [ targ | targ <- args ], t@\loc, var@\loc);
						}
					}
				}

				// Alias
				case (Toplevel) `<Tags tgs> <Visibility vis> alias <UserType typ> = <Type btyp> ;` : {
					Symbol aliasType = convertUserType(typ);
					Symbol aliasedType = convertType(btyp);
					sig.aliases = sig.aliases + AliasSigItem(convertName(getUserTypeRawName(typ)), typ, btyp, t@\loc);
				}

				// Function declaration
				case (Toplevel) `<FunctionDeclaration fd>` :
					sig = signatureForFunction(fd);

				default: throw "RSignature case not implemented for item <t>";
			}
		}
	}

	return sig;
}

@doc{Return true if the parameter list is a varargs list}
public bool isVarArgsParameters(Parameters ps) {
	return `( <Formals _> ...)` := ps;
}

//
// Given a module, return its signature.
//
public RSignature getModuleSignature(Tree t) {
	return createRSignature(t);
}

public RName getNameOfImportedModule((ImportedModule)`<QualifiedName qn> <ModuleActuals ma> <Renamings rn>`) = convertName(qn);
public RName getNameOfImportedModule((ImportedModule)`<QualifiedName qn> <ModuleActuals ma>`) = convertName(qn);
public RName getNameOfImportedModule((ImportedModule)`<QualifiedName qn> <Renamings rn>`) = convertName(qn);
public RName getNameOfImportedModule((ImportedModule)`<QualifiedName qn>`) = convertName(qn);
