module vis::tests::PicoTest

import vis::Dot;
import ParseTree;
import Content;
import lang::pico::\syntax::Main;

Content showP1Full() {
    Program p1 = parse(#Program, |std:///lang/pico/examples/P1.pico|);
    return content("P1 full", dotServer(valueToDot(p1, name="P1 full", config=createParseTreeConfigFull(filterLayout=true))));
}

Content showP1Compact() {
    Program p1 = parse(#Program, |std:///lang/pico/examples/P1.pico|);
    return content("P1 compact", dotServer(valueToDot(p1, name="P1 compact", config=createParseTreeConfig())));
}

Content showP1Yield() {
    Program p1 = parse(#Program, |std:///lang/pico/examples/P1.pico|);
    return content("P1 yield", dotServer(valueToDot(p1, name="P1 yield", config=createYieldParseTreeConfig())));
}