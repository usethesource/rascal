module lang::rascalcore::compile::Examples::Tst1


data Symbol 
    = lbl(str name, Symbol symbol)
    | mp(Symbol from, Symbol to)
    ;
    
public int lub(mp(Symbol lf, Symbol lt), mp(Symbol rf, Symbol rt)) = 10 
when lbl(_,_) := lf, lbl(_,_) := lt, lbl(_,_) := rf, true;