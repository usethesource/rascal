@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module RecoveryTests

@should{return 0}
public int recoveryOfLocalVariable()
{
	x = 0;
	l = [1, 2, 3];
	visit (l) {
     case int n: {
     	x = x + 1; 
     	fail;
     } 	
	};
    return x;
}

@should{return 3}
public int nestedRecoveryOfLocalVariable() 
{
	x = 0;
	l = [1, 2, 3];
	visit (l) {
     case int n: {
     	x = x + 1;
     	visit (l) {
     	   case int m: {
     	      x = x + 1;
     	      fail;
     	   }
     	}; 
      } 	
	};
    return x;
}

@should{return 12} // but why?
public int noNestedRecovery()
{
	int x = 0;
	l = [1, 2, 3];
	visit (l) {
     case int n: {
     	x = x + 1;
     	visit (l) {
     	   case int i: {
     	      x = x + 1;
     	   }
     	}; 
      } 	
	};
    return x;
}

@should{return 0}
public int recoveryOfLocalVariableUsingIfThen()
{
	x = 0;
	l = [1, 2, 3];
	visit (l) {
     case int n: {
     	x = x + 1; 
     	if (n > 10) {
     	   x = x + 1; // another update
     	} else
     		fail;
      } 	
	};
    return x;
}

public int gx = 0;

@should{return 0}
public int recoveryOfGlobalVariable() 
{
    //global int gx;
	l = [1, 2, 3];
	visit (l) {
     case int n: {
     	gx = gx + 1; 
     	fail;
     } 	
	};
    return gx;
}

public int gt = 0;

data City = amsterdam();

a1 amsterdam()  { 
  gt = gt + 1;
  fail;
}

public int recoveryOfGlobalAfterFailingRule() {
	x = amsterdam();
	return gt;
}

public bool meddle() {
  gt = 123;
  return true;
}

public int recoveryOfGlobalDuringComprehension() {
	aset = {1, 2, 3};
	another = { x | int x <- aset, meddle() };
	return gt;
}




