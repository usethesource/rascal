module lang::saf::Parse

import lang::saf::SAF;
import ParseTree;

public start[Fighter] parse(str src, loc l) = parse(#start[Fighter], src, l);
public start[Fighter] parse(str src) = parse(#start[Fighter], src);
public start[Fighter] parse(loc l) = parse(#start[Fighter], l);
