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
		IValueFactory vf = rvm.vf;
		
		rvm.declareConst("TRUE", vf.bool(true));
		rvm.declareConst("FALSE", vf.bool(false));
		
		rvm.declareConst("LST", vf.list(vf.integer(0), vf.integer(1), vf.integer(2)));
		
		rvm.declareConst("0", vf.integer(0));
		rvm.declareConst("1", vf.integer(1));
		rvm.declareConst("2", vf.integer(2));
		rvm.declareConst("3", vf.integer(3));
		rvm.declareConst("4", vf.integer(4));
		
		rvm.declareConst("1.5", vf.real(1.5));
		rvm.declareConst("1r5", vf.rational(1, 5));
		
		rvm.declareConst("ZERO", vf.string("ZERO"));
		rvm.declareConst("ONE", vf.string("ONE"));
		rvm.declareConst("TWO", vf.string("TWO"));
		
		rvm.declareConst("L1", vf.list(vf.integer(2), vf.integer(4)));
		rvm.declareConst("L2", vf.list(vf.integer(3), vf.integer(4)));
		
		rvm.declareConst("S1", vf.set(vf.integer(2), vf.integer(4)));
		rvm.declareConst("S2", vf.set(vf.integer(3), vf.integer(4)));
		
		rvm.declareConst("T1", vf.tuple(vf.integer(2), vf.integer(4)));
		rvm.declareConst("T2", vf.tuple(vf.integer(3), vf.integer(4)));
		
		
		rvm.declareConst("MSG", vf.string("A Message: @0!"));
		
		rvm.declare(new Function("main", 0, 0, 0, 6,
					new CodeBlock(vf).
						loadcon("T1").
						loadcon("3").
						print("MSG").
						halt()));
	
		rvm.executeProgram("main", new IValue[] {});
	}

}
