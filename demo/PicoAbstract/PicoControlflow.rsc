module PicoControlflow
 
import PicoAbstractSyntax;
import PicoAnalysis;
import PicoPrograms;
import IO;
import UnitTest;
                     

public BLOCK cflow(PROGRAM P){
    if(program(list[DECL] Decls, list[STATEMENT] Stats) := P){
           return cflow(Stats);
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
           set[ProgramPoint] E = {Exp@pos}; // {pp(Exp)};
           return block( E, 
                    (E * CF1.entry) + (E * CF2.entry) + 
                                      CF1.graph + CF2.graph,
                    CF1.exit + CF2.exit
                  );
      }
      
      case whileStat(EXP Exp, list[STATEMENT] Stats): {
           BLOCK CF = cflow(Stats);
           set[ProgramPoint] E = {Exp@pos}; //{pp(Exp)};
           return block(E, 
                    (E * CF.entry) + CF.graph + (CF.exit * E),
                    E
                  );
      }
         
      case STATEMENT Stat: {PP = pp(Stat); return block({PP}, {}, {PP});}
    }
    println("cflowstat returns no value");
}

public bool test(){
   
	assertTrue(
       cflow([asgStat("x", natCon(1))[@pos=1],  asgStat("s", conc(id("s"), strCon("#")))[@pos=2] ]) ==
       block({1}, {<1,2>}, {2})
             );
	
	assertTrue(cflow(small) == block({1}, {<2,3>, <3,4>, <4,2>, <1,2>}, {2}));
          
	assertTrue(cflow(fac) == block({1},{<8,6>,<1,2>,<3,4>,<2,3>,<6,7>,<7,8>,<9,3>,<4,5>,<5,6>,<6,9>}, {3}) );

	return report();
}

