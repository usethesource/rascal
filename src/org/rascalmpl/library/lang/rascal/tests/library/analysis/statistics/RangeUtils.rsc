module lang::rascal::tests::library::analysis::statistics::RangeUtils

import util::Math;


list[&T <: num] abs(list[&T <: num] nums) 
	= [abs(n) | n <- nums]; 

(&T<:num) assureRange(&T <: num n, num low, num high) {
	ab = abs(n);
	if (ab >= low && ab <= high) {
		return n;	
	}
	if (ab <= high) {
		if (n < 0) {
			return n - low;
		}
		return n + low;	
	}
	return makeSmallerThan(n, toInt(high));
}


list[&T <: num] assureRange(list[&T <: num] nums, num low, num high)
	= [ assureRange(n, low, high) | n <- nums];



int makeSmallerThanInt(int n, int limit) = n % limit;
real makeSmallerThanReal(real n, int limit) {
	if (abs(n) < limit) {
		return n;
	}
	f = toInt(n);
	r = n - f;
	return (f % limit) + r;
}
rat makeSmallerThanRat(rat n, int limit) {
	if (abs(n) < limit) {
		return n;
	}
	return toRat(1, denominator(n));
}

&T <: num makeSmallerThan(&T <: num n, int limit) {
	if (int i := n) {
	    &T <: num x = i;
		return makeSmallerThanInt(x, limit);	
	}
	if (real r := n) {
	    &T <: num x = r;
		return makeSmallerThanReal(x, limit);	
	}
	if (rat r := n) {
	    &T <: num x = r;
		return makeSmallerThanRat(x, limit);	
	}
	throw "Forgot about a different number type <n>";
}

list[&T <: num] makeSmallerThan(list[&T <: num] nums, int limit) 
	= [ makeSmallerThan(n, limit) | n <- nums];
