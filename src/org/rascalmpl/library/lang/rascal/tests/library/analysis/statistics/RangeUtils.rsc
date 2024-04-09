@synopsis{Conversions and assertions on numerical ranges, for testing and sampling purposes.}
module lang::rascal::tests::library::analysis::statistics::RangeUtils

import util::Math;

@synopsis{list of absolute numbers for every list element.}
list[&T <: num] abs(list[&T <: num] nums) = [abs(n) | n <- nums]; 

@synopsis{Returns `n` if low <= n <= high, or any other number that is guaranteed between the `low` and `high` bounds.}
@description{
This function is used to coerce randomly generated numbers into a range. The goal
is to achieve a more-or-less uniform distribution inside of the range, given a more
ore less uniformly distributed value for `n`.
}
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

	// ab >= low and ab > high
	return makeSmallerThan(n, toInt(high));
}

@synopsis{Change a list of numbers into a list of numbers that all fit into a range.}
@description{
The goal of this function is to make sure an otherwise randomly generated list of numbers
is limited to a given range, between `low` and `high` inclusive bounds.
The target numbers are the same if they already fit, and we try to keep a uniform distribution
within the range as much as possible.
}
list[&T <: num] assureRange(list[&T <: num] nums, num low, num high)
	= [assureRange(n, low, high) | n <- nums];

int makeSmallerThanInt(int n, int limit) = n % limit;
real makeSmallerThanReal(real n, int limit) {
	if (abs(n) < limit) {
		return n; // if limit < 0 this is broken
	}
	f = toInt(n);
	r = n - f;
	return (f % limit) + r;
}
rat makeSmallerThanRat(rat n, int limit) {
	if (abs(n) < limit) {
		return n; // this is not ok if limit < 0
	}
	return toRat(1, denominator(n)); // this does not work if limit < 1
}

&T <: num makeSmallerThan(&T <: num n, int limit) {
	if (int i := n) {
	    &T <: num x = i; // this is an illegal assignment
		return makeSmallerThanInt(x, limit);	
	}
	if (real r := n) {
	    &T <: num x = r; // this is an illegal assignment
		return makeSmallerThanReal(x, limit);	
	}
	if (rat r := n) {
	    &T <: num x = r; // this is an illegal assignment
		return makeSmallerThanRat(x, limit);	
	}
	throw "Forgot about a different number type <n>";
}

list[&T <: num] makeSmallerThan(list[&T <: num] nums, int limit) 
	= [ makeSmallerThan(n, limit) | n <- nums];
