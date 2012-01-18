@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::types::AbstractKind

import ParseTree;
import Type;
import lang::rascal::syntax::RascalRascal;

@doc{Definition of abstract representation of kinds.}
data RKind = 
	  functionKind() 
	| variableKind() 
	| allKind() 
	| annoKind() 
	| dataKind() 
	| viewKind() 
	| aliasKind() 
	| moduleKind() 
	| tagKind()
	;
             
@doc{Convert a concrete into an abstract kind.}             
public RKind convertKind(Kind k) {
    switch(k) {
        case (Kind) `function` : return FunctionKind();
        case (Kind) `variable` : return VariableKind();
        case (Kind) `all` : return AllKind();
        case (Kind) `anno` : return AnnoKind();
        case (Kind) `data` : return DataKind();
        case (Kind) `view` : return ViewKind();
        case (Kind) `alias` : return AliasKind();
        case (Kind) `module` : return ModuleKind();
        case (Kind) `tag` : return TagKind();
    }
    throw "convertKind, error, no match for kind <k>";
}

@doc{Pretty print abstract kinds.}
public str prettyPrintKind(functionKind()) = "function";
public str prettyPrintKind(variableKind()) = "variable";
public str prettyPrintKind(allKind()) = "all";
public str prettyPrintKind(annoKind()) = "anno";
public str prettyPrintKind(dataKind()) = "data";
public str prettyPrintKind(viewKind()) = "view";
public str prettyPrintKind(aliasKind()) = "alias";
public str prettyPrintKind(moduleKind()) = "module";
public str prettyPrintKind(tagKind()) = "tag";
public default str prettyPrintKind(Kind _) {
	throw "prettyPrintKind, error, no match for rkind <rk>";
}
