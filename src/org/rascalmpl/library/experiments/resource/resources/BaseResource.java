package org.rascalmpl.library.experiments.resource.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.library.experiments.resource.IResource;

public abstract class BaseResource implements IResource {

	@Override
	public List<String> getPathItems() {
		return new ArrayList<String>();
	}

	@Override
	public List<String> getQueryParameters() {
		return new ArrayList<String>();
	}

	@Override
	public List<Type> getQueryParameterTypes() {
		return new ArrayList<Type>();
	}

	@Override
	public List<String> getOptionalQueryParameters() {
		return new ArrayList<String>();
	}

	@Override
	public List<Type> getOptionalQueryParameterTypes() {
		return new ArrayList<Type>();
	}

	protected Type makeOptionalParameterType(Type t) {
		TypeFactory tf = TypeFactory.getInstance();
		TypeStore ts = new TypeStore();

		Type paramType = tf.parameterType("T");
		Type adtType = tf.abstractDataType(ts, "Option", paramType);
		Map<Type,Type> bindings = new HashMap<Type,Type>();
		bindings.put(paramType, t);
		adtType = adtType.instantiate(bindings);

		return adtType;
	}
}
