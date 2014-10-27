@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Anastasia Izmaylova - Anastasia.Izmaylova@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::types::Util

import lang::rascal::types::AbstractName;
import lang::rascal::\syntax::Rascal;
import ParseTree;

//syntax Assignable
//	= bracket \bracket   : "(" Assignable arg ")"
//	| variable          : QualifiedName qualifiedName
//    | subscript         : Assignable receiver "[" Expression subscript "]" 
//    | slice             : Assignable receiver "[" OptionalExpression optFirst ".." OptionalExpression optLast "]" 
//    | sliceStep         : Assignable receiver "[" OptionalExpression optFirst "," Expression second ".." OptionalExpression optLast "]"     
//	| fieldAccess       : Assignable receiver "." Name field 
//	| ifDefinedOrDefault: Assignable receiver "?" Expression defaultExpression 
//	| constructor       : Name name "(" {Assignable ","}+ arguments ")"  
//	| \tuple             : "\<" {Assignable ","}+ elements "\>" 
//	| annotation        : Assignable receiver "@" Name annotation  ;
public rel[RName,loc] getIntroducedNames((Assignable)`( <Assignable a> )`) = getIntroducedNames(a);
public rel[RName,loc] getIntroducedNames((Assignable)`<QualifiedName qn>`) = { < convertName(qn), qn@\loc > };
public rel[RName,loc] getIntroducedNames((Assignable)`<Assignable a> [ <Expression _> ]`) = { };
public rel[RName,loc] getIntroducedNames((Assignable)`<Assignable a> [ <OptionalExpression _> .. <OptionalExpression _> ]`) = { };
public rel[RName,loc] getIntroducedNames((Assignable)`<Assignable a> [ <OptionalExpression _> , <Expression _> .. <OptionalExpression _> ]`) = { };
public rel[RName,loc] getIntroducedNames((Assignable)`<Assignable a> . <Name _>`) = { };
public rel[RName,loc] getIntroducedNames((Assignable)`<Assignable a> ? <Expression _>`) = { };
public rel[RName,loc] getIntroducedNames((Assignable)`<Name _> ( <{Assignable ","}+ al> )`) = { }; // NOTE: We should remove this case, it is unsafe
public rel[RName,loc] getIntroducedNames((Assignable)`\< <{Assignable ","}+ al> \>`) = { *getIntroducedNames(ali) | ali <- al };
public rel[RName,loc] getIntroducedNames((Assignable)`<Assignable a>  @ <Name _>`) = { };
public default rel[RName,loc] getIntroducedNames(Assignable a) { throw "Case not handled: <a>"; }
