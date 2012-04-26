module demo::lang::turing::l2::desugar::Desugar

import List;
import IO;
import demo::lang::turing::l2::ast::Turing;

public Program desugar(Program prog) 
	= removeLabels(removeLoops(prog));
	

private Program removeLoops(Program prog) {
	return prog;
}

private Program removeLabels(Program prog) {
	map[str, int] labelLocs = ();
	list[Statement] newStatements = [];
	for (s <- prog.statements) {
		if (label(l) := s) {
			labelLocs[l] = size(newStatements) + 1;	
		}
		else {
			newStatements += [s];	
		}	
	}
	return prog[statements = visit(newStatements) {
			case jumpAlwaysLabel(l) => jumpAlways(labelLocs[l])	
			case jumpSetLabel(l) => jumpSet(labelLocs[l])	
			case jumpUnsetLabel(l) => jumpUnset(labelLocs[l])	
		}
	];
} 
