module demo::lang::Pico::ControlFlow

import analysis::graphs::Graph;
import demo::lang::Pico::Abstract;
import demo::lang::Pico::Load;
import List;

// highlight-next-line
data CFNode // <1>
    = entry(loc location)
    | exit()
    | choice(loc location, EXP exp)
    | statement(loc location, STATEMENT stat)
    ;

// highlight-next-line
alias CFGraph = tuple[set[CFNode] entry, Graph[CFNode] graph, set[CFNode] exit]; // <2>

// highlight-next-line
CFGraph cflowStat(s:asgStat(PicoId Id, EXP Exp)) { // <3> 
   S = statement(s.src, s);
   return <{S}, {}, {S}>;
}

// highlight-next-line
CFGraph cflowStat(ifElseStat(EXP Exp,                   // <4>             
                              list[STATEMENT] Stats1,
                              list[STATEMENT] Stats2)) {
   CF1 = cflowStats(Stats1); 
   CF2 = cflowStats(Stats2); 
   E = {choice(Exp.src, Exp)}; 
   return < E, (E * CF1.entry) + (E * CF2.entry) + CF1.graph + CF2.graph, CF1.exit + CF2.exit >;
}

// highlight-next-line
CFGraph cflowStat(whileStat(EXP Exp, list[STATEMENT] Stats)) {  // <5>
   CF = cflowStats(Stats); 
   E = {choice(Exp.src, Exp)}; 
   return < E, (E * CF.entry) + CF.graph + (CF.exit * E), E >;
}

// highlight-next-line
CFGraph cflowStats(list[STATEMENT] Stats) { // <6>
  if(size(Stats) == 1) {
     return cflowStat(Stats[0]);
  }
  
  CF1 = cflowStat(Stats[0]);
  CF2 = cflowStats(tail(Stats));
  
  return < CF1.entry, CF1.graph + CF2.graph + (CF1.exit * CF2.entry), CF2.exit >;
}

// highlight-next-line
CFGraph cflowProgram(PROGRAM P:program(list[DECL] _, list[STATEMENT] Series)) { // <7>
   CF = cflowStats(Series);
   Entry = entry(P.src);
   Exit  = exit();
   
   return <{Entry}, ({Entry} * CF.entry) + CF.graph + (CF.exit * {Exit}), {Exit}>;
}

// highlight-next-line
CFGraph cflowProgram(str txt) = cflowProgram(load(txt)); // <8>

