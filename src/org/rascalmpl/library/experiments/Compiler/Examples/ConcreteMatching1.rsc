module experiments::Compiler::Examples::ConcreteMatching1

import ParseTree;
syntax A = "a";
syntax As = A+;

syntax B = "b";
syntax Bs = B*;

syntax C = "c";
syntax Cs = {C ","}+;

syntax D = "d";
syntax Ds = {D ","}*;

value main(list[value] args) {
  res = "";

  if((As) `<A+ as>` := [As] "aaaa") 									res +=  "[11] <as>\n";
  if((As) `<A+ as>a` := [As] "aaaa") 									res +=  "[12] <as>\n";
  if((As) `<A+ as>aa` := [As] "aaaa") 									res +=  "[13] <as>\n";
  if((As) `<A+ as>aaa` := [As] "aaaa") 									res +=  "[14] <as>\n";
  
  if((As) `<A+ as1>a<A+ as2>a` := [As] "aaaa") 							res +=  "[15] <as1>;<as2>\n";
  if((As) `<A+ as1>a<A+ as2>a` := [As] "aaaaa") 						res +=  "[16] <as1>;<as2>\n";
  
  if((Bs) `<B* bs>` := [Bs] "") 										res +=  "\n[21] <bs>\n";
  if((Bs) `<B* bs>` := [Bs] "bbbb") 									res +=  "[22] <bs>\n";
  if((Bs) `<B* bs>b` := [Bs] "bbbb") 									res +=  "[23] <bs>\n";
  if((Bs) `<B* bs>bb` := [Bs] "bbbb") 									res +=  "[24] <bs>\n";
  if((Bs) `<B* bs>bbb` := [Bs] "bbbb") 									res +=  "[25] <bs>\n";
  if((Bs) `<B* bs>bbbb` := [Bs] "bbbb") 								res +=  "[26] <bs>\n";
  
  if((Bs) `<B* bs1>b<B* bs2>b` := [Bs] "bbbb") 							res +=  "[27] <bs1>;<bs2>\n";
  if((Bs) `<B* bs1>b<B* bs2>b` := [Bs] "bbbbb") 						res +=  "[28] <bs1>;<bs2>\n";
  if((Bs) `<B* bs1>b<B* bs2>b` := [Bs] "bb") 							res +=  "[29] <bs1>;<bs2>\n";
  
   
  if((Cs) `<{C ","}+ cs>` := [Cs] "c,c,c,c") 							res +=  "[31] <cs>\n";
  if((Cs) `<{C ","}+ cs>,c` := [Cs] "c,c,c,c") 							res +=  "[32] <cs>\n";
  if((Cs) `<{C ","}+ cs>,c,c` := [Cs] "c,c,c,c") 						res +=  "[33] <cs>\n";
  if((Cs) `<{C ","}+ cs>,c,c,c` := [Cs] "c,c,c,c") 						res +=  "[34] <cs>\n";
  
  if((Cs) `<{C ","}+ cs1>,c,<{C ","}+ cs2>,c` := [Cs] "c,c,c,c,c") 		res +=  "[35] <cs1>;<cs2>\n";
  if((Cs) `<{C ","}+ cs1>,c,c,<{C ","}+ cs2>,c` := [Cs] "c,c,c,c,c") 	res +=  "[36] <cs1>;<cs2>\n";
  if((Cs) `<{C ","}+ cs1>,c,c,c,<{C ","}+ cs2>` := [Cs] "c,c,c,c,c") 	res +=  "[37] <cs1>;<cs2>\n";
  
  
  if((Ds) `<{D ","}* ds>` := [Ds] "") 									res +=  "[41] <ds>\n";
  if((Ds) `<{D ","}* ds>` := [Ds] "d,d,d,d") 							res +=  "[42] <ds>\n";
  if((Ds) `<{D ","}* ds>,d` := [Ds] "d,d,d,d") 							res +=  "[43] <ds>\n";
  if((Ds) `<{D ","}* ds>,d,d` := [Ds] "d,d,d,d") 						res +=  "[44] <ds>\n";
  if((Ds) `<{D ","}* ds>,d,d,d` := [Ds] "d,d,d,d") 						res +=  "[45] <ds>\n";
  
  if((Ds) `<{D ","}* ds1>,<{D ","}* ds2>` := [Ds] "") 					res +=  "[46] <ds1>;<ds2>\n";
  if((Ds) `<{D ","}* ds1>,d,<{D ","}* ds2>` := [Ds] "d") 				res +=  "[47] <ds1>;<ds2>\n";
  if((Ds) `<{D ","}* ds1>,<{D ","}* ds2>,d` := [Ds] "d") 				res +=  "[48] <ds1>;<ds2>\n";
  if((Ds) `<{D ","}* ds1>,d,d,<{D ","}* ds2>,d` := [Ds] "d,d,d,d,d") 	res +=  "[49] <ds1>;<ds2>\n";
  if((Ds) `<{D ","}* ds1>,d,d,d,<{D ","}* ds2>` := [Ds] "d,d,d,d,d") 	res +=  "[50] <ds1>;<ds2>\n";

  return res;
}