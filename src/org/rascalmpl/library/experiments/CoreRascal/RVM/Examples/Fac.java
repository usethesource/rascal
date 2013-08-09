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
					LOADLOC(0).
					LOADCON(1).
					CALLPRIM(Primitive.equals_num_num).
					JMPFALSE("L").
					LOADCON(1).
					RETURN1().
					LABEL("L").
					LOADLOC(0).
					LOADLOC(0).
					LOADCON(1).
					CALLPRIM(Primitive.subtraction_num_num).
					CALL("fac").
					CALLPRIM(Primitive.product_num_num).
					RETURN1()));
		
		rvm.declare(new Function("main_fac", 0, 0, 0, 7,
				new CodeBlock(vf).
					LOADCON(4).
					CALL("fac").
					HALT()));
		
		rvm.declare(new Function("main_repeat", 0, 0, 2, 20,
				new CodeBlock(vf).
					LOADCON(10).
					STORELOC(0). // n
					LOADCON(10).
					STORELOC(1). // cnt
					LABEL("L").
					LOADLOC(1). // cnt
					LOADCON(0).
					CALLPRIM(Primitive.greater_num_num).
					JMPTRUE("M").
					HALT().
					LABEL("M").
					LOADLOC(0).
					CALL( "fac").
					POP().
					LOADLOC(1).
					LOADCON(1).
					CALLPRIM(Primitive.subtraction_num_num).
					STORELOC(1).
					JMP("L")));
		
		long total = 0;
		int times = 20;
		
		for(int i = 0; i < times; i++){
			long start = System.currentTimeMillis();
			
			rvm.executeProgram("main_fac", new IValue[] {});
			long now = System.currentTimeMillis();
			total += now - start;
			
		}
		System.out.println("RVM: average elapsed time in msecs:" + total/times);
	}

}
