module experiments::Statistics::Frequency

@javaClass{org.rascalmpl.library.experiments.Statistics.Frequencies}
public java int cumFreq(list[value] values, num n);

@javaClass{org.rascalmpl.library.experiments.Statistics.Frequencies}
public java int cumFreq(list[value] values, str s);

@javaClass{org.rascalmpl.library.experiments.Statistics.Frequencies}
public java num cumPct(list[value] values, num n);

@javaClass{org.rascalmpl.library.experiments.Statistics.Frequencies}
public java num cumPct(list[value] values, str s);

@javaClass{org.rascalmpl.library.experiments.Statistics.Frequencies}
public java num pct(list[value] values, num n);

@javaClass{org.rascalmpl.library.experiments.Statistics.Frequencies}
public java num pct(list[value] values, str s);