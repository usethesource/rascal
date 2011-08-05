package org.rascalmpl.library.vis.properties;

import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.util.Key;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.values.ValueFactoryFactory;

public class MeasureProperties {
	
	public static abstract class MeasureProperty<PropType> extends PropertyValue<PropType> {

		ICallbackEnv fpa;
		
		IValue idVal;
		IValue valVal;
		String lastId;
		IValue lastVal;
		PropType lastValP;
		boolean alreadyParsed;

		Key key;

		public MeasureProperty(Properties property,IValue idVal, IValue valVal, ICallbackEnv fpa){
			super(property);
			
			this.idVal = idVal;
			this.valVal = valVal;
			this.fpa = fpa;
		}
		
		public synchronized void compute(ICallbackEnv env) {
			lastId = ((IString)computeIfNessecary(idVal)).getValue();
			lastVal = computeIfNessecary(valVal);
		}
		
		private IValue computeIfNessecary(IValue arg){
			if(arg.getType().isExternalType() && ((arg instanceof RascalFunction) || (arg instanceof OverloadedFunctionResult))){
				return fpa.executeRascalCallBackWithoutArguments(arg).getValue();
			} else {
				return arg;
			}
		}
		
		public void registerMeasures(NameResolver resolver){
			//System.out.printf("Resolving lastId\n");
			alreadyParsed = false;
			Figure k = resolver.resolve(lastId);
			if(k instanceof Key){
				key = (Key)k;
				if(property == Properties.WIDTH || property == Properties.HEIGHT){
					key.registerValue(property,ValueFactoryFactory.getValueFactory().real(0));
				}
				//System.out.printf("Lastval %s\n", lastVal);
				key.registerValue(property,lastVal);
				
			} else {
				throw RuntimeExceptionFactory.figureException("Unkown key id:" + lastId, fpa.getRascalContext().getValueFactory().string(lastId), fpa.getRascalContext().getCurrentAST(),
						fpa.getRascalContext().getStackTrace());
			}
		}
		
		public PropType getValue() {
			
			//if(!alreadyParsed){
				IValue v;
				if(property == Properties.WIDTH || property == Properties.HEIGHT){
					System.out.printf("Lastval %s\n", lastVal);
					IReal high = (IReal)key.scaleValue(lastVal);
					IReal low = (IReal)key.scaleValue(ValueFactoryFactory.getValueFactory().real(0));
					//System.out.printf("High %f low %f\n", high , low);
					v = high.subtract(low);
				} else {
					v= key.scaleValue(lastVal);
				}
				lastValP = parseVal(v);
				alreadyParsed = true;
				//System.out.printf("getting parsed thingie %s!\n",lastValP);
			//}
			return lastValP;
		}	
		
		abstract PropType parseVal(IValue v);
		
		public boolean isConverted() { return true; }
		
		public String getKeyId() { return lastId; }
		public Key getKey() { return key; }
		
		public IValue getUnconverted() { return lastVal;}
	}
	
	public static class MeasureRealProperty extends MeasureProperty<Double>{
		public MeasureRealProperty(Properties property, IValue idVal,
				IValue valVal, ICallbackEnv fpa) {
			super(property, idVal, valVal, fpa);
		}

		@Override
		Double parseVal(IValue v) {
			return ComputedProperties.ComputedRealProperty.convertValueS(v);
		}
	}
	
	public static class MeasureStringProperty extends MeasureProperty<String>{
		public MeasureStringProperty(Properties property, IValue idVal,
				IValue valVal, ICallbackEnv fpa) {
			super(property, idVal, valVal, fpa);
		}

		@Override
		String parseVal(IValue v) {
			return ComputedProperties.ComputedStringProperty.convertValueS(v);
		}
	}
	
	public static class MeasureBooleanProperty extends MeasureProperty<Boolean>{
		public MeasureBooleanProperty(Properties property, IValue idVal,
				IValue valVal, ICallbackEnv fpa) {
			super(property, idVal, valVal, fpa);
		}

		@Override
		Boolean parseVal(IValue v) {
			return ComputedProperties.ComputedBooleanProperty.convertValueS(v);
		}
	}
	
	public static class MeasureIntegerProperty extends MeasureProperty<Integer>{
		public MeasureIntegerProperty(Properties property, IValue idVal,
				IValue valVal, ICallbackEnv fpa) {
			super(property, idVal, valVal, fpa);
		}

		@Override
		Integer parseVal(IValue v) {
			return ComputedProperties.ComputedIntegerProperty.convertValueS(v);
		}
	}
	
	public static class MeasureColorProperty extends MeasureProperty<Integer>{
		public MeasureColorProperty(Properties property, IValue idVal,
				IValue valVal, ICallbackEnv fpa) {
			super(property, idVal, valVal, fpa);
		}

		@Override
		Integer parseVal(IValue v) {
			return ComputedProperties.ComputedColorProperty.convertValueS(v);
		}
	}
	
}
