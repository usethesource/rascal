package org.rascalmpl.library.experiments.CoreRascal.RVM.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Function;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Primitive;
import org.rascalmpl.library.experiments.CoreRascal.RVM.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class CountDown {
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
		rvm.declareConst("5", vf.integer(5));
		
		/*
		 * g (n) 
		 * { 
		 * 		while(n > 1) { 
		 * 			yield n; 
		 * 			n = n - 1; 
		 * 		}; 
		 * 		return 0; 
		 * }
		 */
		rvm.declare(new Function("g", 0, 1, 1, 6,
					new CodeBlock(vf)
							.label("LOOP")
							.loadloc(0)
							.loadcon("0")
							.callprim(Primitive.greater_num_num)
							.jmptrue("BODY")
							.loadcon("0")
							.ret1()
							.label("BODY")
							.loadloc(0)
							.yield1()
							.loadloc(0)
							.loadcon("1")
							.callprim(Primitive.substraction_num_num)
							.storeloc(0)
							.jmp("LOOP")));
		
		/*
		 * c = create(g);
		 * c.start(5) * c.next() + c.next();
		 */
		/*
		 * result: 23
		 */
		rvm.declare(new Function("main", 0, 0, 1, 6,
					new CodeBlock(vf)
						.create("g")
						.storeloc(0)
						.loadcon("5")
						.loadloc(0)
						.start()
						.loadloc(0)
						.next0()
						.callprim(Primitive.multiplication_num_num)
						.loadloc(0)
						.next0()
						.callprim(Primitive.addition_num_num)
						.halt()));
	
		rvm.executeProgram("main", new IValue[] {});
	}

}
