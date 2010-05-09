module demo::AbstractPico::AsgCount

import demo::AbstractPico::AbstractSyntax;
import IO;
import demo::AbstractPico::Programs;
import Benchmark;

public int countAsg(PROGRAM P) {
   int n = 0;
   visit(P){
     case asgStat(PicoId Id, EXP Exp): n += 1;
   }
   return n;
}

PROGRAM makeProgram(int n){
   asgStats = [asgStat("x", natCon(i)) | i <- [1 .. n]];

   return program([decl("x", natural())], asgStats);
}

public real ameasure(int n){
		start = currentTimeMillis();
		result = countAsg(makeProgram(n));
		assert result == n;
		return currentTimeMillis() - start;
}

public void reports(){
    for(int n <- [10,100,500,1000,1500,2000]){
      m1 = ameasure(n);
      println("<n>: <m1>");
    }
}


test countAsg(small) == 3;
test countAsg(makeProgram(10)) == 10;
test countAsg(makeProgram(10000)) == 10000;