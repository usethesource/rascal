@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

@synopsis{Statistical methods for simple regression.}
@description{
The following functions are provided:
(((TOC)))
}
module analysis::statistics::SimpleRegression

import Exception;


@synopsis{Intercept of regression line.}
@description{
Returns the [interce](http://en.wikipedia.org/wiki/Root_of_a_function) of the estimated regression line.
The least squares estimate of the intercept is computed using these [normal equations](http://www.xycoon.com/estimation4.htm)
}

@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num intercept(lrel[num,num] values) throws IllegalArgument;


@synopsis{Standard error of intercept estimate.}
@description{
Returns the http://www.xycoon.com/standarderrorb0.htm[standard error of the intercept estimate], usually denoted s(b0).
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num interceptStdErr(lrel[num,num] values) throws IllegalArgument;


@synopsis{Sum of squared errors divided by the degrees of freedom.}
@description{
Returns the sum of squared errors divided by the degrees of freedom, usually abbreviated MSE.
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num meanSquareError(lrel[num,num] values) throws IllegalArgument;


@synopsis{Pearson's product-moment correlation coefficient.}
@description{
Computes Pearson's product-moment correlation coefficient.
More functions related to this coefficient can be found in ((module:analysis::statistics::Correlation)).
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num R(lrel[num,num] values) throws IllegalArgument;


@synopsis{Sum of squared deviations of the predicted y values about their mean.}
@description{
Returns the sum of squared deviations of the predicted y values about their mean (which equals the mean of y).
This is usually abbreviated SSR or [SSM](http://www.xycoon.com/SumOfSquares.htm).
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num regressionSumSquares(list[tuple[num,num]] values) throws IllegalArgument;


@synopsis{Coefficient of determination.}
@description{
Returns the [coefficient of determination](http://en.wikipedia.org/wiki/Coefficient_of_determination) usually denoted r__^2^.
It provides a measure of how well future outcomes are likely to be predicted by the regression model.
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num RSquare(lrel[num,num] values) throws IllegalArgument;


@synopsis{Significance of the slope correlation.}
@description{
Returns the significance level of the slope (equiv) correlation.
Specifically, the returned value is the smallest alpha such that the slope confidence interval with significance level equal to alpha does not include 0. On regression output, this is often denoted Prob(|t| > 0)
}
@pitfalls{
The validity of this statistic depends on the assumption that the observations included in the model are drawn from a 
[Bivariate Normal Distribution](http://en.wikipedia.org/wiki/Bivariate_normal_distribution).
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num significance(lrel[num,num] values) throws IllegalArgument;


@synopsis{Slope of regression line.}
@description{
Returns the slope of the estimated regression line.
The least squares estimate of the slope is computed using the http://www.xycoon.com/estimation4.htm[normal equations].
The slope is sometimes denoted b1.
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num slope(lrel[num,num] values) throws IllegalArgument;


@synopsis{The 95% slope confidence interval.}
@description{
Returns the half-width of a 95% confidence interval for the slope estimate.
The 95% confidence interval is

(slope - slopeConfidenceInterval, slope + slopeConfidenceInterval)
}
@pitfalls{
* The validity of this statistic depends on the assumption that the observations included in the model are drawn from a 
[Bivariate Normal Distribution](http://en.wikipedia.org/wiki/Bivariate_normal_distribution)
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num slopeConfidenceInterval(lrel[num,num] values) throws IllegalArgument;


@synopsis{Standard error of slope estimate.}
@description{
Returns the http://www.xycoon.com/standarderrorb0.htm[standard error of the slope estimate], usually denoted s(b1).
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num slopeStdErr(lrel[num,num] values) throws IllegalArgument;


@synopsis{Sum of cross products of observations.}
@description{
Returns the sum of crossproducts, x__~i~*y__~i~.
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num sumOfCrossProducts(lrel[num,num] values) throws IllegalArgument;


@synopsis{Sum of squared errors.}
@description{
Returns the sum of squared errors (SSE) associated with the regression model.
The sum is computed using the computational formula

SSE = SYY - (SXY * SXY / SXX)

where SYY is the sum of the squared deviations of the y values about their mean, SXX is similarly defined and SXY is the sum of the products of x and y mean deviations.

The return value is constrained to be non-negative, i.e., if due to rounding errors the computational formula returns a negative result, 0 is returned.
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num sumSquaredErrors(lrel[num,num] values) throws IllegalArgument;


@synopsis{Sum of squared deviations.}
@description{
Returns the sum of squared deviations of the y values about their mean.
This is defined as http://www.xycoon.com/SumOfSquares.htm[SSTO].
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num totalSumSquares(lrel[num,num] values) throws IllegalArgument;


@synopsis{Sum of squared deviations of x values about their mean.}
@description{
Returns the sum of squared deviations of the x values about their mean.
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num XSumSquares(lrel[num,num] values) throws IllegalArgument;


@synopsis{Predict a value.}
@description{
Returns the "predicted" `y` value associated with the supplied `x` value, based on regression model derived from the provided data values:

`predict(x) = intercept + slope * x`
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num predict(lrel[num,num] values, num x) throws IllegalArgument;
