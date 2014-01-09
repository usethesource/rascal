@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module experiments::Compiler::Benchmarks::fca::CXTIO

import Exception;
import IO;
import String;
import List;
import Set;
import experiments::Compiler::Benchmarks::fca::FCA;

@doc{Read object attribute in .cxt format.}   
public FormalContext[str, str] readCxt(loc input)  {
    list[str] d = readFileLines(input);
    map[str, set[str]] vb1 = ();
    int nRows = toInt(d[2]);
    int nCols = toInt(d[3]);
    int theStart = 5+nRows+nCols;
    list[str] e = tail(d, size(d)-theStart);
    int idx = 5;
    map [str, set[str]] vb = ();
    for (str f <- e) {
         set[str] b = {d[5+nRows+i]|int i<-[0, 1..size(f)], charAt(f,i)==88};
         vb[d[idx]] = b;
         idx = idx+1;
         }
    return toFormalContext(vb);
    }
   