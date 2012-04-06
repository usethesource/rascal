package org.rascalmpl.library.experiments.Statistics;

import org.apache.commons.math.MathException;
import org.apache.commons.math.stat.regression.SimpleRegression;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

public class SimpleRegressions {
	
	private final IValueFactory values;
	
	public SimpleRegressions(IValueFactory values){
		super();
		this.values = values;
	}
	
	SimpleRegression make(IList dataValues){
		SimpleRegression simple = new SimpleRegression();
		for(IValue v : dataValues){
			ITuple t = (ITuple) v;
			INumber x = (INumber) t.get(0);
			INumber y = (INumber) t.get(1);
			simple.addData(x.toReal().doubleValue(), y.toReal().doubleValue());
		}
		return simple;
	}

	
	public IValue intercept(IList dataValues) {
		return values.real(make(dataValues).getIntercept());
	}
	
	public IValue interceptStdErr(IList dataValues) {
		return values.real(make(dataValues).getInterceptStdErr());
	}
	
	public IValue meanSquareError(IList dataValues) {
		return values.real(make(dataValues).getMeanSquareError());
	}
	
	public IValue R(IList dataValues) {
		return values.real(make(dataValues).getR());
	}
	
	public IValue RSquare(IList dataValues) {
		return values.real(make(dataValues).getRSquare());
	}
	
	public IValue regressionSumSquares(IList dataValues) {
		return values.real(make(dataValues).getRegressionSumSquares());
	}
	
	public IValue significance(IList dataValues) {
		try {
			return values.real(make(dataValues).getSignificance());
		} catch (MathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataValues;
	}
	
	public IValue slope(IList dataValues) {
		return values.real(make(dataValues).getSlope());
	}
	
	public IValue slopeConfidenceInterval(IList dataValues) {
		try {
			return values.real(make(dataValues).getSlopeConfidenceInterval());
		} catch (MathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataValues;
	}
	
	public IValue slopeStdErr(IList dataValues) {
		return values.real(make(dataValues).getSlopeStdErr());
	}
	
	public IValue sumOfCrossProducts(IList dataValues) {
		return values.real(make(dataValues).getSumOfCrossProducts());
	}
	
	public IValue sumSquaredErrors(IList dataValues) {
		return values.real(make(dataValues).getSumSquaredErrors());
	}
	
	public IValue totalSumSquares(IList dataValues) {
		return values.real(make(dataValues).getTotalSumSquares());
	}
	
	public IValue XSumSquares(IList dataValues) {
		return values.real(make(dataValues).getXSumSquares());
	}
	
	public IValue predict(IList dataValues, INumber x) {
		return values.real(make(dataValues).predict(x.toReal().doubleValue()));
	}
	
}
