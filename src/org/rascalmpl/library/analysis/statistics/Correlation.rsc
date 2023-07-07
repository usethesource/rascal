@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

@synopsis{

Correlation between data values.

}
@description{

Compute the [correlation](http://en.wikipedia.org/wiki/Correlation) between pairs of data values.
Correlation measures the statistical relationship between two sets of data.

The following functions are provided:
(((TOC)))
}
module analysis::statistics::Correlation


@synopsis{

Pearson product-moment correlation coefficient.

}
@description{

Compute the [Pearson product-moment correlation coefficient](http://en.wikipedia.org/wiki/Pearson_product-moment_correlation_coefficient).
It is a measure of the strength of the linear dependence between two variables.

}
@pitfalls{

Use ((SpearmansCorrelation)) when there is a *monotonous dependence* between the two variables.
}
@javaClass{org.rascalmpl.library.analysis.statistics.Correlations}
public java num PearsonsCorrelation(lrel[num x,num y] values);


@synopsis{

Standard errors associated with Pearson correlation.
}
@javaClass{org.rascalmpl.library.analysis.statistics.Correlations}
public java list[real] PearsonsCorrelationStandardErrors(lrel[num x,num y] values);


@synopsis{

P-values (significance) associated with Pearson correlation.
}
@javaClass{org.rascalmpl.library.analysis.statistics.Correlations}
public java list[real] PearsonsCorrelationPValues(lrel[num x,num y] values);


@synopsis{

Spearman's rank correlation coefficient.

}
@description{

Compute [Spearman's rank correlation coefficient](http://en.wikipedia.org/wiki/Spearman's_rank_correlation_coefficient).
The correlation between the data values is computed by first performing a rank transformation
on the data values using a natural ranking and then computing ((PearsonsCorrelation)).

}
@pitfalls{

Use ((PearsonsCorrelation)) when there is a *linear dependence* between the variables.
}
@javaClass{org.rascalmpl.library.analysis.statistics.Correlations}
public java num SpearmansCorrelation(lrel[num x,num y] values);


@synopsis{

Covariance of data values.

}
@description{

Computes the [covariance](http://en.wikipedia.org/wiki/Covariance) between the `x` and `y` values.

}
@examples{

```rascal-shell
import analysis::statistics::Correlation;
covariance([<1,12>,<3,12>,<3,11>,<5,7>]);
```
}
@javaClass{org.rascalmpl.library.analysis.statistics.Correlations}
public java num covariance(lrel[num x,num y] values);
