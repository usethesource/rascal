@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module demo::WordReplacement

import String;

// capitalize: convert first letter to uppercase

public str capitalize(str word)
{
   if(/^<letter:[a-z]><rest:.*$>/ := word){
     return toUpperCase(letter) + rest;
   } else {
     return word;
   }
}

// Capitalize all words in a string

// capAll1: using a while loop

public str capAll1(str S)
{
 result = "";
 while (/^<before:\W*><word:\w+><after:.*$>/ := S) { 
    result = result + before + capitalize(word);
    S = after;
  }
  return result;
}

// capAll2: using visit

public str capAll2(str S)
{
   return visit(S){
   	case /<word:\w+>/i => capitalize(word)
   };
}

public test bool t1() =  capitalize("1") == "1";
public test bool t2() =  capitalize("rascal") == "Rascal";
public test bool t3() =  capAll1("turn this into a title") == "Turn This Into A Title";
public test bool t4() =  capAll2("turn this into a title") == "Turn This Into A Title";
