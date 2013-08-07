package org.rascalmpl.library.experiments.CoreRascal.RVM.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Function;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Primitive;
import org.rascalmpl.library.experiments.CoreRascal.RVM.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class Test {

	public static void main(String[] args) {
		
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory v = rvm.vf;
		
		rvm.declareConst("TRUE", v.bool(true));
		rvm.declareConst("FALSE", v.bool(false));
		
		rvm.declareConst("LST", v.list(v.integer(0), v.integer(1), v.integer(2)));
		
		rvm.declareConst("0", v.integer(0));
		rvm.declareConst("1", v.integer(1));
		rvm.declareConst("2", v.integer(2));
		rvm.declareConst("3", v.integer(3));
		rvm.declareConst("4", v.integer(4));
		
		rvm.declareConst("1.5", v.real(1.5));
		rvm.declareConst("1r5", v.rational(1, 5));
		
		rvm.declareConst("ZERO", v.string("ZERO"));
		rvm.declareConst("ONE", v.string("ONE"));
		rvm.declareConst("TWO", v.string("TWO"));
		
		rvm.declareConst("L1", v.list(v.integer(2), v.integer(4)));
		rvm.declareConst("L2", v.list(v.integer(3), v.integer(4)));
		
		rvm.declareConst("S1", v.set(v.integer(2), v.integer(4)));
		rvm.declareConst("S2", v.set(v.integer(3), v.integer(4)));
		
		rvm.declareConst("T1", v.tuple(v.integer(2), v.integer(4)));
		rvm.declareConst("T2", v.tuple(v.integer(3), v.integer(4)));
		
		
		rvm.declareConst("MSG", v.string("A Message: @0!"));
		
		rvm.declare(new Function("main", 0, 0, 0, 6,
					new CodeBlock().
						loadcon("T1").
						loadcon("3").
						print("MSG").
						halt()));
	
		rvm.executeProgram("main", new IValue[] {});
	}

}
