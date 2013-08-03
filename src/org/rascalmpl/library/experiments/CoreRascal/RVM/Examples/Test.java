package org.rascalmpl.library.experiments.CoreRascal.RVM.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Function;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Primitive;
import org.rascalmpl.library.experiments.CoreRascal.RVM.RVM;
import org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions.Instructions;

public class Test {

	public static void main(String[] args) {
		
		RVM rvm = new RVM();
		
		rvm.declareConst("TRUE", rvm.vf.bool(true));
		rvm.declareConst("FALSE", rvm.vf.bool(false));
		
		rvm.declareConst("ONE", rvm.vf.integer(1));
		rvm.declareConst("FOUR", rvm.vf.integer(4));
		
		rvm.declare(new Function("main_test", 0, 0, 1, 6,
					new Instructions().
						loadcon("FALSE").
						jmptrue("L").
						label("M").
						loadcon("FOUR").
						loadcon("ONE").
						callprim(Primitive.addition_int_int).
						halt().
						label("L").
						jmp("M").
						halt()));
	
		rvm.executeProgram("main_test", new IValue[] {});
	}

}
