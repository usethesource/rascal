module demo::PicoAbstract::PicoControlflow
 
import demo::PicoAbstract::PicoAbstractSyntax;
import demo::PicoAbstract::PicoAnalysis;
import demo::PicoAbstract::PicoPrograms;
import IO;
import UnitTest;
                     

public BLOCK cflow(PROGRAM P){
    if(program(list[DECL] Decls, list[STATEMENT] Stats) := P){
            BLOCK ControlFlow = cflow(Stats);
            // Add a unique entry point to the graph
            ProgramEntry = {0};
            CFG = ({0} * ControlFlow.entry) + ControlFlow.graph;
            return block({0}, CFG, ControlFlow.exit);
    }
    return false;
}

public BLOCK cflow(list[STATEMENT] Stats){ 
    switch (Stats) {
    
      case [STATEMENT Stat]:
       		return cflow(Stat);
       		
      case [STATEMENT Stat, list[STATEMENT] Stats2]: {
           CF1 = cflow(Stat);
           CF2 = cflow(Stats2);
           return block(CF1.entry, 
                   CF1.graph + CF2.graph + (CF1.exit * CF2.entry), 
                   CF2.exit);
      }
      case []: return block({}, {}, {});
    }
     println("cflow returns no value");
}

public BLOCK cflow(STATEMENT Stat){
    switch (Stat) {                
      case ifStat(EXP Exp, list[STATEMENT] Stats1,
                            list[STATEMENT] Stats2): {
           BLOCK CF1 = cflow(Stats1);
           BLOCK CF2 = cflow(Stats2);
           set[ProgramPoint] E = {Exp@pos};
           return block( E, 
                    (E * CF1.entry) + (E * CF2.entry) + 
                                      CF1.graph + CF2.graph,
                    CF1.exit + CF2.exit
                  );
      }
      
      case whileStat(EXP Exp, list[STATEMENT] Stats): {
           BLOCK CF = cflow(Stats);
           set[ProgramPoint] E = {Exp@pos};
           return block(E, 
                    (E * CF.entry) + CF.graph + (CF.exit * E),
                    E
                  );
      }
         
      case STATEMENT Stat: return block({Stat@pos}, {}, {Stat@pos});
    }
    println("cflowstat returns no value");
}

public bool test(){

	assertTrue(cflow(annotate(small)) == block({0},{<9,5>,<5,10>,<12,10>,<10,9>,<0,12>},{10}));
          
	assertTrue(cflow(annotate(fac)) == block({0},{<21,19>,<18,14>,<23,21>,<26,24>,<7,24>,<14,19>,<24,23>,<19,7>,<0,28>,<19,18>,<28,26>},{24}));
	return report("PicoControlFlow");
}

