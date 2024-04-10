@synopsis{Conversions and assertions on numerical ranges, for testing and sampling purposes.}
module lang::rascal::tests::library::analysis::statistics::RangeUtils

import util::Math;

@synopsis{list of absolute numbers for every list element.}
list[&T <: num] abs(list[&T <: num] nums) = [abs(n) | n <- nums]; 

@synopsis{Returns `n` if low <= n <= high, or any other number that is guaranteed between the `low` and `high` bounds.}
@description{
This function is used to _map_ randomly generated numbers _into_ a range. The goal
is to achieve a more-or-less **uniform distribution** inside of the range `[low, high]`, given a more
ore less uniformly distributed value for `n` over any other unknown range `[x,y]`. This works
best if `y - x >= high - low`, otherwise parts of the target range may be unreachable. 
}
&T <: num assureRange(&T <: num n, &T <: num low, &T <: num high) {
	assert low < high;

	target = n;
	window = high - low;

	// jump above the lower bound into the range with steps sized `window`
	if (target < low) {
		target += (floor(low / window) - floor(target / window)) * window;
	}

	// or jump below the high bound into the range with steps sized `window`	
	if (high < target) {
		target -= (ceil(target / window) - ceil(high / window)) * window;
	}

	assert low <= target && target <= high;

	return target;
}

test bool assureRangeTest(num x) = 0 <= target && target <= 10 when target := assureRange(x, 0, 10);

@synopsis{Change a list of numbers into a list of numbers that all fit into a range.}
@description{
The goal of this function is to make sure an otherwise randomly generated list of numbers
is limited to a given range, between `low` and `high` inclusive bounds.
The target numbers are the same if they already fit, and we try to keep a uniform distribution
within the range as much as possible.
}
list[&T <: num] assureRange(list[&T <: num] nums, num low, num high)
	= [assureRange(n, low, high) | n <- nums];
