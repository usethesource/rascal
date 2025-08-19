module lang::rascal::tests::basic::RepositionTree

import List;
import ParseTree;
import lang::pico::\syntax::Main;
import String;

loc facPico = |project://rascal/src/org/rascalmpl/library/lang/pico/examples/fac.pico|;

private list[loc] collect(Tree t) = [s@\loc | /Tree s := t, s@\loc?];

test bool repositionSimulatesReparse() {
    t1 = parse(#start[Program], facPico);
    t2 = reposition(t1); // defaults set
    assert t1 := t2; // but that skips keyword parameters and layout
    return collect(t1) == collect(t2);
}

test bool removeAllAnnotations() {
    t1 = parse(#start[Program], facPico);
    t2 = reposition(t1, 
        markSyntax=false, 
        markLexical=false, 
        markSubLexical=false, 
        markAmb=false, 
        markChar=false, 
        markLayout=false,
        markLit=false,
        markStart=false,
        markSubLit=false,
        markSubLayout=false,
        markRegular=false);
    assert t1 := t2; // but that skips keyword parameters and layout
    return collect(t2) == [];
}

test bool charsFromLeftToRight() {
    // removing \r from this test to avoid problems.
    t1 = parse(#start[Program], facPico);
    t2 = reposition(t1, markChar=true);
    allChars = [ch | /ch:char(_) := t2];
    sortedChars = sort(allChars, bool (Tree c1, Tree c2) { return c1@\loc.offset < c2@\loc.offset;});

    return allChars == sortedChars;
}