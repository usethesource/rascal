@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@synopsis{demonstrating factorial in Rascal}
module demo::basic::Factorial

@synopsis{fac1 demonstrates the ternary conditional and recursion}
int fac1(int n) = n <= 0 ? 1 : n * fac1(n - 1); //<1>

@synopsis{fac2 demonstrates overloading and dynamic dispatch with pattern matching}
int fac2(0) = 1;  //<2>
default int fac2(int n) = n * fac2(n - 1); //<3>

@synopsis{fac3 demonstrates structured programming and recursion}
int fac3(int n)  { //<4>
  if (n == 0) 
     return 1;
  return n * fac3(n - 1);
}

test bool tfac0()  = fac1(0)  == 1;
test bool tfac1()  = fac2(1)  == 1;
test bool tfac2()  = fac3(4)  == 24;
test bool tfac47() = fac3(47) == 258623241511168180642964355153611979969197632389120000000000;
