module experiments::Statistics::Correlation

@javaClass{org.rascalmpl.library.experiments.Statistics.Correlations}
public java num PearsonsCorrelation(list[tuple[num x,num y]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.Correlations}
public java list[real] PearsonsCorrelationStandardErrors(list[tuple[num x,num y]] values);

@javaClass{org.rascalmpl.library.experiments.Statistics.Correlations}
public java num SpearmansCorrelation(list[tuple[num x,num y]] values);