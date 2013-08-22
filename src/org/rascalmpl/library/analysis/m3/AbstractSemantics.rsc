module analysis::m3::AbstractSemantics

// These relations are produced by statically or dynamically analyzing code.
// if the relation is produced by static analysis, we should assume they over-approximate the truth, 
// if they are produced by dynamic analyse, they might under-approximate it. The goal of these
// relations is to provide a common abstract interface for the kind of information the represent, 
// for use in downstream reusable analyses or visualizations.

data Type; // whatever is used to represent a type in the analyzed language(s)

rel[loc \in, loc out] controlFlowGraph;
rel[loc \in, loc out] dataFlowGraph;
rel[loc caller, loc callee] callGraph;
rel[loc dependent, loc dependee] dependencyGraph;
rel[loc use, Type typ] typeOf;
