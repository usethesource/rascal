package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.values.ValueFactoryFactory;


public class Fac {
		
	public static void main(String[] args) {
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		
		rvm.declare(new Function("fac", 1, 1, 1, 6, 
				new CodeBlock(vf).
					LOADLOC(0).
					LOADCON(1).
					CALLPRIM(RascalPrimitive.equals_num_num, 2).
					JMPFALSE("L").
					LOADCON(1).
					RETURN1().
					LABEL("L").
					LOADLOC(0).
					LOADLOC(0).
					LOADCON(1).
					CALLPRIM(RascalPrimitive.subtraction_num_num, 2).
					CALL("fac").
					CALLPRIM(RascalPrimitive.product_num_num, 2).
					RETURN1()));
		
		rvm.declare(new Function("main", 2, 1, 1, 7,
				new CodeBlock(vf).
					LOADCON(4).
					CALL("fac").
					HALT()));
		
		rvm.declare(new Function("main_repeat", 3, 0, 2, 20,
				new CodeBlock(vf).
					LOADCON(10).
					STORELOC(0). // n
					POP().
					LOADCON(10).
					STORELOC(1). // cnt
					POP().
					LABEL("L").
					LOADLOC(1). // cnt
					LOADCON(0).
					CALLPRIM(RascalPrimitive.greater_num_num, 2).
					JMPTRUE("M").
					HALT().
					LABEL("M").
					LOADLOC(0).
					CALL( "fac").
					POP().
					LOADLOC(1).
					LOADCON(1).
					CALLPRIM(RascalPrimitive.subtraction_num_num, 2).
					STORELOC(1).
					POP().
					JMP("L")));
		
		rvm.declare(new Function("#module_init", 0, 0, 1, 6, 
				new CodeBlock(vf)
					.LOADLOC(0)
					.CALL("main")
					.RETURN1()
					.HALT()));
		
		long total = 0;
		int times = 20;
		
		for(int i = 0; i < times; i++){
			long start = System.currentTimeMillis();
			
			rvm.executeProgram("main", new IValue[] {});
			long now = System.currentTimeMillis();
			total += now - start;
			
		}
		System.out.println("RVM: average elapsed time in msecs:" + total/times);
	}

}
