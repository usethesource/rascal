@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::types::TypeSignature

import Set;
import util::Reflective;
import IO;

import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractKind;
import lang::rascal::types::AbstractType;
import lang::rascal::types::ConvertType;

import lang::rascal::\syntax::Rascal;
import lang::rascal::grammar::definition::Productions;

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
	| ConstructorSigItem(RName conName, UserType adtType, list[TypeArg] argTypes, list[KeywordFormal] commonParams, list[KeywordFormal] keywordParams, loc adtAt, loc at)
	| ProductionSigItem(Production prod, loc sortAt, loc at)
	| AnnotationSigItem(RName annName, Type annType, Type onType, loc at)
	| TagSigItem(RName tagName, TagKind tagKind, list[Symbol] taggedTypes, loc at)
	| LexicalSigItem(RName sortName, Symbol sort, loc at)
	| ContextfreeSigItem(RName sortName, Symbol sort, loc at)
	| KeywordSigItem(RName sortName, Symbol sort, loc at)
	| LayoutSigItem(RName sortName, Symbol sort, loc at)
	;

@doc{A module signature, made up of the module name, individual signature items, and imports.}
data RSignature = rsignature(
	list[RSignatureItem] datatypes, 
	list[RSignatureItem] lexicalNonterminals,
	list[RSignatureItem] contextfreeNonterminals,
	list[RSignatureItem] keywordNonterminals,
	list[RSignatureItem] layoutNonterminals,
	list[RSignatureItem] aliases,
	list[RSignatureItem] tags,
	list[RSignatureItem] annotations, 
	list[RSignatureItem] publicVariables, 
	list[RSignatureItem] publicFunctions, 
	list[RSignatureItem] publicConstructors, 
	list[RSignatureItem] publicProductions,
	list[RSignatureItem] privateVariables, 
	list[RSignatureItem] privateFunctions, 
	RName moduleName, 
	set[RName] imports);

@doc{Create an empty signature for the given module}
private RSignature emptySignature(RName forName) = rsignature([],[],[],[],[],[],[],[],[],[],[],[],[],[],forName,{}); 

@doc{Add the items from one signature into another}
private RSignature mergeSignatures(RSignature target, RSignature source, bool extending) {
	return target[datatypes = target.datatypes + source.datatypes]
	             [aliases = target.aliases + source.aliases]
	             [tags = target.tags + source.tags]
	             [annotations = target.annotations + source.annotations]
	             [publicVariables = target.publicVariables + source.publicVariables]
	             [publicFunctions = target.publicFunctions + source.publicFunctions]
	             [publicConstructors = target.publicConstructors + source.publicConstructors]
	             [privateVariables = target.privateVariables + source.privateVariables]
	             [privateFunctions = target.privateFunctions + source.privateFunctions]
	             [lexicalNonterminals = target.lexicalNonterminals + source.lexicalNonterminals]
	             [contextfreeNonterminals = target.contextfreeNonterminals + source.contextfreeNonterminals]
	             [keywordNonterminals = target.keywordNonterminals + source.keywordNonterminals]
	             [layoutNonterminals = target.layoutNonterminals + source.layoutNonterminals]
	             [publicProductions = target.publicProductions + source.publicProductions]
	             [imports = target.imports + (extending ? source.imports : { })]
	             ;
}

@doc{Given a tree, representing a module, create the signature for the module.}
private RSignature createRSignature(Tree t, set[RName] visitedAlready) {
	RSignature processModule(RName mn, RSignature sig, Import* i, Body b) {
		visitedAlready = visitedAlready + mn;
		sig = addImports(i,sig,visitedAlready);
		sig = createModuleBodySignature(b,sig,b@\loc);
		return sig;
	}
	if (t has top && Module m := t.top) t = m;
	if ((Module) `<Header h> <Body b>` := t) {
		switch(h) {
			case (Header)`<Tags tgs> module <QualifiedName n> <Import* i>` : {
				sig = emptySignature(convertName(n));
				return processModule(convertName(n),processSyntax(convertName(n),sig,[imp | Import imp <- i]),i,b);
			}

			case (Header)`<Tags tgs> module <QualifiedName n> <ModuleParameters p> <Import* i>` : {
				sig = emptySignature(convertName(n));
				return processModule(convertName(n),processSyntax(convertName(n),sig,[imp | Import imp <- i]),i,b);
			}

			default : throw "createRSignature: unexpected module syntax <t>";
		}
	} else {
		throw "createRSignature: unexpected module syntax <t>";
	}
}

public RSignature processSyntax(RName name, list[Import] defs) {
	sig = emptySignature(name);
	return processSyntax(name, sig, defs);
}

