package org.rascalmpl.library.experiments.CoreRascal.RVM.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Function;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Primitive;
import org.rascalmpl.library.experiments.CoreRascal.RVM.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class CountDown_a {
	public static void main(String[] args) {
		
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		
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
							.loadcon(0)
							.callprim(Primitive.greater_num_num)
							.jmptrue("BODY")
							.loadcon(0)
							.ret1()
							.label("BODY")
							.loadloc(0)
							.yield1()
							.loadloc(0)
							.loadcon(1)
							.callprim(Primitive.substraction_num_num)
							.storeloc(0)
							.jmp("LOOP")));
		
		/*
		 * c = create(g);
		 * c.init(5);
		 * 
		 * count = 0;
		 * while(hasNext(c)) {
		 * 		count = count + c.next();
		 * }
		 */
		/*
		 * result: 5 + 4 + 3 + 2 + 1 = 15
		 */
		rvm.declare(new Function("main", 0, 0, 2, 6,
					new CodeBlock(vf)
						.create("g")
						.storeloc(0)
						.loadcon(5)
						.loadloc(0)
						.init()
						.loadcon(0)
						.storeloc(1)
						.label("LOOP")
						.loadloc(0)
						.hasNext()
						.jmptrue("BODY")
						.halt()
						.label("BODY")
						.loadloc(1)
						.loadloc(0)
						.next0()
						.callprim(Primitive.addition_num_num)
						.storeloc(1)
						.jmp("LOOP")));
	
		rvm.executeProgram("main", new IValue[] {});
	}

}
