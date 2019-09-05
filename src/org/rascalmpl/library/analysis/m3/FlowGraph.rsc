module analysis::m3::FlowGraph

data FlowGraph(
    rel[loc, loc, set[EdgeProperty]] dataFlow = {},
    rel[loc, loc, set[EdgeProperty]] controlFlow = {}

)
    = flow(loc project);
    
data EdgeProperty
    = trueCondition()
    | falseCondition()
    ;
    
