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
module lang::rascal::types::TypeSignatures

import List;
import Set;
import ParseTree;
import Reflective;
import IO;

import lang::rascal::types::Types;
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

//
// Each signature item represents an item visible at the module level.
//
data RSignatureItem =
      AliasSigItem(RName aliasName, RType aliasType, loc at)
    | FunctionSigItem(RName functionName, Parameters params, RType returnType, loc at)
    | VariableSigItem(RName variableName, RType variableType, loc at)
    | ADTSigItem(RName adtName, RType adtType, loc at)
    | ConstructorSigItem(RName conName, RType constructorType, loc at)
    | AnnotationSigItem(RName annName, RType annType, RType onType, loc at)
    | TagSigItem(RName tagName, RKind tagKind, list[RType] taggedTypes, loc at)
    ;

//
// A module signature, made up of the module name, individual signature items, and imports.
//
alias RSignature = tuple[set[RSignatureItem] signatureItems, RName moduleName, set[RName] imports];

//
// Get any needed information on the modules imported by the module for which
// a signature is being constructed. NOTE: This currently does nothing.
//
private set[RName] getImportInfo(Import* imports) {
    return { };
}

//
// Add a new signature item into the given signature.
//
private RSignature addSignatureItem(RSignature sig, RSignatureItem item) {
    return sig[signatureItems = sig.signatureItems + item];
}
 
//
// Given a tree, representing a module, create the signature for the module.
//
private RSignature createRSignature(Tree t) {
    if ((Module) `<Header h> <Body b>` := t) {
        switch(h) {
            case (Header)`<Tags t> module <QualifiedName n> <Import* i>` : {
                RSignature sig = <{  }, convertName(n), getImportInfo(i)>;
                sig = createModuleBodySignature(b,sig,b@\loc);
                return sig;
            }

            case (Header)`<Tags t> module <QualifiedName n> <ModuleParameters p> <Import* i>` : {
                RSignature sig = <{  }, convertName(n), getImportInfo(i)>;
                sig = createModuleBodySignature(b,sig,b@\loc);
                return sig;
            }

            default : throw "createRSignature: unexpected module syntax <t>";
        }
    } else {
        throw "createRSignature: unexpected module syntax <t>";
    }
}

