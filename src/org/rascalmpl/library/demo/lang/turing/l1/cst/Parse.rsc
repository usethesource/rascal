module demo::lang::turing::l1::cst::Parse

import ParseTree;
import demo::lang::turing::l1::cst::Syntax;


public start[Program] parse(str inp) 
	= parse(#start[Program], inp);
	
public start[Program] parse(loc inp) 
	= parse(#start[Program], inp);
	
public start[Program] parse(str s, loc inp) 
	= parse(#start[Program], s, inp);
