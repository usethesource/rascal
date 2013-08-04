package org.rascalmpl.library.experiments.CoreRascal.RVM.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Function;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Primitive;
import org.rascalmpl.library.experiments.CoreRascal.RVM.RVM;
import org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions.Instructions;
import org.rascalmpl.values.ValueFactoryFactory;

public class Fac {
		
	public static void main(String[] args) {
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		
		rvm.declareConst("ZERO", rvm.vf.integer(0));
		rvm.declareConst("ONE", rvm.vf.integer(1));
		rvm.declareConst("TEN", rvm.vf.integer(10));
		rvm.declareConst("THOUSAND", rvm.vf.integer(1000));
		rvm.declareConst("MANY", rvm.vf.integer(100000));
		
		rvm.declareRecursive("fac");
		
		rvm.declare(new Function("fac", 1, 1, 1, 6, 
				new Instructions().
					loadloc(0).
					loadcon("ONE").
					callprim(Primitive.equal_int_int).
					jmpfalse("L").
					loadcon("ONE").
					ret().
					label("L").
					loadloc(0).
					loadloc(0).
					loadcon("ONE").
					callprim(Primitive.substraction_int_int).
					call("fac").
					callprim(Primitive.multiplication_int_int).
					ret()));
		
		rvm.declare(new Function("main_fac", 0, 0, 0, 7,
				new Instructions().
					loadcon("THOUSAND").
					call("fac").
					halt()));
		
		rvm.declare(new Function("main_repeat", 0, 0, 2, 20,
				new Instructions().
					loadcon("TEN").
					storeloc(0). // n
					loadcon("MANY").
					storeloc(1). // cnt
					label("L").
					loadloc(1). // cnt
					loadcon("ZERO").
					callprim(Primitive.greater_int_int).
					jmptrue("M").
					halt().
					label("M").
					loadloc(0).
					call( "fac").
					pop().
					loadloc(1).
					loadcon("ONE").
					callprim(Primitive.substraction_int_int).
					storeloc(1).
					jmp("L")));
		
		for(int i = 0; i < 10000; i++){
			long start = System.currentTimeMillis();
			rvm.setDebug(false);
			rvm.executeProgram("main_repeat", new IValue[] {});
			long now = System.currentTimeMillis();
			System.out.println("RVM: elapsed time in msecs:" + (now - start));
		}
	}

}
