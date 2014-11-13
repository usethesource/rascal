@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module analysis::statistics::Descriptive

import IO;
import List;
import Exception;
import util::Math;

real geometricMean(list[num] l:[]) {
	throw IllegalArgument(l,"Geometric mean cannot be calculated for empty lists");
}
@doc{
Synopsis: Geometric mean of data values.

Description:

Computes the [geometric mean](http://en.wikipedia.org/wiki/Geometric_mean) of the given data values.
}
default real geometricMean([num hd, *num tl]) {
	if (tl == []) {
		return toReal(hd);	
	}
	prod = (hd | it * v | v <- tl);
	if (prod < 0) {
		throw ArithmeticException("Geometric mean can only be calculated for positive numbers");	
	}
	if (prod == 0) {
		return toReal(prod);
	}
	return nroot(prod, 1 + size(tl));
}

@doc{
Synopsis: Kurtosis of data values.

Description:

Computes the [kurtosis](http://en.wikipedia.org/wiki/Kurtosis) of the given data values.
Kurtosis is a measure of the "peakedness" of a distribution.
}
@javaClass{org.rascalmpl.library.analysis.statistics.Descriptive}
public java num kurtosis(list[num] values);

@doc{
Synopsis: Largest data value.
}
(&T <: num) max(list[&T <: num] nums) throws EmptyList
	= (head(nums) | it < n ? n : it | n <- tail(nums));


(&T <: num) mean(list[&T<:num] l:[]) {
	throw IllegalArgument(l,"Mean cannot be calculated for empty lists");
}

@doc{
Synopsis: Arithmetic mean of data values.

Description:

Computes the [arithmetic mean](http://en.wikipedia.org/wiki/Arithmetic_mean) of the data values.
}
real mean(list[int] nums)
	= toReal(sum(nums)) / size(nums);
real mean(list[real] nums)
	= sum(nums) / size(nums);
num mean(list[num] nums)
	= sum(nums) / size(nums);
rat mean(list[rat] nums)
	= sum(nums) / size(nums);


(&T <: num) median(list[&T<:num] l:[]) {
	throw IllegalArgument(l,"Median cannot be calculated for empty lists");
}

private list[&T <: num] middle(list[&T <: num] nums) {
	nums = sort(nums);
	n = size(nums);
	if (n % 2 == 1) {
		return [nums[n/2]];	
	}	
	n = n / 2;
	return nums[n-1..n+1];
}

@doc{
Synopsis: Median of data values.

Description:

Returns the [median](http://en.wikipedia.org/wiki/Median) of the available values.
This is the same as the 50th [percentile].

Examples:
<screen>
import analysis::statistics::Descriptive;
median([1,2,5,7,8]);
median([1,2,2,6,7,8]);
</screen>

}
real median(list[int] nums) 
	= mean(middle(nums));
real median(list[real] nums) 
	= mean(middle(nums));
rat median(list[rat] nums) 
	= mean(middle(nums));
num median(list[num] nums) 
	= mean(middle(nums));

@doc{
Synopsis: Smallest data value.
}
(&T <: num) min(list[&T <: num] nums) throws EmptyList
	= (head(nums) | it > n ? n : it | n <- tail(nums));

@doc{
Synopsis: Percentile of data values.

Description:

Returns the `p`th [percentile](http://en.wikipedia.org/wiki/Percentile) of the data values.
 0 < `p` <= 100 should hold.

}
&T <: num percentile(list[&T <: num] nums, num p) {
	if (0 > p || p > 100) {
		throw IllegalArgument(p, "Percentile argument should be between 0 and 100");
	}
	if (nums == []) {
		throw EmptyList();
	}
	nums = sort(nums);
	idx = max(1., toReal(size(nums)) * (toReal(p) / 100));
	return nums[ceil(idx) - 1];
}


num variance(list[num] l:[]) {
	throw IllegalArgument(l,"variance cannot be calculated for empty lists");
}
@doc{
Synopsis: Variance of data values.

Description:
Computes the [variance](http://en.wikipedia.org/wiki/Variance) of the data values.
It measures how far a set of numbers is spread out.
}
num variance([num hd, *num tl]) {
	if (tl == []) {
		return 0.;	
	}
	//Compensated variant of the two pass algorithm
	n = 1 + size(tl);
	mn = mean(tl + hd);
	sum2 = (pow(hd - mn, 2) | it + pow(i - mn, 2) | i <- tl); 
	sum3 = (hd - mn | it + (i - mn) | i <- tl); 
	return (sum2 - (pow(sum3,2)/n)) / (n -1);
}

@doc{
Synopsis: Skewness of data values.

Description:
Returns the [skewness](http://en.wikipedia.org/wiki/Skewness) of the available values. Skewness is a measure of the asymmetry of a given distribution.
}
@javaClass{org.rascalmpl.library.analysis.statistics.Descriptive}
public java num skewness(list[num] values);

@doc{
Synopsis: Standard deviation of data values.

Description:
Computes the [standard deviation](http://en.wikipedia.org/wiki/Standard_deviation)
of the data values. It shows how much variation exists from the average (mean, or expected value). 
}
@javaClass{org.rascalmpl.library.analysis.statistics.Descriptive}
public java num standardDeviation(list[num] values);

public (&T <:num) sum(list[(&T <:num)] _:[]) {
	throw ArithmeticException(
		"For the emtpy list it is not possible to decide the correct precision to return.\n
		'If you want to call sum on empty lists, use sum([0.000]+lst) or sum([0r] +lst) or sum([0]+lst) 
		'to make the list non-empty and indicate the required precision for the sum of the empty list
		");
}
@doc{
Synopsis: Sum of data values.
}
public default (&T <:num) sum([(&T <: num) hd, *(&T <: num) tl])
	= (hd | it + i | i <- tl);

@doc{
Synopsis: Sum of the squares of data values.
}
@javaClass{org.rascalmpl.library.analysis.statistics.Descriptive}
public java num sumsq(list[num] values);

