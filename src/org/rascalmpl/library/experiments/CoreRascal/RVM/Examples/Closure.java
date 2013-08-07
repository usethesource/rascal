package org.rascalmpl.library.experiments.CoreRascal.RVM.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Function;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Primitive;
import org.rascalmpl.library.experiments.CoreRascal.RVM.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class Closure {

	public static void main(String[] args) {
		
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		
		rvm.declareConst("TRUE", vf.bool(true));
		rvm.declareConst("FALSE", vf.bool(false));
		
		rvm.declareConst("LST", vf.list(vf.integer(0), vf.integer(1), vf.integer(2)));
		
		rvm.declareConst("0", vf.integer(0));
		rvm.declareConst("1", vf.integer(1));
		rvm.declareConst("2", vf.integer(2));
		rvm.declareConst("3", vf.integer(3));
		rvm.declareConst("4", vf.integer(4));
		rvm.declareConst("ZERO", vf.string("ZERO"));
		rvm.declareConst("ONE", vf.string("ONE"));
		rvm.declareConst("TWO", vf.string("TWO"));
		
		
		rvm.declare(new Function("g", 1, 0, 0, 6,
				new CodeBlock(vf).
					loadvar(1,0).
					ret1()
		));
		
		rvm.declare(new Function("f", 0, 0, 1, 6,
				new CodeBlock(vf).
					loadcon("1").
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
