module ListMatchingTests

// import List;

public bool hasOrderedElement(list[int] L)
{
   switch(L){
   
   case [list[int] L1, int I, list[int] L2, int J, list[int] L3]:
        if(I > J){
        	return true;
        } else {
        	fail;
        }
   }
   return false;
}


public boolean hasDuplicateElement(list[int] L)
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

public boolean isDuo1(list[int] L)
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

public boolean isDuo2(list[int] L)
{
	switch(L){
	case [list[int] L1, L1]:
			return true;
	default:
		return false;
    }
}

public boolean isDuo3(list[int] L)
{
    return [list[int] L1, L1] ~= L;
}

public boolean isTrio1(list[int] L)
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

public boolean isTrio2(list[int] L)
{
	switch(L){
	case [list[int] L1, L1, L1]:
		return true;
	default:
		return false;
    }
}

public boolean isTrio3(list[int] L)
{
    return [list[int] L1, L1, L1] ~= L;
}

public boolean isNestedDuo(list[int] L)
{
    return [[list[int] L1, L1], [L1, L1]] ~= L;
}

/*
public boolean palindrome(list[int] L)
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