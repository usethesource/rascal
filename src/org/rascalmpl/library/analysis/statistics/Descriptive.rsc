module analysis::statistics::Descriptive

@doc{
Synopsis: Geometric mean of data values.

Description:

Computes the [geometric mean](http://en.wikipedia.org/wiki/Geometric_mean) of the given data values.
}
@javaClass{org.rascalmpl.library.analysis.statistics.Descriptive}
public java num geometricMean(list[num] values);

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
@javaClass{org.rascalmpl.library.analysis.statistics.Descriptive}
public java num max(list[num] values);

@doc{
Synopsis: Arithmetic mean of data values.

Description:

Computes the [arithmetic mean](http://en.wikipedia.org/wiki/Arithmetic_mean) of the data values.
}
@javaClass{org.rascalmpl.library.analysis.statistics.Descriptive}
public java num mean(list[num] values);

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
@javaClass{org.rascalmpl.library.analysis.statistics.Descriptive}
public java num median(list[num] values);

@doc{
Synopsis: Smallest data value.
}
@javaClass{org.rascalmpl.library.analysis.statistics.Descriptive}
public java num min(list[num] values);

@doc{
Synopsis: Percentile of data values.

Description:

Returns the `p`th [percentile](http://en.wikipedia.org/wiki/Percentile) of the data values.
 0 < `p` <= 100 should hold.

}
@javaClass{org.rascalmpl.library.analysis.statistics.Descriptive}
public java num percentile(list[num] values, num p);

@doc{
Synopsis: Variance of data values.

Description:
Computes the [variance](http://en.wikipedia.org/wiki/Variance) of the data values.
It measures how far a set of numbers is spread out.
}
@javaClass{org.rascalmpl.library.analysis.statistics.Descriptive}
public java num variance(list[num] values);

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

@doc{
Synopsis: Sum of data values.
}
@javaClass{org.rascalmpl.library.analysis.statistics.Descriptive}
public java num sum(list[num] values);

@doc{
Synopsis: Sum of the squares of data values.
}
@javaClass{org.rascalmpl.library.analysis.statistics.Descriptive}
public java num sumsq(list[num] values);

