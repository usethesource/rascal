module rascal::checker::TreeUtils

import ParseTree;
import List;

public list[Tree] getProductionChildren(Tree t) { 
    if (list[Tree] l1 := t[1]) 
        return l1; 
    else 
        return []; 
}

public map[int,str] getProductionTypes(Tree t) {
    map[int,str] mt = ( );

    if (Production p := t[0]) {
        if (list[Symbol] ls := p[0]) {
            for (n <- domain(ls)) {
                if (containsSort(ls[n])) mt[n] = getSortNameFromSymbol(ls[n]);
            }
        }
    }

    return mt;
}

public bool containsSort(Symbol s) {
    if (Symbol sym := s[0] && sort(srt) := sym) return true;
    return false;
}

public str getSortNameFromSymbol(Symbol s) {
    if (Symbol sym := s[0] && sort(srt) := sym) return srt;
    throw "No Match Found for Sort in Symbol <s>";
}

public list[Tree] prodFilter(Tree t, bool(Production) fltr) {
    if (appl(p,list[Tree] tl) := t) {
        if (fltr(p)) {
            return [ t ];
        } else {
            return [ prodFilter(tli,fltr) | tli <- tl ];
        }
    } else {
        return [  ] ;
    }
}

public list[Tree] getAllNodes(Tree t) {
    if (appl(p,list[Tree] tl) := t) {
        return [ t ] + [ getAllNodes(tli) | tli <- tl ];
    } else {
        return [  ] ;
    }
}