module demo::lang::Pico::Load

import Prelude;
import demo::lang::Pico::Syntax;
import demo::lang::Pico::Abstract;

public PROGRAM load(str txt) // <1>
    = /*<3>*/ implode(#PROGRAM, /*<2>*/ parse(#start[Program], txt).top);
