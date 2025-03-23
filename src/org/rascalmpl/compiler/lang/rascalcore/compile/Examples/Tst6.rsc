@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::compile::Examples::Tst6
 
 //import Grammar;
 //import lang::rascal::grammar::definition::Literals;
 import IO;
 import ParseTree;
 import String;

 data Grammar 
  = \grammar(set[Symbol] starts, map[Symbol sort, Production def] rules)
  ;


// public Grammar literals(Grammar g) {
//   return compose(g, grammar({}, {/*literal(s) | /lit(s) <- g*/}));
// }

// public Production literal(str s) = prod(lit(s),str2syms(s),{});

// public list[Symbol] str2syms(str x) {
//   if (x == "") return [];
//   return [\char-class([range(c,c)]) | i <- [0..size(x)], int c:= charAt(x,i)]; 
// }

public Grammar grammar(set[Symbol] starts, set[Production] prods) {
  map[Symbol, Production] rules = ();

  for (p <- prods) {
    t = (p.def is label) ? p.def.symbol : p.def;

    if (t in rules) {
        if (choice(_, existing) := rules[t]) {
            rules[t] = choice(t, existing + p);
        }
        else {
            rules[t] = choice(t, {p, rules[t]});
        }
    }
    else {
        rules[t] = choice(t, {p});
    }
  } 
  return grammar(starts, rules);
} 

public Grammar compose(Grammar g1, Grammar g2) {
  println("g1 == ...: <g1 == grammar({sort("S")},())>");
  for (s <- g2.rules)
    if (g1.rules[s]?)
      g1.rules[s] = choice(s, {g1.rules[s], g2.rules[s]});
    else
      g1.rules[s] = g2.rules[s];
  g1.starts += g2.starts;

  reduced_rules = ();
  for(s <- g1.rules){
  	  c = g1.rules[s];
  	  if (c is choice) {
  	    c.alternatives -= { *choices | priority(_, choices) <- c.alternatives } +
  		                  { *alts | associativity(_, _, alts) <- c.alternatives};
  	  }	                
  	  reduced_rules[s] = c;
  }
  println("g1 == ...: <g1 == grammar({sort("S")},())>");
  println("g1.starts: <g1.starts>, <g1.starts == {sort("S")}>");
  println("reduced_rules: <reduced_rules>, <reduced_rules == ()>");
  
  res = grammar(g1.starts, reduced_rules);
  println("res == g1: <res == g1>");
  return res;
}    
value main() {
    g = grammar({sort("S")}, ());
    g2 = grammar({sort("S")}, ());
    println("g == g2: <g == g2>");
    el = compose(g, grammar({}, {/*literal(s) | /lit(s) <- g*/}));
    println("el:"); iprintln(el);
    println("g"); iprintln(g);
    println("g == el: <g == el>");
  return 0;
}