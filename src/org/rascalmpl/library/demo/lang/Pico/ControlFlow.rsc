module demo::lang::Pico::ControlFlow

import Prelude;
import demo::lang::Pico::Abstract;
import demo::lang::Pico::Assembly;
import demo::lang::Pico::Load;


data CP = exp(EXP exp) | stat(STATEMENT stat);

alias CFSEGMENT = tuple[set[CP] entry, rel[CP,CP] graph, set[CP] exit];

CFSEGMENT cflowStat(asgStat(PicoId Id, EXP Exp)) {
  set[CP] E = {exp(Exp)};
  return <E, {}, E>;
}

CFSEGMENT cflowStat(whileStat(EXP Exp, list[STATEMENT] Stats)) { 
   CFSEGMENT CF = cflow(Stats); 
   set[CP] E = {exp(Exp)}; 
   return < E, (E * CF.entry) + CF.graph + (CF.exit * E), E >;
}

CFSEGMENT cflowStat(ifElseStat(EXP Exp, 
                              list[STATEMENT] Stats1,
                              list[STATEMENT] Stats2)){
   CFSEGMENT CF1 = cflow(Stats1); 
   CFSEGMENT CF2 = cflow(Stats2); 
   set[CP] E = {exp(Exp)}; 
   return < E, (E * CF1.entry) + (E * CF2.entry) + CF1.graph + CF2.graph, CF1.exit + CF2.exit >;
}

CFSEGMENT cflow(list[STATEMENT] Stats){
    return cflowStats(Stats);
    }

CFSEGMENT cflowStats(list[STATEMENT] Stats){ 
  if(size(Stats) == 1)
     return cflowStat(Stats[0]);
  CF1 = cflowStat(Stats[0]);
  CF2 = cflowStats(tail(Stats));
  return < CF1.entry, CF1.graph + CF2.graph + (CF1.exit * CF2.entry), CF2.exit >;
}

public CFSEGMENT cflowProgram(PROGRAM P){
  if(program(list[DECL] Decls, list[STATEMENT] Series) := P){
     CF = cflowStats(Series);
     Entry = exp(strCon("ENTRY"));
     Exit = exp(strCon("EXIT"));
     return <{Entry}, ({Entry} * CF.entry) + CF.graph + (CF.exit * {Exit}), {Exit}>;
  } else
    throw "Cannot happen";
}

public CFSEGMENT cflowProgram(str txt) = cflowProgram(load(txt));


