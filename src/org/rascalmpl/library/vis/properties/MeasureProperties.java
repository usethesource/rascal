package org.rascalmpl.library.vis.properties;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureColorUtils;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.util.Key;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.values.ValueFactoryFactory;

public class MeasureProperties {
	
	public static abstract class MeasureProperty<PropType> extends PropertyValue<PropType> {

		IFigureApplet fpa;
		IEvaluatorContext ctx;
		
		IValue idVal;
		IValue valVal;
		String lastId;
		IValue lastVal;
		Key<PropType> key;

		public MeasureProperty(Properties property,IValue idVal, IValue valVal, IFigureApplet fpa,IEvaluatorContext ctx){
			super(property);
			this.ctx = ctx;
			
			this.idVal = idVal;
			this.valVal = valVal;
			this.fpa = fpa;
		}
		
		public synchronized void compute() {
			lastId = ((IString)computeIfNessecary(idVal)).getValue();
			lastVal = computeIfNessecary(valVal);
		}
		
		private IValue computeIfNessecary(IValue arg){
			if(arg.getType().isExternalType() && ((arg instanceof RascalFunction) || (arg instanceof OverloadedFunctionResult))){
				fpa.setComputedValueChanged();
				return fpa.executeRascalCallBackWithoutArguments(arg).getValue();
			} else {
				return arg;
			}
		}
		
		@SuppressWarnings("unchecked")
		public void registerMeasures(NameResolver resolver){
			//System.out.printf("Resolving lastId\n");
			Figure k = resolver.resolve(lastId);
			if(k instanceof Key){
				key = (Key<PropType>)k;
				if(property == Properties.WIDTH || property == Properties.HEIGHT){
					key.registerValue(property,ValueFactoryFactory.getValueFactory().real(0));
				}
				key.registerValue(property,lastVal);
				
			} else {
				throw RuntimeExceptionFactory.figureException("Unkown key id:" + lastId, ctx.getValueFactory().string(lastId), ctx.getCurrentAST(),
						ctx.getStackTrace());
			}
		}
		
		public PropType getValue() {
			if(property == Properties.WIDTH || property == Properties.HEIGHT){
				double high = (Double)key.scaleValue(lastVal);
				double low = (Double)key.scaleValue(ValueFactoryFactory.getValueFactory().real(0));
				//System.out.printf("High %f low %f\n", high , low);
				return (PropType)new Double(high-low);
			} else {
				if(property == Properties.FILL_COLOR){
					// TODO: fix this horrible hack
					return (PropType)(Integer)(FigureColorUtils.colorNames.get(((IString)key.scaleValue(lastVal)).getValue()).intValue());
				}
				return key.scaleValue(lastVal);
			}
		}	
		
		public boolean isConverted() { return true; }
		
		public String getKeyId() { return lastId; }
		public Key<PropType> getKey() { return key; }
		
		public IValue getUnconverted() { return lastVal;}
	}
	
	public static class MeasureRealProperty extends MeasureProperty<Double>{
		public MeasureRealProperty(Properties property, IValue idVal,
				IValue valVal, IFigureApplet fpa, IEvaluatorContext ctx) {
			super(property, idVal, valVal, fpa, ctx);
		}
	}
	
	public static class MeasureStringProperty extends MeasureProperty<String>{
		public MeasureStringProperty(Properties property, IValue idVal,
				IValue valVal, IFigureApplet fpa, IEvaluatorContext ctx) {
			super(property, idVal, valVal, fpa, ctx);
		}
	}
	
	public static class MeasureBooleanProperty extends MeasureProperty<Boolean>{
		public MeasureBooleanProperty(Properties property, IValue idVal,
				IValue valVal, IFigureApplet fpa, IEvaluatorContext ctx) {
			super(property, idVal, valVal, fpa, ctx);
		}
	}
	
	public static class MeasureIntegerProperty extends MeasureProperty<Integer>{
		public MeasureIntegerProperty(Properties property, IValue idVal,
				IValue valVal, IFigureApplet fpa, IEvaluatorContext ctx) {
			super(property, idVal, valVal, fpa, ctx);
		}
	}
	
	public static class MeasureColorProperty extends MeasureProperty<Integer>{
		public MeasureColorProperty(Properties property, IValue idVal,
				IValue valVal, IFigureApplet fpa, IEvaluatorContext ctx) {
			super(property, idVal, valVal, fpa, ctx);
		}
	}
	
	public static class MeasureFigureProperty extends MeasureProperty<Figure>{
		public MeasureFigureProperty(Properties property, IValue idVal,
				IValue valVal, IFigureApplet fpa, IEvaluatorContext ctx) {
			super(property, idVal, valVal, fpa, ctx);
		}
	}
}
