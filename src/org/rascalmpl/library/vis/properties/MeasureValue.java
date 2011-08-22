package org.rascalmpl.library.vis.properties;

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.vis.util.Key;
import org.rascalmpl.library.vis.util.NameResolver;

public class MeasureValue<T> extends PropertyValue<T> {

	PropertyValue<IValue> inner;
	PropertyValue<String> id;
	Key<T> key;
	
	MeasureValue(PropertyValue<String> id, PropertyValue<IValue> inner){
		this.inner = inner;
		this.id = id;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void registerMeasures(NameResolver resolver){
		key = (Key<T>)resolver.resolveKey(id.getValue());
		key.registerValue(inner.getValue());
	}
	
	@Override
	public T getValue() {
		return key.scaleValue(inner.getValue());
	}

}
