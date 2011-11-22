@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module Reverse::Reverse

import Integer;
import Benchmark;
import List;
import IO;

public list[int] rev1 (list[int] L)
{
	if([int X, list[int] L1] := L)
		return rev1(L1) + X;
    else
        return L;
}

public list[int] rev2 (list[int] L)
{
	if([list[int] L1, int X] := L)
		return X + rev2(L1);
    else
        return L;
}

public list[int] rev3 (list[int] L)
{
	if([int X, list[int] L1, int Y] := L)
		return Y + rev3(L1) + X;
    else
        return L;
}


public test bool trev1() = rev1([9,8,7,6,5,4,3,2,1]) == [1,2,3,4,5,6,7,8,9];

public test bool trev2() = rev2([9,8,7,6,5,4,3,2,1]) == [1,2,3,4,5,6,7,8,9];

public test bool trev3() = rev3([9,8,7,6,5,4,3,2,1]) == [1,2,3,4,5,6,7,8,9];


public bool measure(){
	int size = 200;
	list[int] L = [];
	for(int i <- [0 .. size]){
		L = L + arbInt();
	}
	begin = realTime();
	rev1(L);
	end1 = realTime();
	rev2(L);
	end2 = realTime();
	rev3(L);
	end3 = realTime();
	reverse(L);
	end4 = realTime();
	
	used1 = end1 - begin;
	used2 = end2 - end1;
	used3 = end3 - end2;
	used4 = end4 - end3;
	println("rev[123], reverse: <used1>, <used2>, <used3>, <used4> (msec)");
	
	return true;

}
	
	
	
