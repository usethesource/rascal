module demo::lang::turing::l2::desugar::Desugar

import List;
import IO;
import demo::lang::turing::l2::ast::Turing;

	
public Program expandLoops(Program p) {
  return innermost visit (p) {
    case [*stats1, loop(n, sts), *stats2] => [*stats1, *renameLabels(n,sts), loop(n-1, sts), *stats2]
      when n > 0
    case [*stats1, loop(0, sts), *stats2] => [*stats1, *stats2]
  }
}

public list[Statement] renameLabels(int n, list[Statement] ss) 
  = [ (s has name) ? s[name="<s.name>_<n>"] : s | s <- ss ]; 

public Program labelsToLineNumbers(Program p) {
  lineNo = 1;
  labels = ();
    
  for (s <- p.statements) {
    if (label(l) := s) {
      labels[l] = lineNo; 
    }
    else {
      lineNo += 1;
    }   
  }
  p.statements = [ labelToLineNo(s, labels) | s <- p.statements, !(s is label) ];
  return p;
}

public Program(Program) desugar = expandLoops o labelsToLineNumbers;


public Statement labelToLineNo(jumpAlwaysLabel(n), ren) = jumpAlways(ren[n]); 
public Statement labelToLineNo(jumpSetLabel(n), ren) = jumpSet(ren[n]); 
public Statement labelToLineNo(jumpUnsetLabel(n), ren) = jumpUnset(ren[n]);
public default Statement labelToLineNo(Statement x, ren) = x; 

