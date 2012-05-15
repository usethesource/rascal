module demo::lang::turing::l2::ide::Outline

import demo::lang::turing::l2::ast::Turing;
import demo::lang::turing::l2::format::Format;
import lang::box::util::Box2Text;
import List;

public node turing2outline(Program p) = "program"(stats2outline(p.statements));

public node stat2outline(l:loop(n, ss)) 
  = "loop"(stats2outline(ss))[@label="rep <n>"][@\loc=l@location];

public default node stat2outline(Statement s) 
  = "stat"()[@label=format(stat2box(s))][@\loc=s@location];

public list[node] stats2outline(list[Statement] ss) {
  result = while (ss != []) {
    <h, ss> = headTail(ss);
    if (h is label) {
      kids = takeWhile(ss, bool(Statement s) { return !(s is label); });
      ss = drop(size(kids), ss);
      append "labeled"([stat2outline(k) | k <- kids])[@label=h.name][@\loc=h@location]; 
    }
    else {
      append stat2outline(h);
    }
  }
  return result;
}

