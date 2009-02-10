module PicoControlflow
 
import PicoAbstractSyntax;

data CP = exp(EXP) | stat(STATEMENT);

alias CFSEGMENT = tuple[set[CP] entry, 
                        rel[CP,CP] graph, 
                        set[CP] exit];

CFSEGMENT cflow(list[STATEMENT] Stats){ 
    switch (Stats) {
      case [STATEMENT Stat, list[STATEMENT] Stats2]: { 
           CFSEGMENT CF1 = cflow(Stat);
           CFSEGMENT CF2 = cflow(Stats2);
           return <CF1.entry, 
                   CF1.graph + CF2.graph + (CF1.exit x CF2.entry), 
                   CF2.exit>;
      }
      case []: return <{}, {}, {}>;
    }
}

CFSEGMENT cflow(STATEMENT Stat){
    switch (Stat) {                
      case ifStat(EXP Exp, list[STATEMENT] Stats1,
                            list[STATEMENT] Stats2): {
           CFSEGMENT CF1 = cflow(Stats1);
           CFSEGMENT CF2 = cflow(Stats2);
           set[CP] E = {exp(Exp)};
           return < E, 
                    (E x CF1.entry) + (E x CF2.entry) + 
                                      CF1.graph + CF2.graph,
                    CF1.exit + CF2.exit
                  >;
      }
      
      case whileStat(EXP Exp, list[STATEMENT] Stats): {
           CFSEGMENT CF = cflow(Stats);
           set[CP] E = {exp(Exp)};
           return < E, 
                    (E x CF.entry) + CF.graph + (CF.exit x E),
                    E
                  >;
      }
         
      case STATEMENT Stat: return <{Stat}, {}, {Stat}>;
    }
}
