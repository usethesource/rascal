module lang::rascalcore::compile::Examples::Tst1


//data Symbol 
//    = \label(str name, Symbol symbol)
//    | \map(Symbol from, Symbol to)
//    ;
//    
//public Symbol lub(Symbol::\map(Symbol lf, Symbol lt), Symbol::\map(Symbol rf, Symbol rt)) {
//    if(\label(_,_) := lf, \label(_,_) := lt, \label(_,_) := rf , \label(_,_) := rt){
//        return Symbol::\map(lub(lf,rf), lub(lt,rt));
//    } else fail;
//    
//}
//when \label(_,_) := lf, \label(_,_) := lt, \label(_,_) := rf , \label(_,_) := rt;


public int lub(node a, node b, node c, node d) {
    if("f"(1) := a, "f"(2) := b, "f"(3) := c, "f"(4) := d){
        return 10;
    } elsereturn 20;
    
}