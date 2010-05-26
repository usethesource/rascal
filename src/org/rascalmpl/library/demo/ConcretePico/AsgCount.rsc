module demo::ConcretePico::AsgCount

import languages::pico::syntax::Pico;  // Pico concrete syntax
import demo::ConcretePico::Programs;   // Example programs
import ParseTree;

import IO;
import Benchmark;

public int countAsgVis(PROGRAM P) {
   int n = 0;
   visit(P){
   case `<\PICO-ID Id> := <EXP Exp>`: n += 1;
   
   }
   return n;
}

public int countAsgRec(PROGRAM P) {
    if( `begin declare <{\ID-TYPE "," }* Decls>; <{STATEMENT ";"}* Stats> end` := P)
   		return countStats(Stats);
   return 0;
}

public int countStats({STATEMENT ";"}* Stats){
   int n = 0;
   for(STATEMENT S <- Stats)
     n += countStat(S);
   return n;
}

public int countStat(STATEMENT S){
    switch (S) {
      case `<\PICO-ID Id> := <EXP Exp>`:
         return 1;

      case `if <EXP Exp> then <{STATEMENT ";"}* Stats1> 
                           else <{STATEMENT ";"}* Stats2>
            fi`:
         return countStats(Stats1) + countStats(Stats2); 

      case `while <EXP Exp> do <{STATEMENT ";"}* Stats> od`:
         return countStats(Stats);
    }
    return 0;
}

public PROGRAM makeProgram(int n){
   asgStats = "";
   for (i <- [1 .. n-1])
   	asgStats += " x := <i>;";

   return parse(#PROGRAM, "begin declare x : natural; <asgStats> x := <n> end");
}

public real cmeasure(int n, int (PROGRAM) cnt){
		prog = makeProgram(n);
		s = currentTimeMillis();
		result = cnt(prog);
		assert result == n;
		return currentTimeMillis() - s;
}

public void reports(){
    for(int n <- [10,100,500,1000,1500,2000]){
      m2 = cmeasure(n, countAsgRec);
      m3 = cmeasure(n, countAsgVis);
      println("<n>:\t<m2>, \t<m3>");
    }
}

test countAsg(small) == 3;
test countAsg(makeProgram(10)) == 10;