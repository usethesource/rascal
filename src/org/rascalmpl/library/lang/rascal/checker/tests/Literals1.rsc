@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::rascal::checker::tests::Literals1

public int testfun1() {
    int x = 1;
    str y = "hello";
    bool b = true;
    real r = 1.513;
    loc l = |file:///dev/null|;
    datetime dt = $2010-12-01$;
    
    return x;
}

public list[int] testfun2() {
    list[int] l1 = [1,2,3,4,5];
    list[int] l2 = [1..5];
    list[int] l3 = [1,2..5];
    
    return l1;
}
