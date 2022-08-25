@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
//START
module demo::basic::BubbleTest

import demo::basic::Bubble;

// Tests

public list[int] unsorted = [10,9,8,7,6,5,4,3,2,1];
public list[int] sorted = [1,2,3,4,5,6,7,8,9,10];   
public test bool t1() = sort1(unsorted) == sorted;
public test bool t2() = sort2(unsorted) == sorted;
public test bool t3() = sort3(unsorted) == sorted;
 
