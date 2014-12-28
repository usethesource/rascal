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
	return makeSmallerThan(n, high);
}


list[&T <: num] assureRange(list[&T <: num] nums, num low, num high)
	= [ assureRange(n, low, high) | n <- nums];



(&T<:int) makeSmallerThan(&T <: int n, int limit) = n % limit;
(&T<:real) makeSmallerThan(&T <: real n, int limit) {
	if (abs(n) < limit) {
		return n;
	}
	f = trunc(n);
	r = n - f;
	return (f % limit) + r;
}
(&T<:rat) makeSmallerThan(&T <: rat n, int limit) {
	if (abs(n) < limit) {
		return n;
	}
	return toRat(1, denominator(n));
}


list[num] makeSmallerThan(list[num] nums, int limit) 
	= [ makeSmallerThan(n, limit) | n <- nums];