@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module Test2
import IO;
import zoo::pico::syntax::Main;
import ParseTree;

public void test(){
  if(`declare <{\ID-TYPE "," }* decls>;` := `declare x: natural, y : string;`){
     
       println("decls: <decls>");
      
       L = [Id | ` <\PICO-ID Id> : <TYPE Type> ` <- decls];
       
      println("L = <L>");
  }
}
