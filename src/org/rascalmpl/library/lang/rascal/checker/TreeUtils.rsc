@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::rascal::checker::TreeUtils

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
            for (int n <- index(ls)) {
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
            return [ *prodFilter(tli,fltr) | tli <- tl ];
        }
    } else {
        return [  ] ;
    }
}

public list[Tree] getAllNodes(Tree t) {
    if (appl(p,list[Tree] tl) := t) {
        return [ t ] + [ *getAllNodes(tli) | Tree tli <- tl ];
    } else {
        return [  ] ;
    }
}

public list[Tree] getTupleItems(Tree t) {
    list[Tree] tupleParts = [ ];

    // t[1] holds the parse tree contents for the tuple
    if (list[Tree] tupleTop := t[1]) {
        // tupleTop[0] = <, tupleTop[1] = layout, tupleTop[2] = tuple contents, tupleTop[3] = layout, tupleTop[4] = >, so we get out 2
        if (appl(_,list[Tree] tupleItems) := tupleTop[2]) {
            if (size(tupleItems) > 0) {
                // The tuple items include layout and commas, so we use a mod 4 to account for this: we have
                // item layout comma layout item layout comma layout etc
                tupleParts = [ tupleItems[n] | n <- [0..size(tupleItems)], n % 4 == 0];
            }
        }
    }

    return tupleParts;
}

public list[Tree] getMapMappings(Tree t) {
    list[Tree] mapParts = [ ];

    // t[1] holds the parse tree contents for the map
    if (list[Tree] mapTop := t[1]) {
        // mapTop[0] = (, mapTop[1] = layout, mapTop[2] = map contents, mapTop[3] = layout, mapTop[4] = ), so we get out 2
        if (appl(_,list[Tree] mapItems) := mapTop[2]) {
            if (size(mapItems) > 0) {
                // The map items include layout and commas, so we use a mod 4 to account for this: we have
                // item layout comma layout item layout comma layout etc
                list[Tree] mapMappings = [ mapItems[n] | n <- [0..size(mapItems)], n % 4 == 0];

                // Each item should have the domain and range inside. It is organized as pat layout : layout pat
                for (n <- [0..size(mapMappings)]) {
                    if (appl(_,list[Tree] mapContents) := mapMappings[n]) {
                        if (size(mapContents) == 5, Tree tl := mapContents[0], Tree tr := mapContents[4]) {
                            mapParts = mapParts + [ tl, tr ]; 
                        }
                    } 
                }
            }
        }
    }

    return mapParts;
}