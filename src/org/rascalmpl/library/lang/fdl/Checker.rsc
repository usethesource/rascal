@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jouke Stoel - Jouke.Stoel@cwi.nl}
module Checker

import AST;
import Resolver;
import QuickFixer;

import Message;
import Set;
import IO;
import Relation;
import ParseTree;

set[Message] check(Defs defs, Refs refs, RefsInDef refsInDef, ResolvedRefs resolvedRefs) 
	= checkReferences(refs, resolvedRefs)
	+ checkDoubleDefinitions(defs)
	+ checkCircularReferences(refsInDef, resolvedRefs)
	;
		
private set[Message] checkDoubleDefinitions(defs) =
	{ error("Multiple features with this name exists in this diagram", location) | <name, location> <- defs, size(defs[name]) > 1};	
	
private set[Message] checkReferences(Refs refs, ResolvedRefs resolvedRefs) =
	{ error("Referenced feature is unkown, did you declare it?", location,
	    quickFixes = [<"Declare as feature", introduceFeature>]
	    ) | location <- refs<1> - resolvedRefs<0>};

private set[Message] checkCircularReferences(RefsInDef refsInDef, ResolvedRefs resolvedRefs) =
	{ error("Circular feature reference found", ref)| <ref, ref> <- (refsInDef o resolvedRefs)+};