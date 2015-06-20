@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jouke Stoel - Jouke.Stoel@cwi.nl}
module Resolver

import AST;
import Parse;
import Load;
import Set;

alias Included = set[FeatureDiagram];
alias Refs = rel[QualifiedName name, loc location];
alias Defs = rel[QualifiedName name, loc location];
alias ResolvedRefs = rel[loc ref, loc def];
alias RefsInDef = rel[loc def, loc ref];

Included resolveIncludes(FeatureDiagram fd) = resolveIncludes(fd, {});

private Included resolveIncludes(FeatureDiagram fd, set[str] alreadyIncluded) {
	alreadyIncluded += fd.name;

	includes = {};
	for (include <- fd.includes, include notin alreadyIncluded) {
		includes += resolveIncludes(load(fd@location.top[file="<include>.fdl"]), alreadyIncluded);
	}
				
	return fd + includes;
}

Defs resolveDefinitions(Included diagrams) =
	{<qn(diagram.name, name), def@location> | diagram <- diagrams, /def:definition(str name, _) <- diagram} +
	{<qn("<diagram.name>.<defName>", name), atom@location> | diagram <- diagrams, /def:definition(str defName, _) <- diagram, /atom:atomic(str name) <- def};

Refs resolveReferences(Included diagrams) =
	{<qn(diagram.name, name), ref@location> | diagram <- diagrams, /ref:ref(qn(name)) <- diagram} +
	{<qn(ns, name), ref@location> | diagram <- diagrams, /ref:ref(qn(ns,name)) <- diagram};
	
RefsInDef resolveReferencesInDefinitions(Included diagrams) =
	{<def@location, ref@location> | /def:definition(_, fe) <- diagrams, /ref:ref(_) <- fe};	
	
ResolvedRefs resolveFeatures(Defs definitions, Refs references) =
	{<use, df> | <qn, use> <- references, {df} := definitions[qn]};	
	