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
					LOADVAR(1,0).
					RETURN1()
		));
		
		rvm.declare(new Function("f", 0, 0, 1, 6,
				new CodeBlock(vf).
					LOADCON(1).
					STORELOC(0).
					LOADFUN("g").
					RETURN1()
		));
		
		rvm.declare(new Function("main", 0, 1, 1, 6,
					new CodeBlock(vf).
						CALL("f").
						CALLDYN().
						HALT()));
	
		rvm.declare(new Function("#module_init", 0, 0, 1, 6, 
				new CodeBlock(vf)
					.LOADLOC(0)
					.CALL("main")
					.RETURN1()
					.HALT()));

		rvm.executeProgram("main", new IValue[] {});
	}

}
