package org.rascalmpl.library.experiments.CoreRascal.RVM.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Function;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Primitive;
import org.rascalmpl.library.experiments.CoreRascal.RVM.RVM;
import org.rascalmpl.values.ValueFactoryFactory;


public class Fac {
		
	public static void main(String[] args) {
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		
		rvm.declare(new Function("fac", 1, 1, 1, 6, 
				new CodeBlock(vf).
					loadloc(0).
					loadcon(1).
					callprim(Primitive.equal_num_num).
					jmpfalse("L").
					loadcon(1).
					ret1().
					label("L").
					loadloc(0).
					loadloc(0).
					loadcon(1).
					callprim(Primitive.substraction_num_num).
					call("fac").
					callprim(Primitive.multiplication_num_num).
					ret1()));
		
		rvm.declare(new Function("main_fac", 0, 0, 0, 7,
				new CodeBlock(vf).
					loadcon(4).
					call("fac").
					halt()));
		
		rvm.declare(new Function("main_repeat", 0, 0, 2, 20,
				new CodeBlock(vf).
					loadcon(10).
					storeloc(0). // n
					loadcon(10).
					storeloc(1). // cnt
					label("L").
					loadloc(1). // cnt
					loadcon(0).
					callprim(Primitive.greater_num_num).
					jmptrue("M").
					halt().
					label("M").
					loadloc(0).
					call( "fac").
					pop().
					loadloc(1).
					loadcon(1).
					callprim(Primitive.substraction_num_num).
					storeloc(1).
					jmp("L")));
		
		long total = 0;
		int times = 20;
		rvm.setDebug(true);
		
		for(int i = 0; i < times; i++){
			long start = System.currentTimeMillis();
			
			rvm.executeProgram("main_fac", new IValue[] {});
			long now = System.currentTimeMillis();
			total += now - start;
			
		}
		System.out.println("RVM: average elapsed time in msecs:" + total/times);
	}

}
