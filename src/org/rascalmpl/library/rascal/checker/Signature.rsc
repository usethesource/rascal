module rascal::checker::Signature

import rascal::checker::Types;
import rascal::\old-syntax::Rascal;
import List;
import Set;

// Each signature item represents an item visible at the module level.
data RSignatureItem =
	  AliasSigItem(RType aliasType, RType sigType, loc at)
	| FunctionSigItem(RName functionName, RType sigType, loc at)
	| VariableSigItem(RName variableName, RType sigType, loc at)
	| ADTSigItem(RType adtType, RType sigType, loc at)
	| ConstructorSigItem(RName conName, RType sigType, loc at)
	| RuleSigItem(RName ruleName, loc at)
    | AnnotationSigItem(RName annName, RType sigType, RType onType, loc at)
	| TagSigItem(RName tagName, list[RType] tagTypes, loc at)
;

// A module signature, made up of the module name, individual signature items, and imports.
alias RSignature = tuple[set[RSignatureItem] signatureItems, RName moduleName, set[RName] imports];

// TODO: This code needs to be changed to get information from the imports. This is to
// construct the visible signature of a module, which may include information imported
// from other modules indirectly (for instance, aliases)
private set[RName] getImportInfo(Import* imports) {
	return { };
}

// Add a new signature item into the given siguature.
private RSignature addSignatureItem(RSignature sig, RSignatureItem item) {
	return sig[signatureItems = sig.signatureItems + item];
}
 
// Given a tree, representing a module, create the signature for the module.
// TODO: Right now we don't use parameters. Add support for them here when
// we use them in actual programs. Commenting out the part that deals with
// them for now, so we will get an exception when we finally need to deal
// with them.
private RSignature createRSignature(Tree t) {
	if ((Module) `<Header h> <Body b>` := t) {
		switch(h) {
			case `<Tags t> module <QualifiedName n> <Import* i>` : {
				RSignature sig = <{  }, convertName(n), getImportInfo(i)>;
				sig = createModuleBodySignature(b,sig);
				return sig;
			}

//			case `<Tags t> module <QualifiedName n> <ModuleParameters p> <Import* i>` : {
//				RSignature sig = <{  }, convertName(n), getImportInfo(i)>;
//				sig = createModuleBodySignature(b,sig);
//				return sig;
//			}

			default : throw "createRSignature: unexpected module syntax <t>";
		}
	} else {
        throw "createRSignature: unexpected module syntax <t>";
	}
}

