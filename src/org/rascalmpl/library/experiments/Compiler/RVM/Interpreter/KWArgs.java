package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.HashMap;
import java.util.Map;

import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

/**
 * Builder-like class to create IMap with keyword parameters
 * 
 * Typical usage: 
 * 		new KWParams(vf).add("key1", vf.integer(1)).add("key2", vf.string("abc")).build();
 */
public class KWArgs {
	protected Map<String, IValue> kwArgs;
	protected IValueFactory vf;

	/**
	 * Create a keyword parameter builder
	 * @param vf ValueFactory to be used
	 */
	public KWArgs(IValueFactory vf){
		this.vf = vf;
		kwArgs = new HashMap<>();
	}

	/**
	 * Add a (key, value) pair to the keyword parameters
	 * @param key	Name of keyword parameter
	 * @param val	Value of keyword parameter
	 * @return This KWParams, with the pair (key, val) added
	 */
	public KWArgs add(String key, IValue val){
	  kwArgs.put(key,  val);
	  return this;
	}

	public KWArgs add(String key, boolean b){
	  kwArgs.put(key, vf.bool(b));
	  return this;
	}
	public KWArgs add(String key, int n){
	  kwArgs.put(key, vf.integer(n));
	  return this;
	}

	public KWArgs add(String key, float f){
	  kwArgs.put(key, vf.real(f));
	  return this;
	}

	public KWArgs add(String key, double d){
	  kwArgs.put(key, vf.real(d));
	  return this;
	}
	public KWArgs add(String key, String s){
      kwArgs.put(key, vf.string(s));
      return this;
    }
	
	public KWArgs add(IMap kwmap){
	  for(IValue key : kwmap){
	    if(!key.getType().isString()){
	      throw new RuntimeException("Key in keywerd parameter map should be a string");
	    }
	    kwArgs.put(((IString) key).getValue(), kwmap.get(key));
	  }
	  return this;
	}
	
	/**
	 * @return this KWParams as Map
	 */
	public Map<String, IValue> build(){
		return kwArgs;
	}
}
