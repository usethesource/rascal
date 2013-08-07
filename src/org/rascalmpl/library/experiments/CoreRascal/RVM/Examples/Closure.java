package org.rascalmpl.library.experiments.CoreRascal.RVM.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Function;
import org.rascalmpl.library.experiments.CoreRascal.RVM.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class Closure {

	public static void main(String[] args) {
		
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		
		rvm.declare(new Function("g", 1, 0, 0, 6,
				new CodeBlock(vf).
					loadvar(1,0).
					ret1()
		));
		
		rvm.declare(new Function("f", 0, 0, 1, 6,
				new CodeBlock(vf).
					loadcon(1).
					storeloc(0).
					loadfun("g").
					ret1()
		));
		
		rvm.declare(new Function("main", 0, 0, 0, 6,
					new CodeBlock(vf).
						call("f").
						calldyn().
						halt()));
	
		rvm.executeProgram("main", new IValue[] {});
	}

}
