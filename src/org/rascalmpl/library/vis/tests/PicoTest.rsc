module vis::tests::PicoTest

import vis::Dot;
import ParseTree;
import Content;
import lang::pico::\syntax::Main;

Content showP1Tree() {
    Program p1 = parse(#Program, |std:///lang/pico/examples/P1.pico|);
    return content("P1", dotServer(valueToDot(p1, name="P1")));
}