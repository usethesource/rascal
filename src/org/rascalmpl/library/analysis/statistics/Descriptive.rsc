@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

@synopsis{Descriptive Statistics.}
@description{
Provides the following univariate (single variable) statistics functions:

(((TOC)))
}
@examples{
```rascal-shell
import analysis::statistics::Descriptive;
D = [67, 88, 55, 92.5, 102, 51];
mn = min(D);
mx = max(D);
range = mx - mn;
midrange =  mn + range/2;
sum(D);
mean(D);
geometricMean(D);
standardDeviation(D);
variance(D);
percentile(D,25);
percentile(D,50);
percentile(D,75);
```
}
module analysis::statistics::Descriptive

import Exception;
import util::Math;
import List;


@synopsis{Geometric mean of data values.}
@description{
Computes the [geometric mean](http://en.wikipedia.org/wiki/Geometric_mean) of the given data values.
}
real geometricMean([num hd, *num tl]) {
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


@synopsis{Kurtosis of data values.}
@description{
Computes the [kurtosis](http://en.wikipedia.org/wiki/Kurtosis) of the given data values.
Kurtosis is a measure of the "peakedness" of a distribution.
}
real kurtosis(list[num] values:[_, *_]) {
	varPow = pow(variance(values), 2);
	
	if (varPow == 0.) {
		throw ArithmeticException("kurtosis is undefined for values with 0 variance");	
	}
	
	return centralMoment(values, order= 4) / varPow;
}


@synopsis{Kurtosis excess of data values.}
@description{
Computes the [kurtosis excess](http://en.wikipedia.org/wiki/Kurtosis) of the given data values.
Kurtosis excess is a measure of the "peakedness" of a distribution corrected such that a normal distribution will be 0.
}
real kurtosisExcess(list[num] values) = kurtosis(values) - 3;


@synopsis{Largest data value.}
(&T <: num) max([(&T <: num) h, *(&T <: num) t]) = (h | it < n ? n : it | n <- t);


@synopsis{Arithmetic mean of data values.}
@description{
Computes the [arithmetic mean](http://en.wikipedia.org/wiki/Arithmetic_mean) of the data values.
}
real mean(list[num] nums:[_, *_]) = toReal(sum(nums)) / size(nums);



@synopsis{Median of data values.}
@description{
Returns the [median](http://en.wikipedia.org/wiki/Median) of the available values.
This is the same as the 50th ((percentile)).
}
@examples{
```rascal-shell
import analysis::statistics::Descriptive;
median([1,2,5,7,8]);
median([1,2,2,6,7,8]);
```
}
default real median(list[num] nums:[_, *_]) 
	= mean(middle(nums));

private list[&T] middle(list[&T] nums) {
	nums = sort(nums);
	n = size(nums);
	if (n % 2 == 1) {
		return [nums[n/2]];	
	}	
	n = n / 2;
	return nums[n-1..n+1];
}


@synopsis{Smallest data value.}
(&T <: num) min([(&T <: num) h, *(&T <: num) t]) = (h | it > n ? n : it | n <- t);


@synopsis{Percentile of data values.}
@description{
Returns the `p`th [percentile](http://en.wikipedia.org/wiki/Percentile) of the data values.
 0 < `p` <= 100 should hold.
}
&T <: num percentile(list[&T <: num] nums, num p) {
	if (0 > p || p > 100) {
		throw IllegalArgument(p, "Percentile argument should be between 0 and 100");
	}
	
	nums = sort(nums);
	idx = max(1., toReal(size(nums)) * (toReal(p) / 100));
	return nums[ceil(idx) - 1];
}


@synopsis{Variance of data values.}
@description{
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


@synopsis{Skewness of data values.}
@description{
Returns the [skewness](http://en.wikipedia.org/wiki/Skewness) of the available values. Skewness is a measure of the asymmetry of a given distribution.
}
real skewness(list[num] values:[_, *_]) 
	= centralMoment(values, order=3) / pow(centralMoment(values, order=2), 3/2);


@synopsis{Standard deviation of data values.}
@description{
Computes the [standard deviation](http://en.wikipedia.org/wiki/Standard_deviation)
of the data values. It shows how much variation exists from the average (mean, or expected value).
}
real standardDeviation(list[num] values) {
	if (values == []) {
		throw IllegalArgument(values,"Standard Deviation cannot be calculated for empty lists");
	}
	return sqrt(variance(values));
}


@synopsis{Sum of data values.}
public (&T <:num) sum([(&T <: num) hd, *(&T <: num) tl]) = (hd | it + i | i <- tl);


@synopsis{Sum of the squares of data values.}
(&T <:num) sumsq(list[&T <:num] values) = sum([ n * n | n <- values]);

@synopsis{Calculate the k-th central moment}
real centralMoment(list[num] nums:[_, *_], int order = 1) {
	if (order < 0) {
		throw IllegalArgument(nums,"Central moment cannot be calculated for the <order>-th order.");
	}
	if (order == 0) {
		return 1.;	
	}
	if (order == 1) {
		return 0.;	
	}
	mn = mean(nums);
	return moment([n - mn | n <- nums], order = order);
}

@synopsis{Calculate the k-th moment}
real moment(list[num] nums:[_, *_], int order = 1) {
	if (order < 0) {
		throw IllegalArgument(order,"Central moment cannot be calculated for the <order>-th order.");
	}
	if (order == 0) {
		return 1.;	
	}
	if (order == 1) {
		return toReal(sum(nums)) / size(nums);	
	}
	return (0. | it + pow(n, order) | n <- nums) / size(nums);
}
