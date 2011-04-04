@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module experiments::DDA::typecheck

import experiments::DDA::DDA;
import IO;
//alias Type = str;


public void typeCheck(Program p) {
  map[Identifier,Type] syms = ();

  for(Stat s <- p) {
    println("s = <s>");
    switch(s) {
      case Stat[|type <Identifier t>|]:
	syms = addSym(syms, t, "type");
      case Stat[|var <Identifier v> : <Identifier t>|]:
        syms = addSym(syms, v, checkSym(syms, t, "type"));
      case Stat[|compute <Identifier p>:<Identifier P> 
                   along <Identifier DDA> 
                   from <Identifier V> 
                   in <Expr E> 
                 end|]: {
        checkSym(syms, p, checkSym(syms, P, "type"));
        checkSym(syms, DDA, "DDA");
        checkSym(syms, V, "array");
      }
    }
  }
}

map[Identifier,Type] addSym(map[Identifier,Type] symbols, Identifier name,
                   Type type) {
  symbols[name] = type;
  return symbols;
}

Identifier checkSym(map[Identifier,Type] symbols, Identifier name,
                    Type type) {
  t = symbols[name] ? "undefined";

  if(t != type)
    println("Expected <name> to be <type>, but was <t>.");

  return name;
}

public Program parseProgram(str filename) {
  return parse(#Program, |cwd:///<filename>|);
}

public bool testExample(){
   P = parseProgram("src/experiments/DDA/example.dda");
   println(P);
   typeCheck(P);
   return true;
}

test testExample();
