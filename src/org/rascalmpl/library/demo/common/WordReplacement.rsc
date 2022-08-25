@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
//START
// tag::module[]
module demo::common::WordReplacement

import String;

// capitalize: convert first letter of a word to uppercase

str capitalize(str word) // <1>
{
   if(/^<letter:[a-z]><rest:.*$>/ := word){
     return toUpperCase(letter) + rest;
   } else {
     return word;
   }
}

test bool capitalize1() = capitalize("1") == "1";
test bool capitalize2() = capitalize("rascal") == "Rascal";

// Capitalize all words in a string

// Version 1: capAll1: using a while loop

str capAll1(str S) // <2>
{
 result = "";
 while (/^<before:\W*><word:\w+><after:.*$>/ := S) { 
    result = result + before + capitalize(word);
    S = after;
  }
  return result;
}

test bool tstCapAll1() =  capAll1("turn this into a title") == "Turn This Into A Title";

// Version 2: capAll2: using visit

str capAll2(str S) // <3>
{
   return visit(S){
   	case /^<word:\w+>/i => capitalize(word)
   };
}
// end::module[]

test bool tstCapAll2() = capAll2("turn this into a title") == "Turn This Into A Title";
