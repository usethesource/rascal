package org.rascalmpl.library.experiments.Compiler.Examples;

import java.io.IOException;

import org.rascalmpl.values.ValueFactoryFactory;

public class FacUse  {
	
	public static void main(String[] args) throws IOException {
		System.out.println(new Fac(ValueFactoryFactory.getValueFactory()).fac(6));
	}
}
