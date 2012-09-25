module analysis::statistics::Correlation

@doc{
Synopsis: Pearson product-moment correlation coefficient.

Description:

Compute the [Pearson product-moment correlation coefficient](http://en.wikipedia.org/wiki/Pearson_product-moment_correlation_coefficient).
It is a measure of the strength of the linear dependence between two variables.

Pitfalls: Pearson's correlation can only be applied when there is a __linear dependence__ between the two variables.
Use [SpearmansCorrelation] when there is a __monotonous dependence__ between the two variables.

}
@javaClass{org.rascalmpl.library.analysis.statistics.Correlations}
public java num PearsonsCorrelation(list[tuple[num x,num y]] values);

@doc{
Synopsis: Standard errors associated with Pearson correlation. 
}
@javaClass{org.rascalmpl.library.analysis.statistics.Correlations}
public java list[real] PearsonsCorrelationStandardErrors(list[tuple[num x,num y]] values);

@doc{
Synopsis: P-values (significance) associated with Pearson correlation.
}
@javaClass{org.rascalmpl.library.analysis.statistics.Correlations}
public java list[real] PearsonsCorrelationPValues(list[tuple[num x,num y]] values);

@doc{
Synopsis: Spearman's rank correlation coefficient.

Description:

Compute [Spearman's rank correlation coefficient](http://en.wikipedia.org/wiki/Spearman's_rank_correlation_coefficient).
The correlation between the data values is computed by first performing a rank transformation
on the data values using a natural ranking and then computing [PearsonsCorrelation].

Pitfalls: Spearman's correlation can only be applied when there is a __monotonous dependence__ between the two variables.
Use [PearsonsCorrelation] when there is a __linear dependence__ between the variables.
}
@javaClass{org.rascalmpl.library.analysis.statistics.Correlations}
public java num SpearmansCorrelation(list[tuple[num x,num y]] values);

@doc{
Synopsis: Covariance of data values.

Description:

Computes the [covariance](http://en.wikipedia.org/wiki/Covariance) between the `x` and `y` values.

Examples:
<screen>
import analysis::statistics::Correlation;
covariance([<1,12>,<3,12>,<3,11>,<5,7>]);
</screen>
}
@javaClass{org.rascalmpl.library.analysis.statistics.Correlations}
public java num covariance(list[tuple[num x,num y]] values);