module experiments::Statistics::SimpleRegression


@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num intercept(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num interceptStdErr(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num meanSquareError(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num R(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num regressionSumSquares(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num RSquare(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num significance(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num slope(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num slopeConfidenceInterval(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num slopeStdErr(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num sumOfCrossProducts(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num sumSquaredErrors(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num totalSumSquares(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num XSumSquares(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num predict(list[tuple[num,num]] values, num x);


/*
-- regression only in V3.0


@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num regressionAdjustedRSquared(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num regressionCoVarianceOfParameters(list[tuple[num,num]] values, int i, j);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num regressionErrorSumSquares(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num regressionMeanSquareError(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num regressionN(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num regressionNumberOfParameters(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num regressionParameterEstimate(list[tuple[num,num]] values, int i);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java list[num] regressionParameterEstimates(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num regressionSumSquares(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num regressionRSquared(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num regressionStdErrorOfEstimate(list[tuple[num,num]] values, int index);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java list[num] regressionStdErrorOfEstimates(list[tuple[num,num]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.SimpleRegressions}
public java num regressionTotalSumSquares(list[tuple[num,num]] values);

*/



