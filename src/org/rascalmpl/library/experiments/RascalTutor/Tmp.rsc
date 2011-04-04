@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module experiments::RascalTutor::Tmp
import IO;

public str check(int i){
  throw "no";
}

public list[int] f(){
  println("Enter f");
  res = [];
  for(int i <- [0 .. 10]){
     try {
         println("Inside f, entering try");
         res += check(i);
     } catch: {
        println("Inside f, catch");
        res += -1;
     }
  }   
      
  return res;
}
