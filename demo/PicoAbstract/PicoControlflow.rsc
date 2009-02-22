module PicoControlflow
 
import PicoAbstractSyntax;
import PicoAnalysis;
import PicoPrograms;
import IO;
import UnitTest;
                     

public BLOCK cflow(PROGRAM P){
    resetLabelGen();
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
           set[ProgramPoint] E = {pp(Exp)};
           return block( E, 
                    (E * CF1.entry) + (E * CF2.entry) + 
                                      CF1.graph + CF2.graph,
                    CF1.exit + CF2.exit
                  );
      }
      
      case whileStat(EXP Exp, list[STATEMENT] Stats): {
           BLOCK CF = cflow(Stats);
           set[ProgramPoint] E = {pp(Exp)};
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
	
    resetLabelGen();
	assertTrue(
       cflow([asgStat("x", natCon(1)),  asgStat("s", conc(id("s"), strCon("#")))] ) ==
       block({pp(asgStat("x",natCon(1)),1)},
             {<pp(asgStat("x",natCon(1)),1),pp(asgStat("s",conc(id("s"),strCon("#"))),2)>},
             {pp(asgStat("s",conc(id("s"),strCon("#"))),2)})
             );
	
    resetLabelGen();
	assertTrue(
    cflow(small) ==
    block({pp(asgStat("x",natCon(3)),1)},
          {<pp(id("x"),4),pp(asgStat("x",sub(id("x"),natCon(1))),2)>,
           <pp(asgStat("x",sub(id("x"),natCon(1))),2),pp(asgStat("s",conc(id("s"),strCon("#"))),3)>,
           <pp(asgStat("s",conc(id("s"),strCon("#"))),3),pp(id("x"),4)>,
           <pp(asgStat("x",natCon(3)),1),pp(id("x"),4)>},
          {pp(id("x"),4)})
          );

	resetLabelGen();
	assertTrue(
    cflow(fac) ==
    block({pp(asgStat("input",natCon(13)),1)},
          {<pp(asgStat("repnr",sub(id("repnr"),natCon(1))),6),pp(sub(id("repnr"),natCon(1)),7)>,
           <pp(asgStat("input",natCon(13)),1),pp(asgStat("output",natCon(1)),2)>,
           <pp(sub(id("input"),natCon(1)),9),pp(asgStat("rep",id("output")),3)>,
           <pp(asgStat("output",natCon(1)),2),pp(sub(id("input"),natCon(1)),9)>,
           <pp(sub(id("repnr"),natCon(1)),7),pp(asgStat("output",add(id("output"),id("rep"))),5)>,
           <pp(asgStat("output",add(id("output"),id("rep"))),5),pp(asgStat("repnr",sub(id("repnr"),natCon(1))),6)>,
           <pp(asgStat("input",sub(id("input"),natCon(1))),8),pp(sub(id("input"),natCon(1)),9)>,
           <pp(asgStat("rep",id("output")),3),pp(asgStat("repnr",id("input")),4)>,
           <pp(asgStat("repnr",id("input")),4),pp(sub(id("repnr"),natCon(1)),7)>,
           <pp(sub(id("repnr"),natCon(1)),7),pp(asgStat("input",sub(id("input"),natCon(1))),8)>},
          {pp(sub(id("input"),natCon(1)),9)})
          );

return report();
}

