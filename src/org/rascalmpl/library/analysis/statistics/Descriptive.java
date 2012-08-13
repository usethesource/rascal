package org.rascalmpl.library.analysis.statistics;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

public class Descriptive {
	private final IValueFactory values;
	
	public Descriptive(IValueFactory values){
		super();
		this.values = values;
	}
	
	DescriptiveStatistics make(IList dataValues){
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for(IValue v : dataValues){
			stats.addValue(((INumber) v).toReal().floatValue());
		}
		return stats;
	}

	public IValue geometricMean(IList dataValues){
		return values.real(make(dataValues).getGeometricMean());
	}
	
	public IValue kurtosis(IList dataValues){
		return values.real(make(dataValues).getKurtosis());
	}
	
	public IValue max(IList dataValues){
		return values.real(make(dataValues).getMax());
	}

	public IValue mean(IList dataValues){
		return values.real(make(dataValues).getMean());
	}
	
	public IValue median(IList dataValues){
		return values.real(make(dataValues).getPercentile(50));
	}
	
	public IValue min(IList dataValues){
		return values.real(make(dataValues).getMin());
	}
	
	public IValue percentile(IList dataValues, INumber p){
		double d = ((INumber) p).toReal().floatValue();
		return values.real(make(dataValues).getPercentile(d));
	}
	
	public IValue variance(IList dataValues){
		return values.real(make(dataValues).getVariance());
	}
	
	public IValue skewness(IList dataValues){
		return values.real(make(dataValues).getSkewness());
	}
	
	public IValue standardDeviation(IList dataValues){
		return values.real(make(dataValues).getStandardDeviation());
	}
	
	public IValue sum(IList dataValues){
		return values.real(make(dataValues).getSum());
	}
	
	public IValue sumsq(IList dataValues){
		return values.real(make(dataValues).getSumsq());
	}

}
