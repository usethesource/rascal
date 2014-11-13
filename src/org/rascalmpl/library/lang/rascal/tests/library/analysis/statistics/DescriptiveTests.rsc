module lang::rascal::tests::library::analysis::statistics::DescriptiveTests

import List;
import util::Math;
import analysis::statistics::Descriptive;
import lang::rascal::tests::library::analysis::statistics::RangeUtils;

test bool geometricLessThanArithmeticMean(list[num] nums) {
	if (nums == []) return true;
	nums = abs(nums);
	nums = assureRange(nums, 0.1,30);
	return geometricMean(nums) <= mean(nums);
}