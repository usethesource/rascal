package org.rascalmpl.library.experiments.CoreRascal.RVM.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Function;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Primitive;
import org.rascalmpl.library.experiments.CoreRascal.RVM.RVM;
import org.rascalmpl.values.ValueFactoryFactory;


public class Do {
		
	public static void main(String[] args) {
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		
		rvm.declareConst("ZERO", rvm.vf.integer(0));
		rvm.declareConst("ONE", rvm.vf.integer(1));
		rvm.declareConst("FOUR", rvm.vf.integer(4));
		rvm.declareConst("TEN", rvm.vf.integer(10));
		rvm.declareConst("THOUSAND", rvm.vf.integer(1000));
		rvm.declareConst("MANY", rvm.vf.integer(100000));
		
		rvm.declare(new Function("square", 1, 1, 1, 6, 
				new CodeBlock(vf).
					loadloc(0).
					loadloc(0).
					callprim(Primitive.multiplication_num_num).
					ret1()));
		
		rvm.declare(new Function("cube", 1, 1, 1, 6, 
				new CodeBlock(vf).
					loadloc(0).
					loadloc(0).
					callprim(Primitive.multiplication_num_num).
					loadloc(0).
					callprim(Primitive.multiplication_num_num).
					ret1()));
		
		rvm.declare(new Function("do", 1, 2, 2, 6, 
				new CodeBlock(vf).
					loadloc(1).
					loadloc(0).
					calldyn().
					ret1()));
		
		rvm.declare(new Function("main", 0, 0, 0, 7,
				new CodeBlock(vf).
					loadfun("cube").
					loadcon("FOUR").
					call("do").
					halt()));
		
		rvm.setDebug(true);
		rvm.executeProgram("main", new IValue[] {});
	}

}
