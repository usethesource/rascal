package org.rascalmpl.library.experiments.Compiler.Examples;

import java.util.HashMap;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.KWParams;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class FacUse  {
	
	public static void main(String[] args) {
		int n = 20;

		IValueFactory vf = ValueFactoryFactory.getValueFactory();

		Fac myFac = new Fac(vf);

//		System.out.println(myFac.fac(vf.integer(n)));
//		System.out.println(myFac.d1(vf.integer(n)));
//		System.out.println(myFac.d2(vf.string("abc")));
//		System.out.println(myFac.getA());
//		System.out.println(myFac.getAs(vf.integer(3)));
		
		HashMap<String,IValue> kwArgValues = new HashMap<>();
		kwArgValues.put("kw", vf.integer(3));
		System.out.println(myFac.mulKW(vf.integer(3), new KWParams(vf).add("kw", vf.integer(3)).build()));
		
	
		//System.out.println(myFac.sizeAs(myFac.getAs(vf.integer(3))));

	}
}
