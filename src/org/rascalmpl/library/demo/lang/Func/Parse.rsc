module demo::lang::Func::Parse

import demo::lang::Func::Func;
import ParseTree;

public Prog parse(loc l) = parse(#Prog, l);
public Prog parse(str s) = parse(#Prog, s);
