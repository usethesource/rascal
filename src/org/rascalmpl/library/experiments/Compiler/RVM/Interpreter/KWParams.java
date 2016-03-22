package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

public class KWParams {
	private IMapWriter kwargs;
	private IValueFactory vf;

	public KWParams(IValueFactory vf){
		this.vf = vf;
		kwargs = vf.mapWriter();
	}

	public KWParams add(String key, IValue val){
		kwargs.put(vf.string(key),  val);
		return this;
	}
	
	public IMap build(){
		return kwargs.done();
	}

}
