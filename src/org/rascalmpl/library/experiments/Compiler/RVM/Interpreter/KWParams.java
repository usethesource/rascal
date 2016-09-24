package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

/**
 * Builder-like class to create IMap with keyword parameters
 * 
 * Typical usage: 
 * 		new KWParams(vf).add("key1", vf.integer(1)).add("key2", vf.string("abc")).build();
 */
public class KWParams {
	private Map<String, IValue> kwargs;
	private IValueFactory vf;

	/**
	 * Create a keyword parameter builder
	 * @param vf ValueFactory to be used
	 */
	public KWParams(IValueFactory vf){
		this.vf = vf;
		kwargs = new HashMap<>();
	}

	/**
	 * Add a (key, value) pair to the keyword parameters
	 * @param key	Name of keyword parameter
	 * @param val	Value of keyword parameter
	 * @return This KWParams, with the pair (key, val) added
	 */
	public KWParams add(String key, IValue val){
	  kwargs.put(key,  val);
	  return this;
	}

	public KWParams add(String key, boolean b){
	  kwargs.put(key, vf.bool(b));
	  return this;
	}
	public KWParams add(String key, int n){
	  kwargs.put(key, vf.integer(n));
	  return this;
	}

	public KWParams add(String key, float f){
	  kwargs.put(key, vf.real(f));
	  return this;
	}

	public KWParams add(String key, double d){
	  kwargs.put(key, vf.real(d));
	  return this;
	}
	public KWParams add(String key, String s){
      kwargs.put(key, vf.string(s));
      return this;
    }
	
	public KWParams add(IMap kwmap){
	  for(IValue key : kwmap){
	    if(!key.getType().isString()){
	      throw new RuntimeException("Key in keywerd parameter map should be a string");
	    }
	    kwargs.put(((IString) key).getValue(), kwmap.get(key));
	  }
	  return this;
	}
	
	/**
	 * @return this KWParams as Map
	 */
	public Map<String, IValue> build(){
		return kwargs;
	}
}
