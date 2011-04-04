@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
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
