module analysis::m3::FlowGraph

extend analysis::m3::Core;

data M3(
    rel[loc decl, BasicBlocks blocks] basicBlocks = {},
    rel[loc decl, FlowGraph graph] dataFlow = {},
    rel[loc decl, FlowGraph graph] controlFlow = {}
);
    
alias BasicBlocks = rel[loc whole, list[loc] parts];
alias FlowGraph = rel[loc from, set[EdgeProperty] properties, loc to];
    
    
data EdgeProperty
    = trueCondition()
    | falseCondition()
    ;
    
