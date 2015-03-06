@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module Traversal

@javaClass{org.rascalmpl.library.Prelude}
@reflect{uses information about active traversal evaluators at call site}
public java list[value] getTraversalContext();

public list[node] getTraversalContextNodes() {
	list[node] ln = [ n | node n <- getTraversalContext() ];
	return ln;
}
