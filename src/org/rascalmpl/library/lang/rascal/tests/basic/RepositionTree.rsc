module lang::rascal::tests::basic::RepositionTree

import ParseTree;
import lang::pico::\syntax::Main;

loc facPico = |project://rascal/src/org/rascalmpl/library/lang/pico/examples/fac.pico|;

private list[loc] collect(Tree t) = [s@\loc | /Tree s := t, s@\loc?];

test bool repositionSimulatesReparse() {
    t1 = parse(#start[Program], facPico);
    t2 = reposition(t1); // defaults set
    assert t1 := t2; // but that skips keyword parameters and layout
    return collect(t1) == collect(t2);
}