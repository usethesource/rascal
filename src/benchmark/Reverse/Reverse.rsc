module Reverse

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

public bool test(){
	return 
		rev1([9,8,7,6,5,4,3,2,1]) == [1,2,3,4,5,6,7,8,9] &&
		rev2([9,8,7,6,5,4,3,2,1]) == [1,2,3,4,5,6,7,8,9] &&
		rev3([9,8,7,6,5,4,3,2,1]) == [1,2,3,4,5,6,7,8,9];
}

public bool measure(){
	int size = 200;
	list[int] L = [];
	for(int i : [0 .. size]){
		L = L + arbInt();
	}
	start = currentTimeMillis();
	rev1(L);
	end1 = currentTimeMillis();
	rev2(L);
	end2 = currentTimeMillis();
	rev3(L);
	end3 = currentTimeMillis();
	reverse(L);
	end4 = currentTimeMillis();
	
	used1 = end1 - start;
	used2 = end2 - end1;
	used3 = end3 - end2;
	used4 = end4 - end3;
	println("rev[123], reverse: <used1>, <used2>, <used3>, <used4> (msec)");
	
	return true;

}
	
	
	