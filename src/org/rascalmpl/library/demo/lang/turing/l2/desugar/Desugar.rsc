module demo::lang::turing::l2::desugar::Desugar

import List;
import IO;
import demo::lang::turing::l2::ast::Turing;

public Program desugar(Program prog) 
	= removeLabels(removeLoops(prog));
	

private Program removeLoops(Program prog) {
	list[Statement] newStatements = [];
	int loopId = 0;
	for (s <- prog.statements) {
		if (loop(c, st) := s) {
			loopId += 1;
			for (lc <- [1..c]) {
				map[str, str] labelReplacement = (l : "_<loopId>_<l>_<lc>" | label(l) <- st);
				for (st_l <- st) {
					switch(st_l) {
						case label(l) : newStatements += [st_l[name = labelReplacement[l]? l]];	
						case jumpAlwaysLabel(l) : newStatements += [st_l[name = labelReplacement[l]? l]];	
						case jumpSetLabel(l) : newStatements += [st_l[name = labelReplacement[l]? l]];	
						case jumpUnsetLabel(l) : newStatements += [st_l[name = labelReplacement[l]? l]];	
						default: newStatements += [st_l];
					}
				}
			}		
		}
		else {
			newStatements += [s];	
		}	
	}
	return prog[statements = newStatements];
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
