package org.rascalmpl.library.vis.util;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.FigureFactory;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;

public class RascalToJavaValueConverters {
	
	public interface Convert<T>{
		T convert(IValue val, PropertyManager pm, IFigureConstructionEnv env);
	}
	
	public static class ConvertBool implements Convert<Boolean>{
		public static ConvertBool instance = new ConvertBool();
		public Boolean convert(IValue val, PropertyManager pm, IFigureConstructionEnv env){
			return ((IBool) val).getValue();
		}
	}
	
	public static class ConvertInt implements Convert<Integer>{
		public static ConvertInt instance = new ConvertInt();
		public Integer convert(IValue val, PropertyManager pm, IFigureConstructionEnv env){
			return ((IInteger) val).intValue();
		}
	}
	

	public static class ConvertColor implements Convert<Integer>{
		public static ConvertColor instance = new ConvertColor();
		public Integer convert(IValue val, PropertyManager pm, IFigureConstructionEnv env){
			if(val instanceof IString){
				String name = ((IString)val).getValue().toLowerCase();
				if(FigureColorUtils.colorNames.containsKey(name)){
					val =  FigureColorUtils.colorNames.get(name);
				} else {
					throw new Error("No such color "+ name + "!");
				}

			} 
			return ((IInteger) val).intValue();
		}
	}
	
	public static class ConvertReal implements Convert<Double>{
		public static ConvertReal instance = new ConvertReal();
		public Double convert(IValue val, PropertyManager pm, IFigureConstructionEnv env){
			return (val instanceof IInteger) ? ((IInteger) val).intValue() : ((IReal) val).doubleValue();
		}
	}
	

	public static class ConvertStr implements Convert<String>{
		public static ConvertStr instance = new ConvertStr();
		public String convert(IValue val, PropertyManager pm, IFigureConstructionEnv env){
			return ((IString) val).getValue();
		}
	}
	

	public static class ConvertFig implements Convert<Figure>{
		public static ConvertFig instance = new ConvertFig();
		public Figure convert(IValue val, PropertyManager pm, IFigureConstructionEnv env){
			return FigureFactory.make(env, (IConstructor)val, null, null);
		}
	}
	
	public static class DoNotConvert implements Convert<IValue>{
		public static DoNotConvert instance = new DoNotConvert();
		public IValue convert(IValue val, PropertyManager pm, IFigureConstructionEnv env){
			return val;
		}
	}
	
	public static class ConvertNum implements Convert<Double>{
		public static ConvertNum instance = new ConvertNum();
		public Double convert(IValue val, PropertyManager pm,IFigureConstructionEnv env) {
			if(val instanceof IReal){
				return ConvertReal.instance.convert(val, pm, env);
			} else if (val instanceof IInteger){
				return (double)ConvertInt.instance.convert(val, pm, env);
			} else {
				throw new Error("Unkown number case in convertnum!");
			}
		}
	}
	
	

}
