module PicoControlflow
 
import PicoAbstractSyntax;
import PicoPrograms;
import IO;
import UnitTest;

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
    
      case [STATEMENT Stat]:
       		return cflow(Stat);
       		
      case [STATEMENT Stat, list[STATEMENT] Stats2]: {
           CF1 = cflow(Stat);
           CF2 = cflow(Stats2);
           return cfsegment(CF1.entry, 
                   CF1.graph + CF2.graph + (CF1.exit * CF2.entry), 
                   CF2.exit);
      }
      case []: return cfsegment({}, {}, {});
    }
     println("cflow returns no value");
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
    println("cflowstat returns no value");
}

public bool test(){


assertTrue(
       cflow([asgStat("x", natCon(1)),  asgStat("s", conc(id("s"), strCon("#")))] ) ==
       cfsegment({stat(asgStat("x",natCon(1)))},
                 {<stat(asgStat("x",natCon(1))),stat(asgStat("s",conc(id("s"),strCon("#"))))>},
                 {stat(asgStat("s",conc(id("s"),strCon("#"))))})
       );

	
assertTrue(
    cflow(small) ==
	cfsegment({stat(asgStat("x",natCon(3)))},
	          {<stat(asgStat("x",sub(id("x"),natCon(1)))),stat(asgStat("s",conc(id("s"),strCon("#"))))>,
	           <stat(asgStat("x",natCon(3))),exp(id("x"))>,
	           <exp(id("x")),stat(asgStat("x",sub(id("x"),natCon(1))))>,
	           <stat(asgStat("s",conc(id("s"),strCon("#")))),exp(id("x"))>},
	           {exp(id("x"))})
	       );
	          
assertTrue(
    cflow(fac) ==
	cfsegment({stat(asgStat("input",natCon(13)))},
	          {<stat(asgStat("rep",id("output"))),stat(asgStat("repnr",id("input")))>,
	           <stat(asgStat("output",natCon(1))),exp(sub(id("input"),natCon(1)))>,
	           <stat(asgStat("repnr",id("input"))),exp(sub(id("repnr"),natCon(1)))>,
	           <exp(sub(id("repnr"),natCon(1))),stat(asgStat("input",sub(id("input"),natCon(1))))>,
	           <exp(sub(id("repnr"),natCon(1))),stat(asgStat("output",add(id("output"),id("rep"))))>,
	           <stat(asgStat("output",add(id("output"),id("rep")))),stat(asgStat("repnr",sub(id("repnr"),natCon(1))))>,
	           <stat(asgStat("input",natCon(13))),stat(asgStat("output",natCon(1)))>,
	           <exp(sub(id("input"),natCon(1))),stat(asgStat("rep",id("output")))>,
	           <stat(asgStat("input",sub(id("input"),natCon(1)))),exp(sub(id("input"),natCon(1)))>,
	           <stat(asgStat("repnr",sub(id("repnr"),natCon(1)))),exp(sub(id("repnr"),natCon(1)))>},
	           {exp(sub(id("input"),natCon(1)))})
	        );

return report();
}

