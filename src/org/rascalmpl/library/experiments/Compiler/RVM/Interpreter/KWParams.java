package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

/**
 * Builder-like class to create IMap with keyword parameters
 * 
 * Typical usage: 
 * 		new KWParams(vf).add("key1", vf.integer(1)).add("key2", vf.string("abc")).build();
 */
public class KWParams {
	private IMapWriter kwargs;
	private IValueFactory vf;

	/**
	 * Create a keyword parameter builder
	 * @param vf ValueFactory to be used
	 */
	public KWParams(IValueFactory vf){
		this.vf = vf;
		kwargs = vf.mapWriter();
	}

	/**
	 * Add a (key, value) pair to the keyword parameters
	 * @param key	Name of keyword parameter
	 * @param val	Value of keyword parameter
	 * @return This KWParams, with the pair key, val) added
	 */
	public KWParams add(String key, IValue val){
		kwargs.put(vf.string(key),  val);
		return this;
	}
	
	/**
	 * @return this KWParams as IMap
	 */
	public IMap build(){
		return kwargs.done();
	}
}
