module demo::lang::Pico::Load

import Prelude;
import demo::lang::Pico::Syntax;
import demo::lang::Pico::Abstract;

public PROGRAM load(str txt) 
    = implode(#PROGRAM, parse(#Program, txt));
