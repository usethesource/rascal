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
		IValueFactory v = rvm.vf;
		
		rvm.declareConst("0", v.integer(0));
		rvm.declareConst("1", v.integer(1));
		rvm.declareConst("2", v.integer(2));
		rvm.declareConst("3", v.integer(3));
		rvm.declareConst("35", v.integer(35));
		//int fib(int n) = (n == 0) ? 0 : (n == 1) ? 1 : (fib(n-1) + fib(n-2));
		
		rvm.declare(new Function("fib", 1, 1, 1, 6,
				new CodeBlock().
					loadloc(0).
					loadcon("0").
					callprim(Primitive.equal_num_num).
					jmpfalse("L").
					loadcon("0").
					ret().
					label("L").
					loadloc(0).
					loadcon("1").
					callprim(Primitive.equal_num_num).
					jmpfalse("M").
					loadcon("1").
					ret().
					label("M").
					loadloc(0).
					loadcon("1").
					callprim(Primitive.substraction_num_num).
					call("fib").
					loadloc(0).
					loadcon("2").
					callprim(Primitive.substraction_num_num).
					call("fib").
					callprim(Primitive.addition_num_num).
					ret()));
					
		rvm.declare(new Function("main", 0, 0, 0, 6,
					new CodeBlock().
						loadcon("35").
						call("fib").
						halt()));
		rvm.setDebug(false);
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
