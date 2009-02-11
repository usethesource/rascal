module PicoControlflow
 
import PicoAbstractSyntax;
import PicoPrograms;
import IO;

data CP = exp(EXP exp) | stat(STATEMENT stat);

data CFSEGMENT = cfsegment(set[CP] entry, 
                           rel[CP,CP] graph, 
                           set[CP] exit);
                           
CFSEGMENT cflow(PROGRAM P){
    if(program(list[DECL] Decls, list[STATEMENT] Stats) := P){
           return cflow(Stats);
    }
    return false;
}

CFSEGMENT cflow(list[STATEMENT] Stats){ 
    switch (Stats) {
      case [STATEMENT Stat, list[STATEMENT] Stats2]: { 
           CFSEGMENT CF1 = cflow(Stat);
           CFSEGMENT CF2 = cflow(Stats2);
           return cfsegment(CF1.entry, 
                   CF1.graph + CF2.graph + (CF1.exit * CF2.entry), 
                   CF2.exit);
      }
      case []: return cfsegment({}, {}, {});
    }
}

CFSEGMENT cflow(STATEMENT Stat){
    switch (Stat) {                
      case ifStat(EXP Exp, list[STATEMENT] Stats1,
                            list[STATEMENT] Stats2): {
           CFSEGMENT CF1 = cflow(Stats1);
           CFSEGMENT CF2 = cflow(Stats2);
           set[CP] E = {exp(Exp)};
           return cfsegment( E, 
                    (E * CF1.entry) + (E * CF2.entry) + 
                                      CF1.graph + CF2.graph,
                    CF1.exit + CF2.exit
                  );
      }
      
      case whileStat(EXP Exp, list[STATEMENT] Stats): {
           CFSEGMENT CF = cflow(Stats);
           set[CP] E = {exp(Exp)};
           return cfsegment(E, 
                    (E * CF.entry) + CF.graph + (CF.exit * E),
                    E
                  );
      }
         
      case STATEMENT Stat: return cfsegment({stat(Stat)}, {}, {stat(Stat)});
    }
}

public bool test(){
CFSEGMENT cf1 = cflow(
    program([decl("x", natural), decl("s", string)],
        [ asgStat("x", natCon(1)) /*,
          whileStat(id("x"),
                    [ asgStat("x", sub(id("x"), natCon(1))),
                      asgStat("s", conc(id("s"), strCon("#")))
                    ]
                   ) */
        ]
       )
       );
	println("<cf1>");

	CFSEGMENT cf = cflow(small);
	println("<cf>");
	return true;
}

