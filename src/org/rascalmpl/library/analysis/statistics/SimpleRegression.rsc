module analysis::statistics::SimpleRegression


@doc{
Synopsis: Intercept of regression line.
Description:

Returns the [intercept](http://en.wikipedia.org/wiki/Root_of_a_function) of the estimated regression line.
The least squares estimate of the intercept is computed using these [normal equations](http://www.xycoon.com/estimation4.htm).
}

@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num intercept(list[tuple[num,num]] values) throws IllegalArgument(value v, str message);

@doc{
Synopsis: Standard error of intercept estimate.
Description:
Returns the [standard error of the intercept estimate](http://www.xycoon.com/standarderrorb0.htm), usually denoted s(b0).
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num interceptStdErr(list[tuple[num,num]] values) throws IllegalArgument(value v, str message);

@doc{
Synopsis: Sum of squared errors divided by the degrees of freedom.

Description:

Returns the sum of squared errors divided by the degrees of freedom, usually abbreviated MSE.
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num meanSquareError(list[tuple[num,num]] values) throws IllegalArgument(value v, str message);

@doc{
Synopsis: Pearson's product-moment correlation coefficient.

Description:
Computes Pearson's product-moment correlation coefficient.
More functions related to this coefficient can be found in [Correlation].
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num R(list[tuple[num,num]] values) throws IllegalArgument(value v, str message);

@doc{
Synopsis: Sum of squared deviations of the predicted y values about their mean.

Description:

Returns the sum of squared deviations of the predicted y values about their mean (which equals the mean of y).
This is usually abbreviated SSR or [SSM](http://www.xycoon.com/SumOfSquares.htm).
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num regressionSumSquares(list[tuple[num,num]] values) throws IllegalArgument(value v, str message);

@doc{
Synopsis: Coefficient of determination.
Description:

Returns the [coefficient of determination](http://en.wikipedia.org/wiki/Coefficient_of_determination) usually denoted r$^2$.
It provides a measure of how well future outcomes are likely to be predicted by the regression model.
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num RSquare(list[tuple[num,num]] values) throws IllegalArgument(value v, str message);

@doc{
Synopsis: Significance of the slope correlation.
Description:

Returns the significance level of the slope (equiv) correlation.
Specifically, the returned value is the smallest alpha such that the slope confidence interval with significance level equal to alpha does not include 0. On regression output, this is often denoted Prob(|t| > 0)

Pitfalls:
The validity of this statistic depends on the assumption that the observations included in the model are drawn from a 
[Bivariate Normal Distribution](http://en.wikipedia.org/wiki/Bivariate_normal_distribution).
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num significance(list[tuple[num,num]] values) throws IllegalArgument(value v, str message);

@doc{
Synopsis: Slope of regression line.
Description:
Returns the slope of the estimated regression line.
The least squares estimate of the slope is computed using the [normal equations](http://www.xycoon.com/estimation4.htm).
The slope is sometimes denoted b1.
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num slope(list[tuple[num,num]] values) throws IllegalArgument(value v, str message);

@doc{
Synopsis: The 95% slope confidence interval.

Description:

Returns the half-width of a 95% confidence interval for the slope estimate.
The 95% confidence interval is

(slope - slopeConfidenceInterval, slope + slopeConfidenceInterval)


Pitfalls:
The validity of this statistic depends on the assumption that the observations included in the model are drawn from a 
[Bivariate Normal Distribution](http://en.wikipedia.org/wiki/Bivariate_normal_distribution).

}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num slopeConfidenceInterval(list[tuple[num,num]] values) throws IllegalArgument(value v, str message);

@doc{
Synopsis: Standard error of slope estimate.
Description:

Returns the [standard error of the slope estimate](http://www.xycoon.com/standarderrorb0.htm), usually denoted s(b1).
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num slopeStdErr(list[tuple[num,num]] values) throws IllegalArgument(value v, str message);

@doc{
Synopsis: Sum of cross products of observations.
Description:

Returns the sum of crossproducts, x$_i$*y$_i$.
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num sumOfCrossProducts(list[tuple[num,num]] values) throws IllegalArgument(value v, str message);

@doc{
Synopsis: Sum of squared errors.
Description:

Returns the sum of squared errors (SSE) associated with the regression model.
The sum is computed using the computational formula

SSE = SYY - (SXY * SXY / SXX)

where SYY is the sum of the squared deviations of the y values about their mean, SXX is similarly defined and SXY is the sum of the products of x and y mean deviations.

The return value is constrained to be non-negative, i.e., if due to rounding errors the computational formula returns a negative result, 0 is returned.
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num sumSquaredErrors(list[tuple[num,num]] values) throws IllegalArgument(value v, str message);

@doc{
Synopsis: Sum of squared deviations.
Description:

Returns the sum of squared deviations of the y values about their mean.
This is defined as [SSTO](http://www.xycoon.com/SumOfSquares.htm).

}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num totalSumSquares(list[tuple[num,num]] values) throws IllegalArgument(value v, str message);

@doc{
Synopsis: Sum of squared deviations of x values about their mean.

Description:

Returns the sum of squared deviations of the x values about their mean.
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num XSumSquares(list[tuple[num,num]] values) throws IllegalArgument(value v, str message);

@doc{
Synopsis: Predict a value.
Description:

Returns the "predicted" `y` value associated with the supplied `x` value, based on regression model derived from the provided data values:

`predict(x) = intercept + slope * x`
}
@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num predict(list[tuple[num,num]] values, num x) throws IllegalArgument(value v, str message);


/*
-- regression only in V3.0


@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num regressionAdjustedRSquared(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num regressionCoVarianceOfParameters(list[tuple[num,num]] values, int i, j);

@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num regressionErrorSumSquares(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num regressionMeanSquareError(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num regressionN(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num regressionNumberOfParameters(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num regressionParameterEstimate(list[tuple[num,num]] values, int i);

@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java list[num] regressionParameterEstimates(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num regressionSumSquares(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num regressionRSquared(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num regressionStdErrorOfEstimate(list[tuple[num,num]] values, int index);

@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java list[num] regressionStdErrorOfEstimates(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.analysis.statistics.SimpleRegressions}
public java num regressionTotalSumSquares(list[tuple[num,num]] values);

*/



