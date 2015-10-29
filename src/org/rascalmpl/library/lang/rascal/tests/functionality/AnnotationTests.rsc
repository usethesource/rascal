 module lang::rascal::tests::functionality::AnnotationTests
  /*******************************************************************************
   * Copyright (c) 2009-2015 CWI
   * All rights reserved. This program and the accompanying materials
   * are made available under the terms of the Eclipse Public License v1.0
   * which accompanies this distribution, and is available at
   * http://www.eclipse.org/legal/epl-v10.html
   *
   * Contributors:
  
   *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
   *   * Paul Klint - Paul.Klint@cwi.nl - CWI
  *******************************************************************************/
 
data F = f() | f(int n) | g(int n) | deep(F f);
data F(int pos = 0);
data AN = an(int n);
  	
// boolkwparams
  		
test bool boolkwparams1() = f()[pos=1] != f();
test bool boolkwparams2() = f()[pos=1].pos == 1;
test bool boolkwparams3() = f()[pos=1][pos=2].pos == 2;
  		
test bool boolkwparams4() = f(5)[pos=1] != f(5);
test bool boolkwparams5() = f(5)[pos=1].pos == 1;
test bool boolkwparams6() = f(5)[pos=1][pos=2].pos == 2;
  		
test bool boolkwparams7() = deep(f(5)[pos=1]) != deep(f(5));
test bool boolkwparams8() = f(5)[pos=1] != f(5)[pos=2];	
  	
// kwparamsInSets
  
test bool kwparamsInSets1() = {f()[pos=1]} != {f()};
test bool kwparamsInSets2() = {f()[pos=1], g(2)[pos=2]} == {f()[pos=1], g(2,pos=2)};
test bool kwparamsInSets3() = {f()[pos=1], g(2)} == {f(pos=1), g(2)};		
test bool kwparamsInSets4() = {deep(f(5)[pos=1])} == {deep(f(5,pos=1))};
  	
test bool kwparamsInSets5() = {f()[pos=1]} + {g(2)[pos=2]} == {f(pos=1), g(2,pos=2)};
  		
test bool kwparamsInSets6() = {X = {f()[pos=1]} + {f()[pos=2]}; {f(pos=2),f(pos=1)} := X; };
