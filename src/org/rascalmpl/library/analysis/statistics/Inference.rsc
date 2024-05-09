@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

@synopsis{Statistical inference methods.}
@description{
The following functions are provided:
(((TOC)))
}
module analysis::statistics::Inference


@synopsis{Chi-square coefficient of data values.}
@description{
Compute the [ChiSquare statistic](http://en.wikipedia.org/wiki/Chi-square_statistic) comparing observed and expected frequency counts.
}
@examples{
Consider an example from the web page mentioned above.
To test the hypothesis that a random sample of 100 people has been drawn from a population in which men and women are equal in frequency, the observed number of men and women would be compared to the theoretical frequencies of 50 men and 50 women. If there were 44 men in the sample and 56 women, then we have the following:

```rascal-shell
import analysis::statistics::Inference;
chiSquare([<50, 44>, <50, 56>])
```
}
@javaClass{org.rascalmpl.library.analysis.statistics.Inferences}
public java num chiSquare(lrel[num expected, int observed] values);


@synopsis{Chi-square test on data values.}
@description{
Perform a [chi-square test](http://en.wikipedia.org/wiki/Pearson%27s_chi-squared_test) comparing
expected and observed frequency counts. There are two forms of this test:

*  Returns the _observed significance level_, or p-value, associated with a Chi-square goodness of fit test 
comparing observed frequency counts to expected counts.

*   Performs a Chi-square goodness of fit test evaluating the null hypothesis that the observed counts conform to the frequency distribution described by the expected counts, with significance level `alpha` (0 < `alpha` < 0.5). Returns true iff the null hypothesis
can be rejected with confidence 1 - `alpha`.
}
@javaClass{org.rascalmpl.library.analysis.statistics.Inferences}
public java num chiSquareTest(lrel[num expected, int observed] values);

@javaClass{org.rascalmpl.library.analysis.statistics.Inferences}
public java bool chiSquareTest(lrel[num expected, int observed] values, real alpha);


@synopsis{T-test on sample data.}
@description{
Perform [student's t-test](http://en.wikipedia.org/wiki/Student's_t-test)
The test is provided in three variants:

*  Returns the _observed significance level_, or _p-value_, associated with a two-sample, two-tailed t-test comparing the means of the input samples. The number returned is the smallest significance level at which one can reject the null hypothesis that the two means are equal in favor of the two-sided alternative that they are different. For a one-sided test, divide the returned value by 2. 

The t-statistic used is `t = (m1 - m2) / sqrt(var1/n1 + var2/n2)`
where 

**  `n1` is the size of the first sample 
**  `n2` is the size of the second sample; 
**  `m1` is the mean of the first sample; 
**  `m2` is the mean of the second sample; 
**  `var1` is the variance of the first sample; 
**  `var2` is the variance of the second sample.

*  Performs a two-sided t-test evaluating the null hypothesis that `sample1` and `sample2` are drawn from populations with the same mean, with significance level `alpha`. This test does not assume that the subpopulation variances are equal. 
Returns true iff the null hypothesis that the means are equal can be rejected with confidence 1 - `alpha`. To perform a 1-sided test, use `alpha` / 2.

*  Performs a two-sided t-test evaluating the null hypothesis that the mean of the population from which sample is drawn equals `mu`.
Returns true iff the null hypothesis can be rejected with confidence 1 - `alpha`. To perform a 1-sided test, use `alpha` * 2.
}
@examples{
We use the data from the following http://web.mst.edu/~psyworld/texample.htm#1[example] to illustrate the t-test.
First, we compute the t-statistic using the formula given above.
```rascal-shell
import util::Math;
import analysis::statistics::Descriptive;
import List;
s1 = [5,7,5,3,5,3,3,9];
s2 = [8,1,4,6,6,4,1,2];
(mean(s1) - mean(s2))/sqrt(variance(s1)/size(s1) + variance(s2)/size(s2));
```
This is the same result as obtained in the cited example.
We can also compute it directly using the `tTest` functions:
```rascal-shell,continue
import analysis::statistics::Inference;
tTest(s1, s2);
```
Observe that this is a smaller value than comes out directly of the formula.
Recall that: _The number returned is the smallest significance level at which one can reject the null hypothesis that the two means are equal in favor of the two-sided alternative that they are different._
Finally, we perform the test around the significance level we just obtained:
```rascal-shell,continue
tTest(s1,s2,0.40);
tTest(s1,s2,0.50);
```
}
@javaClass{org.rascalmpl.library.analysis.statistics.Inferences}
public java num tTest(list[num] sample1, list[num] sample2);

@javaClass{org.rascalmpl.library.analysis.statistics.Inferences}
public java bool tTest(list[num] sample1, list[num] sample2, num alpha);

@javaClass{org.rascalmpl.library.analysis.statistics.Inferences}
public java bool tTest(num mu, list[num] sample, num alpha);




@synopsis{Analysis of Variance (ANOVA) f-value.}
@description{
Perform [Analysis of Variance test](http://en.wikipedia.org/wiki/Analysis_of_variance)
also described [here](http://www.statsoft.com/textbook/anova-manova/)

Compute the F statistic -- also known as [F-test](http://en.wikipedia.org/wiki/F-test) -- using the definitional formula
   `F = msbg/mswg`
where

*  `msbg` = between group mean square.
*  `mswg` = within group mean square.


are as defined [here](http://faculty.vassar.edu/lowry/ch13pt1.html)
}
@javaClass{org.rascalmpl.library.analysis.statistics.Inferences}
public java num anovaFValue(list[list[num]] categoryData);


@synopsis{Analysis of Variance (ANOVA) p-value.}
@description{
Perform [Analysis of Variance test](http://en.wikipedia.org/wiki/Analysis_of_variance)
also described [here](http://www.statsoft.com/textbook/anova-manova/)

Computes the exact p-value using the formula `p = 1 - cumulativeProbability(F)`
where `F` is the ((anovaFValue)).
}
@javaClass{org.rascalmpl.library.analysis.statistics.Inferences}
public java num anovaPValue(list[list[num]] categoryData);


@synopsis{Analysis of Variance (ANOVA) test.}
@description{
Perform [Analysis of Variance](http://en.wikipedia.org/wiki/Analysis_of_variance)
also described [here](http://www.statsoft.com/textbook/anova-manova/)

Returns true iff the estimated p-value is less than `alpha` (0 < `alpha` <= 0.5).

The exact p-value is computed using the formula `p = 1 - cumulativeProbability(F)`
where `F` is the ((anovaFValue)).
}
@javaClass{org.rascalmpl.library.analysis.statistics.Inferences}
public java bool anovaTest(list[list[num]] categoryData, num alpha);



@synopsis{Gini coefficient.}
@description{
Computes the [Gini Coefficient](http://en.wikipedia.org/wiki/Gini_coefficient)
that measures the inequality among values in a frequency distribution.

The Gini coefficient is computed using Deaton's formula and returns a
value between 0 (completely equal distribution) and
1 (completely unequal distribution).
}
@examples{
```rascal-shell
import analysis::statistics::Inference;
```
A completely equal distribution:
```rascal-shell,continue
gini([<10000, 1>, <10000, 1>, <10000, 1>]);
```
A rather unequal distribution:
```rascal-shell,continue
gini([<998000, 1>, <20000, 3>, <117500, 1>, <70000, 2>, <23500, 5>, <45200,1>]);
```
}

@javaClass{org.rascalmpl.library.analysis.statistics.Inferences}
public java real gini(lrel[num observation,int frequency] values);
