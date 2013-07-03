module Traversal

@javaClass{org.rascalmpl.library.Prelude}
@reflect{uses information about active traversal evaluators at call site}
public java list[value] getTraversalContext();

public list[node] getTraversalContextNodes() {
	list[node] ln = [ n | node n <- getTraversalContext() ];
	return ln;
		}