public RSignature processSyntax(RName name, RSignature sig, list[Import] defs) {
  prods = [];
  
  for ((Import) `<SyntaxDefinition sd>` <- defs) {
    rule = rule2prod(sd);
    
    // Add productions as in grammar definitions to the signature
    for(pi <- rule.prods) {
    	prods += ProductionSigItem(pi, sd.defined@\loc, sd@\loc);
    }
    
    for (/pr:prod(_,_,_) <- rule.prods) {
      // prods += ProductionSigItem(pr, sd.defined@\loc, sd@\loc);
      
      sym = pr.def is label ? pr.def.symbol : pr.def;
      
      switch (sym) {
        case sort(str sortName) : 
          sig.contextfreeNonterminals += [ContextfreeSigItem(RSimpleName(sortName), sym, sd.defined@\loc)];
        case lex(str lexName) : 
          sig.lexicalNonterminals += [LexicalSigItem(RSimpleName(lexName), sym, sd.defined@\loc)];
        case layouts(str layoutsName) : 
          sig.layoutNonterminals += [LayoutSigItem(RSimpleName(layoutsName), sym, sd.defined@\loc)];
        case keywords(str keywordsName) : 
          sig.keywordNonterminals += [KeywordSigItem(RSimpleName(keywordsName), sym, sd.defined@\loc)];  
        case \parameterized-sort(str sortName, list[Symbol] parameters) : 
          sig.contextfreeNonterminals += [ContextfreeSigItem(RSimpleName(sortName), sym, sd.defined@\loc)];
        case \parameterized-lex(str lexName, list[Symbol] parameters) : 
          sig.lexicalNonterminals += [LexicalSigItem(RSimpleName(lexName), sym, sd.defined@\loc)];
        case \start(_) : 
          ; // TODO: add support for start non-terminals
        default: 
          throw "unexpected non-terminal definition <pr.def>"; 
      }
    } 
  }
  
    
  sig.publicProductions = prods;
  
  return sig;   
}

@doc{Add any imports that are needed, specifically imports for extended modules.}
private RSignature addImports(Import* imports, RSignature sig, set[RName] visitedAlready) {
	sig.imports = { getNameOfImportedModule(im) | (Import)`import <ImportedModule im> ;` <- imports };
	for ((Import)`extend <ImportedModule im> ;` <- imports) {
		mn = getNameOfImportedModule(im);
		if (mn notin visitedAlready) {
			visitedAlready = visitedAlready + mn;
			sig = mergeSignatures(sig, getModuleSignature(parseNamedModuleWithSpaces(prettyPrintName(mn)), visitedAlready), true);
		}
	}
	return sig;
}

@doc{Create the individual signature items in the module body.}
private RSignature createModuleBodySignature(Body b, RSignature sig, loc l) {
	RSignature signatureForSignature(Visibility vis, Signature s, loc sl) {
		switch(s) {
			case (Signature)`<FunctionModifiers ns> <Type typ> <Name n> <Parameters ps>` :
				if ((Visibility)`private` := vis) 
					sig.privateFunctions = sig.privateFunctions + FunctionSigItem(convertName(n), s, sl);
				else
					sig.publicFunctions = sig.publicFunctions + FunctionSigItem(convertName(n), s, sl);
			case (Signature)`<FunctionModifiers ns> <Type typ> <Name n> <Parameters ps> throws <{Type ","}+ thrs>` :
				if ((Visibility)`private` := vis) 
					sig.privateFunctions = sig.privateFunctions + FunctionSigItem(convertName(n), s, sl);
				else
					sig.publicFunctions = sig.publicFunctions + FunctionSigItem(convertName(n), s, sl);
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
				case (Toplevel) `<Tags tgs> <Visibility vis> anno <Type typ> <Type otyp>@<Name n> ;` : {
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

				// ADT without variants but with kw params without usage of then
				case (Toplevel) `<Tags tgs> <Visibility vis> data <UserType typ> <CommonKeywordParameters _>;` : {
					sig.datatypes = sig.datatypes + ADTSigItem(convertName(getUserTypeRawName(typ)), typ, t@\loc);
				}

				// ADT with variants
				case (Toplevel) `<Tags tgs> <Visibility vis> data <UserType typ> <CommonKeywordParameters params> = <{Variant "|"}+ vars> ;` : {
					commonParamList = [ ];
					if ((CommonKeywordParameters)`( <{KeywordFormal ","}+ kfs> )` := params) commonParamList = [ kfi | kfi <- kfs ];
					sig.datatypes = sig.datatypes + ADTSigItem(convertName(getUserTypeRawName(typ)), typ, t@\loc);
					for (var <- vars) {
						if ((Variant) `<Name n> ( <{TypeArg ","}* args> <KeywordFormals kfs>)` := var) {
							paramlist = [ ];
							if ((KeywordFormals)`<OptionalComma _> <{KeywordFormal ","}+ kfsi>` := kfs) paramlist = [ kfi | kfi <- kfsi];
							sig.publicConstructors = sig.publicConstructors + ConstructorSigItem(convertName(n), typ, [ targ | targ <- args ], commonParamList, paramlist, t@\loc, var@\loc);
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
	return (Parameters)`( <Formals fs> ...)` := ps;
}

@doc{Given a module, return its signature.}
public RSignature getModuleSignature(Tree t) {
	cacheLoc = |tmp:///org.rascal-mpl/signatureCache|;
	if (!exists(cacheLoc.parent)) mkDirectory(cacheLoc.parent);
	return getModuleSignature(t, {});
}

@doc{Given a module, return its signature. If we have to descend to check imports, the set indicates which have already been visited.}
public RSignature getModuleSignature(Tree t, set[RName] visitedAlready) {
	return createRSignature(t, visitedAlready);
}

@doc{Get the name of the import from within the module name definition.}
public RName getNameOfImportedModule((ImportedModule)`<QualifiedName qn> <ModuleActuals ma> <Renamings rn>`) = convertName(qn);
public RName getNameOfImportedModule((ImportedModule)`<QualifiedName qn> <ModuleActuals ma>`) = convertName(qn);
public RName getNameOfImportedModule((ImportedModule)`<QualifiedName qn> <Renamings rn>`) = convertName(qn);
public RName getNameOfImportedModule((ImportedModule)`<QualifiedName qn>`) = convertName(qn);
