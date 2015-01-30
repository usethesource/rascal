@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::types::AbstractKind

import ParseTree;
import lang::rascal::\syntax::Rascal;

@doc{Kinds of tags}
data TagKind 
	= functionKind() 
	| variableKind() 
	| allKind() 
	| annoKind() 
	| dataKind() 
	| viewKind() 
	| aliasKind() 
	| moduleKind() 
	| tagKind();

@doc{Convert from the concrete to the abstract representation of tag kinds.}
public TagKind convertKind((Kind)`function`) = functionKind();
public TagKind convertKind((Kind)`variable`) = variableKind();
public TagKind convertKind((Kind)`all`) = allKind();
public TagKind convertKind((Kind)`anno`) = annoKind();
public TagKind convertKind((Kind)`data`) = dataKind();
public TagKind convertKind((Kind)`view`) = viewKind();
public TagKind convertKind((Kind)`alias`) = aliasKind();
public TagKind convertKind((Kind)`module`) = moduleKind();
public TagKind convertKind((Kind)`tag`) = tagKind();

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
