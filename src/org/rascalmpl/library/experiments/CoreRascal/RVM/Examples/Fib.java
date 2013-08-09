package org.rascalmpl.library.experiments.CoreRascal.RVM.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Function;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Primitive;
import org.rascalmpl.library.experiments.CoreRascal.RVM.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class Fib {
	
	static int fib(int n){
		return (n == 0) ? 0 : (n == 1) ? 1 : (fib(n-1) + fib(n-2));
	}

public static void main(String[] args) {
		
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		
		//int fib(int n) = (n == 0) ? 0 : (n == 1) ? 1 : (fib(n-1) + fib(n-2));
		
		rvm.declare(new Function("fib", 1, 1, 1, 6,
				new CodeBlock(vf).
					LOADLOC(0).
					LOADCON(0).
					CALLPRIM(Primitive.equal_num_num).
					JMPFALSE("L").
					LOADCON(0).
					RETURN1().
					LABEL("L").
					LOADLOC(0).
					LOADCON(1).
					CALLPRIM(Primitive.equal_num_num).
					JMPFALSE("M").
					LOADCON(1).
					RETURN1().
					LABEL("M").
					LOADLOC(0).
					LOADCON(1).
					CALLPRIM(Primitive.substraction_num_num).
					CALL("fib").
					LOADLOC(0).
					LOADCON(2).
					CALLPRIM(Primitive.substraction_num_num).
					CALL("fib").
					CALLPRIM(Primitive.addition_num_num).
					RETURN1()));
					
		rvm.declare(new Function("main", 0, 0, 0, 6,
					new CodeBlock(vf).
						LOADCON("35").
						CALL("fib").
						HALT()));
		
		long start = System.currentTimeMillis();
		IValue val = (IValue) rvm.executeProgram("main", new IValue[] {});
		long now = System.currentTimeMillis();
		System.out.println("Result: " + val);
		System.out.println("RVM: average elapsed time in msecs:" + (now - start));
		
		start = System.currentTimeMillis();
		int r = fib(35);
		System.out.println("Result: " + r);
		now = System.currentTimeMillis();
		System.out.println("JAVA: average elapsed time in msecs:" + (now - start));
	}

}
