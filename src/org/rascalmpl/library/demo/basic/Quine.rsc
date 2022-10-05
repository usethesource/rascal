@license{
  Copyright (c) 2009-2022 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@synopsis{A Quine is a program that prints itself}
module demo::basic::Quine

import IO;
import String;

void quine(){
  println(program); // <3>
  println("\"" + escape(program, ("\"" : "\\\"", "\\" : "\\\\")) + "\";"); // <4>
}

str program = // <1>
"module demo::basic::Quine

import IO;
import String;

void quine(){
  println(program);
  println(\"\\\"\" + escape(program, (\"\\\"\" : \"\\\\\\\"\", \"\\\\\" : \"\\\\\\\\\")) + \"\\\";\");
}

str program ="; // <2>
