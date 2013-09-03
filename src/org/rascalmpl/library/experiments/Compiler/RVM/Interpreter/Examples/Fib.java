package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class Fib {
	
	static int fib(int n){
		return (n == 0) ? 0 : (n == 1) ? 1 : (fib(n-1) + fib(n-2));
	}

public static void main(String[] args) {
		
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		
		//int fib(int n) = (n == 0) ? 0 : (n == 1) ? 1 : (fib(n-1) + fib(n-2));
		
		rvm.declare(new Function("fib", 1, 1, 6,
				new CodeBlock(vf).
					LOADLOC(0).
					LOADCON(0).
					CALLPRIM(RascalPrimitive.equal, 2).
					JMPFALSE("L").
					LOADCON(0).
					RETURN1().
					LABEL("L").
					LOADLOC(0).
					LOADCON(1).
					CALLPRIM(RascalPrimitive.equal, 2).
					JMPFALSE("M").
					LOADCON(1).
					RETURN1().
					LABEL("M").
					LOADLOC(0).
					LOADCON(1).
					CALLPRIM(RascalPrimitive.subtraction_num_num, 2).
					CALL("fib", 1).
					LOADLOC(0).
					LOADCON(2).
					CALLPRIM(RascalPrimitive.subtraction_num_num, 2).
					CALL("fib", 1).
					CALLPRIM(RascalPrimitive.addition_num_num, 2).
					RETURN1()));
					
		rvm.declare(new Function("main", 1, 1, 6,
					new CodeBlock(vf).
						LOADCON(10).
						CALL("fib", 1).
						HALT()));
		
		rvm.declare(new Function("#module_init", 0, 1, 6, 
				new CodeBlock(vf)
					.LOADLOC(0)
					.CALL("main", 1)
					.RETURN1()
					.HALT()));
		
		long start = System.currentTimeMillis();
		IValue val = (IValue) rvm.executeProgram("main", "#module_init", new IValue[] {});
		long now = System.currentTimeMillis();
		System.out.println("Result: " + val);
		System.out.println("RVM: average elapsed time in msecs:" + (now - start));
		
		start = System.currentTimeMillis();
		int r = fib(10);
		System.out.println("Result: " + r);
		now = System.currentTimeMillis();
		System.out.println("JAVA: average elapsed time in msecs:" + (now - start));
	}

}
