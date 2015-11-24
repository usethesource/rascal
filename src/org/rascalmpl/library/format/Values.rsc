module \format::Values

import util::Math;
extend \format::Box;

format (real r, int to = 0.001) 
  = "~~(round(r, to))";

format (list[value] r) 
  = HV("[", HV(r,sep=","),"]"); 
  
format (set[value] r) 
  = HV("[", HV(r,sep=","),"]"); 