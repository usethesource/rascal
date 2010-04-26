module rascal::checker::Signature

import rascal::checker::Types;

import rascal::\old-syntax::Rascal;

import List;
import Set;

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

alias RSignature = tuple[set[RSignatureItem] signatureItems, RName moduleName, set[RName] imports];

private set[RName] getImportInfo(Import* imports) {
	return { }; // TODO: Obviously, add some code here
}

private RSignature addSignatureItem(RSignature sig, RSignatureItem item) {
	return sig[signatureItems = sig.signatureItems + item];
}
 
private RSignature createRSignature(Tree t) {
	if ((Module) `<Header h> <Body b>` := t) {
		switch(h) {
			case `<Tags t> module <QualifiedName n> <Import* i>` : {
				RSignature sig = <{  }, convertName(n), getImportInfo(i)>;
				sig = createModuleBodySignature(b,sig);
				return sig;
			}

			case `<Tags t> module <QualifiedName n> <ModuleParameters p> <Import* i>` : {
				RSignature sig = <{  }, convertName(n), getImportInfo(i)>;
				sig = createModuleBodySignature(b,sig);
				return sig;
			}

			default : throw "buildNamespace: unexpected syntax for module";
		}
	} else {
        throw "buildNamespace: missed a case for <t.prod>";
	}
}

private RSignature createModuleBodySignature(Body b, RSignature sig) {
	if (`<Toplevel* ts>` := b) {
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
							case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ thrs> ` :
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
							case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ thrs> ` :
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
					sig = addSignatureItem(sig, AliasSigItem(convertType(typ), convertType(btyp), t@\loc));
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

private list[RType] getParameterTypes(Parameters ps) {
	list[RType] pTypes = [ ];

	if (`( <Formals f> )` := ps) {
		if (`<{Formal ","}* fs>` := f) {
			for ((Formal)`<Type t> <Name n>` <- fs) {
				pTypes += convertType(t);
			}
		}
	} else if (`( <Formals f> ... )` := ps) {
		varArgs = true;
		if (`<{Formal ","}* fs>` := f) {
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

private RSignature markUndefinedTypes(RSignature sig) {
	set[RName] localNames = getLocallyDefinedTypeNames(sig);
	sig.signatureItems = 
		visit(sig.signatureItems) {
			case RType t => (((RUserType(n) := t || RParameterizedUserType(n,_) := t)) && n notin localNames) ? RUnknownType(t) : t
		}
	return sig;
}

anno RSignature Header@sig;

public Tree addModuleSignature(Tree t) {
	return top-down-break visit(t) {
		case Header h => h[@sig = markUndefinedTypes(createRSignature(t))]
	}
}

public RSignature getModuleSignature(Tree t) {
	return markUndefinedTypes(createRSignature(t));
}

