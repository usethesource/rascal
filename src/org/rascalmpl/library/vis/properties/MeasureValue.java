package org.rascalmpl.library.vis.properties;

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.Key;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.RascalToJavaValueConverters.Convert;

public class MeasureValue<PropType> extends PropertyValue<PropType> {

	PropertyValue<IValue> inner;
	PropertyValue<String> id;
	Key<PropType> key;
	Convert<PropType> convert;
	PropertyManager pm;
	IFigureConstructionEnv env;
	MeasureValue(Convert<PropType> convert, PropertyManager pm, IFigureConstructionEnv env,PropertyValue<String> id, PropertyValue<IValue> inner){
		this.inner = inner;
		this.convert = convert;
		this.id = id;
		this.env = env;
		this.pm = pm;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void registerMeasures(NameResolver resolver){
		System.out.printf("Resolver %s!\n",id.getValue());
		key = (Key<PropType>)resolver.resolve(id.getValue());
		key.registerValue(inner.getValue());
	}
	
	@Override
	public PropType getValue() {
		return convert.convert(key.scaleValue(inner.getValue()), pm, env);
	}

}
