package org.rascalmpl.library.analysis.statistics;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math.MathException;
import org.apache.commons.math.stat.inference.ChiSquareTestImpl;
import org.apache.commons.math.stat.inference.OneWayAnovaImpl;
import org.apache.commons.math.stat.inference.TTestImpl;
import org.apache.commons.math.stat.ranking.NaturalRanking;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class Inferences {
	private final IValueFactory values;
	private TypeFactory types;
	
	public Inferences(IValueFactory values){
		super();
		this.types = TypeFactory.getInstance();
		this.values = values;
	}
	
	double [] expected;
	long [] observed;

	void makeChi(IList dataValues){
		int n = dataValues.length();
		expected = new double[n];
		observed = new long[n];
	
		int i = 0;
		for(IValue v : dataValues){
			ITuple t = (ITuple) v;
			INumber exp = (INumber) t.get(0);
			INumber obs = (INumber) t.get(1);
			expected[i] = (long) exp.toReal().doubleValue();
			observed[i] = obs.toInteger().longValue();
			
			if(expected[i] < 0 || observed[i] < 0) throw RuntimeExceptionFactory.illegalArgument(dataValues, null, null, "Chi test requires positive values");
			i++;
		}
	}
	
	double[] makeT(IList dataValues){
		int n = dataValues.length();
		double [] data = new double[n];
	
		for(int i = 0; i < n; i++){
			INumber d = (INumber) dataValues.get(i);
			data[i] = d.toReal().doubleValue();
		}
		
		return data;
	}
	
	public IValue chiSquare(IList dataValues){
		makeChi(dataValues);
		return values.real(new ChiSquareTestImpl().chiSquare(expected, observed));
	}
	
	public IValue chiSquareTest(IList dataValues){
		makeChi(dataValues);
		try {
			return values.real(new ChiSquareTestImpl().chiSquareTest(expected, observed));
		} catch (IllegalArgumentException e) {
			throw RuntimeExceptionFactory.illegalArgument(dataValues, null, null, e.getMessage());
		} catch (MathException e) {
			throw RuntimeExceptionFactory.illegalArgument(dataValues, null, null, e.getMessage());
		}
	}
	
	public IValue chiSquareTest(IList dataValues, IReal alpha){
		makeChi(dataValues);
		try {
			return values.bool(new ChiSquareTestImpl().chiSquareTest(expected, observed, alpha.doubleValue()));
		} catch (IllegalArgumentException e) {
			throw RuntimeExceptionFactory.illegalArgument(dataValues, null, null, e.getMessage());
		} catch (MathException e) {
			throw RuntimeExceptionFactory.illegalArgument(dataValues, null, null, e.getMessage());
		}
	}
	
	public IValue tTest(IList sample1, IList sample2){
		double s1[] = makeT(sample1);
		double s2[] = makeT(sample2);
		//for(int i = 0; i < s1.length; i++) System.err.println("s1[" + i + "] = " + s1[i]);
		//for(int i = 0; i < s2.length; i++) System.err.println("s2[" + i + "] = " + s2[i]);
		try {
			double r = new TTestImpl().tTest(s1, s2);
			System.err.println("r = " + r);
			return values.real(r);
		} catch (IllegalArgumentException e) {
			throw RuntimeExceptionFactory.illegalArgument(sample1, null, null, e.getMessage());
		} catch (MathException e) {
			throw RuntimeExceptionFactory.illegalArgument(sample1, null, null, e.getMessage());
		}
	}
	
	public IValue tTest(IList sample1, IList sample2, INumber alpha){
		double s1[] = makeT(sample1);
		double s2[] = makeT(sample2);
		try {
			return values.bool(new TTestImpl().tTest(s1, s2, alpha.toReal().doubleValue()));
		} catch (IllegalArgumentException e) {
			throw RuntimeExceptionFactory.illegalArgument(sample1, null, null, e.getMessage());
		} catch (MathException e) {
			throw RuntimeExceptionFactory.illegalArgument(sample1, null, null, e.getMessage());
		}
	}
	
	public IValue tTest(INumber mu, IList sample, INumber alpha){
		double s[] = makeT(sample);
		try {
			return values.bool(new TTestImpl().tTest( mu.toReal().doubleValue(), s, alpha.toReal().doubleValue()));
		} catch (IllegalArgumentException e) {
			throw RuntimeExceptionFactory.illegalArgument(sample, null, null, e.getMessage());
		} catch (MathException e) {
			throw RuntimeExceptionFactory.illegalArgument(sample, null, null, e.getMessage());
		}
	}
	
	
	public IValue gini(IList dataValues){
		
		if(dataValues.length() < 2)
			throw RuntimeExceptionFactory.illegalArgument(dataValues, null, null, "At least 2 observations required");
		double sum = 0;
		double g = 0;
		double N = 0;
		
		double xvalues[] = new double[dataValues.length()];
		for(int i = 0; i < dataValues.length(); i++){
			ITuple T = (ITuple) dataValues.get(i);
			xvalues[i] = ((INumber) T.get(0)).toReal().doubleValue();
		}
		
		// Create a natural ranking: largest first.
		double rank[] = new NaturalRanking().rank(xvalues);
		
		for(int i = 0; i < rank.length; i++){
			rank[i] = rank.length - rank[i] + 1; // invert the ranking: smallest come now first.
			System.err.println("rank[" + i + "] = " + rank[i]);
		}
		
		for(int i = 0; i < dataValues.length(); i++){
			ITuple T = (ITuple) dataValues.get(i);
			double Y = ((INumber) T.get(1)).toReal().doubleValue();
			if(Y < 0)
				throw RuntimeExceptionFactory.illegalArgument(T, null, null, "Frequency should be positive");
			
			g += xvalues[i] * rank[i] * Y;
			N += Y;
			sum += xvalues[i] * Y;
		}
		double avg = sum / N;
		
		return values.real((N+1)/(N-1) - (2 * g) / (N * (N-1) * avg));
	}
	
	Collection<double[]> makeAnova(IList dataValues){
		int n = dataValues.length();
		Collection<double[]> res = new ArrayList<double[]>();
		for(int i = 0; i < n; i++){
			IList cat = (IList) dataValues.get(i);
			int m = cat.length();
			double[] dat = new double[m];
			for(int j = 0; j < m; j++){
				dat[i] = ((INumber) cat.get(j)).toReal().doubleValue();
			}
			res.add(dat);
		}
		return res;
	}
	
	public IValue anovaFValue(IList categoryData){
		try {
			return values.real(new OneWayAnovaImpl().anovaFValue(makeAnova(categoryData)));
		} catch (IllegalArgumentException e) {
			throw RuntimeExceptionFactory.illegalArgument(categoryData, null, null, e.getMessage());
		} catch (MathException e) {
			throw RuntimeExceptionFactory.illegalArgument(categoryData, null, null, e.getMessage());
		}
	}
	
	public IValue anovaPValue(IList categoryData){
		try {
			return values.real(new OneWayAnovaImpl().anovaPValue(makeAnova(categoryData)));
		} catch (IllegalArgumentException e) {
			throw RuntimeExceptionFactory.illegalArgument(categoryData, null, null, e.getMessage());
		} catch (MathException e) {
			throw RuntimeExceptionFactory.illegalArgument(categoryData, null, null, e.getMessage());
		}
	}
	
	public IValue anovaTest(IList categoryData, INumber alpha){
		try {
			return values.bool(new OneWayAnovaImpl().anovaTest(makeAnova(categoryData), alpha.toReal().doubleValue()));
		} catch (IllegalArgumentException e) {
			throw RuntimeExceptionFactory.illegalArgument(categoryData, null, null, e.getMessage());
		} catch (MathException e) {
			throw RuntimeExceptionFactory.illegalArgument(categoryData, null, null, e.getMessage());
		}
	}
}
