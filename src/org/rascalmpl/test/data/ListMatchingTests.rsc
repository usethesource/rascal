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
module ListMatchingTests

// import List;
import IO;

public bool hasOrderedElement(list[int] L)
{
   switch(L){
   
   case [list[int] L1, int I, list[int] L2, int J, list[int] L3]: {
        println("I: <I> J: <J>");
        if(I > J){
        println("ordered");
        	return true;
        } else {
        println("not-ordered");
        	fail;
        }
        }
   }
   return false;
}


public bool hasDuplicateElement(list[int] L)
{
	switch(L){
	
	case [list[int] L1, int I, list[int] L2, int J, list[int] L3]:
		if(I == J){
			return true;
		} else {
			fail;
		}
	default:
		return false;
    }
}

public bool isDuo1(list[int] L)
{
	switch(L){
	case [list[int] L1, list[int] L2]:
		if(L1 == L2){
			return true;
		} else {
			fail;
		}
	default:
		return false;
    }
}

public bool isDuo2(list[int] L)
{
	switch(L){
	case [list[int] L1, L1]:
			return true;
	default:
		return false;
    }
}

public bool isDuo3(list[int] L)
{
    return [list[int] L1, L1] := L;
}

public bool isTrio1(list[int] L)
{
	switch(L){
	case [list[int] L1, list[int] L2, list[int] L3]:
		if((L1 == L2) && (L2 == L3)){
			return true;
		} else {
			fail;
		}
	default:
		return false;
    }
}

public bool isTrio2(list[int] L)
{
	switch(L){
	case [list[int] L1, L1, L1]:
		return true;
	default:
		return false;
    }
}

public bool isTrio3(list[int] L)
{
    return [list[int] L1, L1, L1] := L;
}

public bool isNestedDuo(list[int] L)
{
    return [[list[int] L1, L1], [L1, L1]] := L;
}

/*
public bool palindrome(list[int] L)
{
	switch(L){
	
	case [list[int] L1, list[int] L2, list[int] L3]:
		if(L1 == reverse(L3) && size(L2) <= 1){
			return true;
		} else {
			fail;
		}
	default:
		return false;
    }
}
*/