// Create the individual signature items in the module body: dispatches out to
// individual functions to create the various types of signature items.
private RSignature createModuleBodySignature(Body b, RSignature sig) {
	if ((Body)`<Toplevel* ts>` := b) {
		for (Toplevel t <- ts) {
			switch(t) {
				// Variable declaration
				case (Toplevel) `<Tags tgs> <Visibility vis> <Type typ> <{Variable ","}+ vs> ;` : {
					if (`public` := vis) { 
						for (v <- vs) {
							switch(v) {
								case (Variable)`<Name n>` : sig = addSignatureItem(sig, VariableSigItem(convertName(n), convertType(typ), t@\loc));
								case (Variable)`<Name n> = <Expression e>` : sig = addSignatureItem(sig, VariableSigItem(convertName(n), convertType(typ), t@\loc));
							}
						}
					}
				}
	
				// Abstract (i.e., without a body) function declaration
				case (Toplevel) `<Tags tgs> <Visibility vis> <Signature s> ;` : {
					if (`public` := vis) { 
						switch(s) {
							case `<Type typ> <FunctionModifiers ns> <Name n> <Parameters ps>` : 
								sig = addSignatureItem(sig, FunctionSigItem(convertName(n), makeFunctionType(convertType(typ),getParameterTypes(ps)), t@\loc));
							case `<Type typ> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ thrs> ` :
								sig = addSignatureItem(sig, FunctionSigItem(convertName(n), makeFunctionType(convertType(typ),getParameterTypes(ps)), t@\loc));
						}
					}
				}
	 
	 			// Concrete (i.e., with a body) function declaration
				case (Toplevel) `<Tags tgs> <Visibility vis> <Signature s> <FunctionBody fb>` : {
					if (`public` := vis) {
						switch(s) {
							case `<Type typ> <FunctionModifiers ns> <Name n> <Parameters ps>` : 
								sig = addSignatureItem(sig, FunctionSigItem(convertName(n), makeFunctionType(convertType(typ),getParameterTypes(ps)), t@\loc));
							case `<Type typ> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ thrs> ` :
								sig = addSignatureItem(sig, FunctionSigItem(convertName(n), makeFunctionType(convertType(typ),getParameterTypes(ps)), t@\loc));
						}
					}
				}
				
				// Annotation declaration
				// TODO: Currently visibility is ignored. Should update this from any visibility to just public when this changes.
				case (Toplevel) `<Tags tgs> <Visibility vis> anno <Type typ> <Type otyp> @ <Name n> ;` : {
					sig = addSignatureItem(sig, AnnotationSigItem(convertName(n), convertType(typ), convertType(otyp), t@\loc));
				}
									
				// Tag declaration
				// TODO: Currently visibility is ignored. Should update this from any visibility to just public when this changes.
				// TODO: Add in kind
				case (Toplevel) `<Tags tgs> <Visibility vis> tag <Kind k> <Name n> on <{Type ","}+ typs> ;` : {
					sig = addSignatureItem(sig, TagSigItem(convertName(n), [ convertType(typ) | typ <- typs ], t@\loc));
				}
				
				// Rule declaration -- we will include the rule names in the signature, but I'm not sure we actually need them
				case (Toplevel) `<Tags tgs> rule <Name n> <PatternWithAction pwa> ;` : {
					sig = addSignatureItem(sig, RuleSigItem(convertName(n), t@\loc));
				}
				
				// Test -- tests are not part of the signature, but we include them here so we don't throw below on unhandled cases
				case (Toplevel) `<Test tst> ;` : {
					sig = sig;
				}
								
				// ADT without variants
				// TODO: Currently visibility is ignored. Should update this from any visibility to just public when this changes.
				case (Toplevel) `<Tags tgs> <Visibility vis> data <UserType typ> ;` : {
					sig = addSignatureItem(sig, ADTSigItem(convertUserType(typ), RADTType(convertUserType(typ),[]), t@\loc));
				}
				
				// ADT with variants
				// TODO: Currently visibility is ignored. Should update this from any visibility to just public when this changes.
				case (Toplevel) `<Tags tgs> <Visibility vis> data <UserType typ> = <{Variant "|"}+ vars> ;` : {
					sig = addSignatureItem(sig, ADTSigItem(convertUserType(typ), RADTType(convertUserType(typ),[]), t@\loc));
					for (var <- vars) {
						if (`<Name n> ( <{TypeArg ","}* args> )` := var) {
							sig = addSignatureItem(sig,ConstructorSigItem(convertName(n), RConstructorType(convertName(n), [ convertTypeArg(targ) | targ <- args ], RADTType(convertUserType(typ),[])), t@\loc));
						}
					}
				}

				// Alias
				// TODO: Currently visibility is ignored. Should update this from any visibility to just public when this changes.
				case (Toplevel) `<Tags tgs> <Visibility vis> alias <UserType typ> = <Type btyp> ;` : {
					sig = addSignatureItem(sig, AliasSigItem(convertUserType(typ), convertType(btyp), t@\loc));
				}
								
				// View
				case (Toplevel) `<Tags tgs> <Visibility vis> view <Name n> <: <Name sn> = <{Alternative "|"}+ alts> ;` : {
					throw "Not yet implemented";
				}
								
				default: throw "RSignature case not implemented for item <t>";
			}
		}
	}
	
	return sig;
}

// Find the types of items in a function signature, also taking care of the ... varargs case.
private list[RType] getParameterTypes(Parameters ps) {
	list[RType] pTypes = [ ];

	if (`( <Formals f> )` := ps) {
		if ((Formals)`<{Formal ","}* fs>` := f) {
			for ((Formal)`<Type t> <Name n>` <- fs) {
				pTypes += convertType(t);
			}
		}
	} else if (`( <Formals f> ... )` := ps) {
		varArgs = true;
		if ((Formals)`<{Formal ","}* fs>` := f) {
			for ((Formal)`<Type t> <Name n>` <- fs) {
				pTypes += convertType(t);
			}
			if (size(pTypes) > 0)
				pTypes[size(pTypes)-1] = RVarArgsType(head(tail(pTypes,1)));
			else
				pTypes += RVarArgsType(RValueType());			
		}
	}
	
	return pTypes;
}

// Get the type names introduced in the signature items. ADTs and aliases both introduce
// new type names.
private set[RName] getLocallyDefinedTypeNames(RSignature sig) {
	set[RName] definedTypeNames = { };
	for (si <- sig.signatureItems) {
		switch(si) {
			case AliasSigItem(t,_,_) : definedTypeNames += getUserTypeName(t);
			case ADTSigItem(t,_,_) : definedTypeNames += getUserTypeName(t); 
		}
	}
	return definedTypeNames;
}

// Mark types that are not defined locally, i.e., are not either built-ins (int, list, etc)
// or type names introduced in this module (via alias or data). These names need to be
// declared in one of the modules being imported.
private RSignature markUndefinedTypes(RSignature sig) {
	set[RName] localNames = getLocallyDefinedTypeNames(sig);
	sig.signatureItems = 
		visit(sig.signatureItems) {
			case RType t => (((RUserType(n) := t || RParameterizedUserType(n,_) := t)) && n notin localNames) ? RUnknownType(t) : t
		}
	return sig;
}

// Allow module headers to carry their signatures.
anno RSignature Header@sig;

// Given a module, annotate it with its signature.
public Tree addModuleSignature(Tree t) {
	return top-down-break visit(t) {
		case Header h => h[@sig = markUndefinedTypes(createRSignature(t))]
	}
}

// Given a module, return its signature.
public RSignature getModuleSignature(Tree t) {
	return markUndefinedTypes(createRSignature(t));
}
