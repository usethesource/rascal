module demo::func::Parse

import demo::func::Func;
import ParseTree;

public Prog parse(loc l) = parse(#Prog, l);
public Prog parse(str s) = parse(#Prog, s);
