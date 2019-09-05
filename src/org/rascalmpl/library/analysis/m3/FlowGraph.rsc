module analysis::m3::FlowGraph

import analysis::m3::Core;

data M3(
    rel[loc, list[loc]] basicBlocks = {},
    rel[loc, loc, set[EdgeProperty]] dataFlow = {},
    rel[loc, loc, set[EdgeProperty]] controlFlow = {}
);
    
data EdgeProperty
    = trueCondition()
    | falseCondition()
    ;
    
