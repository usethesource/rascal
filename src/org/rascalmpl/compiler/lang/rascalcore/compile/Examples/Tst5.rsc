module lang::rascalcore::compile::Examples::Tst5

import ParseTree;

// collectAndSolve shorthand for a common, simple, scenario

Tree main(Tree pt){
    if(pt has top) pt = pt.top;
    return pt;
}