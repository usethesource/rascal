module demo::lang::turing::l2::ast::Load

import ParseTree;
import demo::lang::turing::l2::ast::Turing;
import demo::lang::turing::l2::cst::Parse;

public Program load(str inp) 
	= implode(#Program, parse(inp));
	
public Program load(loc inp) 
	= implode(#Program, parse(inp));
	
public Program load(str s, loc inp) 
	= implode(#Program, parse(s, inp));
	
public Program load(Tree parsed) 
	= implode(#Program, parsed);