//
// Create the individual signature items in the module body.
//
private RSignature createModuleBodySignature(Body b, RSignature sig, loc l) {
    RSignature signatureForSignature(Visibility vis, Signature s, loc sl) {
        if ((Visibility)`public` := vis) { 
            switch(s) {
                case (Signature)`<FunctionModifiers ns> <Type typ> <Name n> <Parameters ps>` : 
                    sig = addSignatureItem(sig, FunctionSigItem(convertName(n), ps, convertType(typ), sl));
                case (Signature)`<FunctionModifiers ns> <Type typ> <Name n> <Parameters ps> throws <{Type ","}+ thrs>` :
                    sig = addSignatureItem(sig, FunctionSigItem(convertName(n), ps, convertType(typ), sl));
                default: throw "signatureForSignature case not implemented for item <s>";
            }
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
                    if ((Visibility)`public` := vis) { 
                        for (v <- vs) {
                            switch(v) {
                                case (Variable)`<Name n>` : sig = addSignatureItem(sig, VariableSigItem(convertName(n), convertType(typ), t@\loc));
                                case (Variable)`<Name n> = <Expression e>` : sig = addSignatureItem(sig, VariableSigItem(convertName(n), convertType(typ), t@\loc));
                            }
                        }
                    }
                }
    
                // Annotation declaration
                case (Toplevel) `<Tags tgs> <Visibility vis> anno <Type typ> <Type otyp> @ <Name n> ;` : {
//                    if ((Visibility)`public` := vis) {
                        sig = addSignatureItem(sig, AnnotationSigItem(convertName(n), convertType(typ), convertType(otyp), t@\loc));
//                    }
                }
                                    
                // Tag declaration
                case (Toplevel) `<Tags tgs> <Visibility vis> tag <Kind k> <Name n> on <{Type ","}+ typs> ;` : {
//                    if ((Visibility)`public` := vis) {
                        sig = addSignatureItem(sig, TagSigItem(convertName(n), convertKind(k), [ convertType(typ) | typ <- typs ], t@\loc));
//                    }
                }
                
                // ADT without variants
                case (Toplevel) `<Tags tgs> <Visibility vis> data <UserType typ> ;` : {
//                    if ((Visibility)`public` := vis) {
                        RType adtBaseType = convertUserType(typ);
                        RType adtType = makeParameterizedADTType(adtBaseType.typeName, adtBaseType.typeParams);
                        sig = addSignatureItem(sig, ADTSigItem(adtBaseType.typeName, adtType, t@\loc));
//                    }
                }
                
                // ADT with variants
                case (Toplevel) `<Tags tgs> <Visibility vis> data <UserType typ> = <{Variant "|"}+ vars> ;` : {
//                    if ((Visibility)`public` := vis) {
                        RType adtBaseType = convertUserType(typ);
                        RType adtType = makeParameterizedADTType(adtBaseType.typeName, adtBaseType.typeParams);
                        sig = addSignatureItem(sig, ADTSigItem(adtBaseType.typeName, adtType, t@\loc));
                        for (var <- vars) {
                            if (`<Name n> ( <{TypeArg ","}* args> )` := var) {
                                sig = addSignatureItem(sig,ConstructorSigItem(convertName(n), makeConstructorType(convertName(n), adtType, [ convertTypeArg(targ) | targ <- args ]), t@\loc));
                            }
                        }
//                    }
                }

                // Alias
                case (Toplevel) `<Tags tgs> <Visibility vis> alias <UserType typ> = <Type btyp> ;` : {
//                    if ((Visibility)`public` := vis) {
                        RType aliasType = convertUserType(typ);
                        RType aliasedType = convertType(btyp);
                        sig = addSignatureItem(sig, AliasSigItem(convertName(getUserTypeRawName(typ)), makeParameterizedAliasType(aliasType.typeName, aliasedType, aliasType.typeParams), t@\loc));
//                    }
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

//
// Return true if the parameter list is a varargs list
//
// TODO: Find a better place for this...
//
public bool isVarArgsParameters(Parameters ps) {
    return `( <Formals _> ...)` := ps;
}

//
// Get the type names introduced in the signature items. ADTs and aliases both introduce
// new type names.
//
private set[RName] getLocallyDefinedTypeNames(RSignature sig) {
    set[RName] definedTypeNames = { };
    for (si <- sig.signatureItems, AliasSigItem(n,_,_) := si || ADTSigItem(n,_,_) := si) definedTypeNames += n;
    return definedTypeNames;
}

//
// Mark types that are not defined locally, which we define here as user types (i.e.,
// types identified by user defined names) that are not defined in a data or alias
// declaration in the same file. These are marked with a new type, RUnknownType,
// indicating that they should be resolved by the name resolution stage (e.g., by
// other imports).
//

data RType = RUnknownType(RType baseType);

public RType makeUnknownType(RType rt) {
    return RUnknownType(rt);
}

public RType getUnknownType(RType rt) {
    if (RAliasType(_,_,at) := rt) return getUnknownType(at);
    if (RTypeVar(_,tvb) := rt) return getUnknownType(tvb);
    if (RUnknownType(t) := rt) return t;
    throw "Warning, was not given RUnknownType";
}

public RType isUnknownType(RType rt) {
    if (RAliasType(_,_,at) := rt) return isUnknownType(at);
    if (RTypeVar(_,tvb) := rt) return isUnknownType(tvb);
    if (RUnknownType(t) := rt) return true;
    return false;
}

private RSignature markUndefinedTypes(RSignature sig) {
    set[RName] localNames = getLocallyDefinedTypeNames(sig);
    sig.signatureItems = 
        visit(sig.signatureItems) {
            case RType t => ((RUserType(n,_) := t) && n notin localNames) ? RUnknownType(t) : t
        }
    return sig;
}

//
// Allow module headers to carry their signatures.
//
anno RSignature Header@sig;

//
// Given a module, annotate it with its signature.
//
public Tree addModuleSignature(Tree t) {
    return top-down-break visit(t) {
        case Header h => h[@sig = markUndefinedTypes(createRSignature(t))]
    }
}

//
// Given a module, return its signature.
//
public RSignature getModuleSignature(Tree t) {
    return markUndefinedTypes(createRSignature(t));
}

//
// Signature maps: maps from the imported module to the module signature
//
public alias SignatureMap = map[Import importedModule, RSignature moduleSignature];

public str getNameOfImportedModule(ImportedModule im) {
    switch(im) {
        case (ImportedModule)`<QualifiedName qn> <ModuleActuals ma> <Renamings rn>` : {
            return prettyPrintName(convertName(qn));
        }
        case (ImportedModule)`<QualifiedName qn> <ModuleActuals ma>` : {
            return prettyPrintName(convertName(qn));
        }
        case (ImportedModule)`<QualifiedName qn> <Renamings rn>` : {
            return prettyPrintName(convertName(qn));
        }
        case (ImportedModule)`<QualifiedName qn>` : {
            return prettyPrintName(convertName(qn));
        }
    }
    throw "getNameOfImportedModule: invalid syntax for ImportedModule <im>, cannot get name";
}

//
// Fill in the signature map with one signature per import.
//
public SignatureMap populateSignatureMap(list[Import] imports) {
    SignatureMap sigMap = ( );
    for (i <- imports) {
        if ((Import)`import <ImportedModule im> ;` := i || (Import)`extend <ImportedModule im> ;` := i) {
            try {
                Tree importTree = getModuleParseTree(getNameOfImportedModule(im));
                sigMap[i] = getModuleSignature(importTree);
            } catch v : {
                println("TypeSignatures: Failed to build signature for module <getNameOfImportedModule(im)>: <v>");
            }
        } 
    }

    return sigMap;
}
