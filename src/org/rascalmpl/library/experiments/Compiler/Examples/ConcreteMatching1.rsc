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
  
  if((A) `<A a>` := [A] "a")  											res += "[01] <a> "; else "[01] FAIL ";
  
  if((As) `<A+ as>` := [As] "a") 										res += "[11] <as> "; else res += "[11] FAIL ";
  if((As) `<A+ as>` := [As] "aa") 										res += "[12] <as> "; else res += "[12] FAIL ";
  if((As) `<A+ as>` := [As] "aaa") 										res += "[13] <as> "; else res += "[13] FAIL ";

  if((As) `a<A+ as>` := [As] "aa") 										res += "[14] <as> "; else res += "[14] FAIL ";
  if((As) `aa<A+ as>` := [As] "aaa") 									res += "[15] <as> "; else res += "[15] FAIL ";
 
  if((As) `<A+ as>a` := [As] "aa") 										res += "[16] <as> "; else res += "[16] FAIL ";
  if((As) `<A+ as>aa` := [As] "aaa") 									res += "[17] <as> "; else res += "[17] FAIL ";
  
  if((As) `a<A+ as>a` := [As] "aaa") 									res += "[18] <as> "; else res += "[18] FAIL ";
  if((As) `a<A+ as>a` := [As] "aaaa") 									res += "[19] <as> "; else res += "[19] FAIL ";

  if((As) `<A+ as>` := [As] "a") 										res += "[20] <as> "; else res += "[20] FAIL ";
  if((As) `<A+ as>` := [As] "aa") 										res += "[21] <as> "; else res += "[21] FAIL ";
  
  if((As) `<A+ as1>a<A+ as2>a` := [As] "aaaa") 							res += "[22] <as1>;<as2> "; else res += "[22] FAIL ";
  if((As) `<A+ as1>a<A+ as2>a` := [As] "aaaaa") 						res += "[23] <as1>;<as2> "; else res += "[23] FAIL ";
  
  if((Bs) `<B* bs>` := [Bs] "") 										res += "[31] <bs> "; else res += "[31] FAIL ";
  if((Bs) `<B* bs>` := [Bs] "b") 										res += "[32] <bs> "; else res += "[32] FAIL ";
   if((Bs) `<B* bs>` := [Bs] "bb") 										res += "[33] <bs> "; else res += "[33] FAIL ";
   
  if((Bs) `b<B* bs>` := [Bs] "b") 										res += "[34] <bs> "; else res += "[34] FAIL "; 
  if((Bs) `b<B* bs>` := [Bs] "bb") 										res += "[35] <bs> "; else res += "[35] FAIL "; 
   
  if((Bs) `<B* bs>b` := [Bs] "bbbb") 									res += "[36] <bs> "; else res += "[36] FAIL ";
  
  if((Bs) `<B* bs>bb` := [Bs] "bbbb") 									res += "[37] <bs> "; else res += "[37] FAIL ";
  if((Bs) `<B* bs>bbb` := [Bs] "bbbb") 									res += "[38] <bs> "; else res += "[38] FAIL ";
  if((Bs) `<B* bs>b` := [Bs] "b") 										res += "[39] <bs> "; else res += "[39] FAIL ";
  
  if((Bs) `<B* bs1><B* bs2>` := [Bs] "") 								res += "[40] <bs1>;<bs2> "; else res += "[40] FAIL ";
  if((Bs) `b<B* bs1>b<B* bs2>b` := [Bs] "bbbb") 						res += "[41] <bs1>;<bs2> "; else res += "[41] FAIL ";
  if((Bs) `b<B* bs1>b<B* bs2>b` := [Bs] "bbbbb") 						res += "[42] <bs1>;<bs2> "; else res += "[42] FAIL ";
  if((Bs) `b<B* bs1>b<B* bs2>b` := [Bs] "bbb") 							res += "[43] <bs1>;<bs2> "; else res += "[43] FAIL ";
  
   
  if((Cs) `<{C ","}+ cs>` := [Cs] "c,c,c,c") 							res += "[51] <cs> "; else res += "[51] FAIL ";
  if((Cs) `c,<{C ","}+ cs>` := [Cs] "c,c,c,c") 							res += "[52] <cs> "; else res += "[52] FAIL ";
  if((Cs) `c,c,<{C ","}+ cs>` := [Cs] "c,c,c,c") 						res += "[53] <cs> "; else res += "[53] FAIL ";
  
  if((Cs) `<{C ","}+ cs>,c` := [Cs] "c,c,c,c") 							res += "[54] <cs> "; else res += "[54] FAIL ";
  if((Cs) `<{C ","}+ cs>,c,c` := [Cs] "c,c,c,c") 						res += "[55] <cs> "; else res += "[55] FAIL ";
  if((Cs) `<{C ","}+ cs>,c,c,c` := [Cs] "c,c,c,c") 						res += "[56] <cs> "; else res += "[56] FAIL ";
  
  if((Cs) `c,<{C ","}+ cs>,c` := [Cs] "c,c,c") 							res += "[57] <cs> "; else res += "[57] FAIL ";
  if((Cs) `c,<{C ","}+ cs>,c` := [Cs] "c,c,c,c") 						res += "[58] <cs> "; else res += "[58] FAIL ";
  
  if((Cs) `<{C ","}+ cs1>,<{C ","}+ cs2>` := [Cs] "c,c") 				res += "[59] <cs1>/<cs2> "; else res += "[59] FAIL ";
  if((Cs) `c,<{C ","}+ cs1>,<{C ","}+ cs2>` := [Cs] "c,c,c") 			res += "[60] <cs1>/<cs2> "; else res += "[60] FAIL ";
  if((Cs) `<{C ","}+ cs1>,c,<{C ","}+ cs2>` := [Cs] "c,c,c,c,c") 		res += "[61] <cs1>/<cs2> "; else res += "[61] FAIL ";
  if((Cs) `<{C ","}+ cs1>,<{C ","}+ cs2>,c` := [Cs] "c,c,c,c,c") 		res += "[62] <cs1>/<cs2> "; else res += "[62] FAIL ";
  if((Cs) `c,<{C ","}+ cs1>,c,<{C ","}+ cs2>,c` := [Cs] "c,c,c,c,c") 	res += "[63] <cs1>/<cs2> "; else res += "[63] FAIL ";
  
  if((Ds) `<{D ","}* ds>` := [Ds] "") 									res += "[71] <ds> "; else res += "[71] FAIL ";
  if((Ds) `<{D ","}* ds>` := [Ds] "d,d,d,d") 							res += "[72] <ds> "; else res += "[72] FAIL ";
  if((Ds) `<{D ","}* ds>,d` := [Ds] "d,d,d,d") 							res += "[73] <ds> "; else res += "[73] FAIL ";
  if((Ds) `<{D ","}* ds>,d,d` := [Ds] "d,d,d,d") 						res += "[74] <ds> "; else res += "[74] FAIL ";
  if((Ds) `d,<{D ","}* ds>,d` := [Ds] "d,d,d,d") 						res += "[75] <ds> "; else res += "[75] FAIL ";
  if((Ds) `d,d,<{D ","}* ds>` := [Ds] "d,d,d,d") 						res += "[76] <ds> "; else res += "[76] FAIL ";
  if((Ds) `d,d,<{D ","}* ds>,d` := [Ds] "d,d,d,d") 						res += "[77] <ds> "; else res += "[77] FAIL ";
  
  if((Ds) `<{D ","}* ds1>,<{D ","}* ds2>` := [Ds] "") 					res += "[80] <ds1>/<ds2> "; else res += "[80] FAIL ";
  
  if((Ds) `d,<{D ","}* ds1>,<{D ","}* ds2>` := [Ds] "d") 				res += "[81] <ds1>/<ds2> "; else res += "[81] FAIL ";

  if((Ds) `<{D ","}* ds1>,d,<{D ","}* ds2>` := [Ds] "d") 				res += "[82] <ds1>/<ds2> "; else res += "[82] FAIL ";
 
  if((Ds) `<{D ","}* ds1>,<{D ","}* ds2>,d` := [Ds] "d") 				res += "[83] <ds1>/<ds2> "; else res += "[83] FAIL ";
  if((Ds) `<{D ","}* ds1>,d,d,<{D ","}* ds2>,d` := [Ds] "d,d,d,d,d") 	res += "[84] <ds1>/<ds2> "; else res += "[84] FAIL ";
  if((Ds) `<{D ","}* ds1>,d,d,d,<{D ","}* ds2>` := [Ds] "d,d,d,d,d") 	res += "[85] <ds1>/<ds2> "; else res += "[85] FAIL ";

  return res;
}