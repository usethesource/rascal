module lang::rascal::tests::library::analysis::statistics::DescriptiveTests

import List;
import util::Math;
import analysis::statistics::Descriptive;
import lang::rascal::tests::library::analysis::statistics::RangeUtils;

bool eq(num a, num b) {
	error = 1 / pow(10, min(scale(a), scale(b)) - 1);
	return abs(a-b) <= error;
}
bool leq(num a, num b) = a < b ? true : eq(a,b);

test bool geometricLessThanArithmeticMean(list[num] nums) {
	if (nums == []) return true;
	nums = abs(nums);
	nums = assureRange(nums, 0.1, 30); 
	return leq(geometricMean(nums), mean(nums));
}
test bool meanTimesSizeEqualsSum(list[num] nums) {
	if (nums == []) return true;
	return eq(mean(nums) * size(nums), sum(nums));
}

test bool percentileRelation(list[num] nums, int a, int b) {
	if (nums == []) {
	  return true;
	}
	
	a = abs(a) % 100;
	b = abs(b) % 100;
	if (a > b) {
		t = a;
		a = b;
		b = t;	
	}
	return leq(percentile(nums, a), percentile(nums, b));
}

test bool varianceIsPositive(list[num] nums) {
	if (nums == []) return true;
	nums = assureRange(nums, 0.0001, 400);
	return variance(nums) >= 0;
}

test bool kurtoiseNeverBelowZero(list[num] nums) {
	if (nums == []) return true;
	nums = assureRange(nums, 0.0001, 400);
	return variance(nums) > 0 ==> kurtosis(nums) >= 0;
}

test bool standardDeviationIsPositive(list[num] nums) {
	if (nums == []) return true;
	nums = assureRange(nums, 0.0001, 400);
	return standardDeviation(nums) >= 0;
}
