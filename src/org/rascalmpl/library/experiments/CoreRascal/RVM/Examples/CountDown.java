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
							.LABEL("LOOP")
							.LOADLOC(0)
							.LOADCON(0)
							.CALLPRIM(Primitive.greater_num_num)
							.JMPTRUE("BODY")
							.LOADCON(0)
							.RETURN1()
							.LABEL("BODY")
							.LOADLOC(0)
							.YIELD1()
							.LOADLOC(0)
							.LOADCON(1)
							.CALLPRIM(Primitive.subtraction_num_num)
							.STORELOC(0)
							.JMP("LOOP")));
		
		/*
		 * c = create(g);
		 * c.init(5);
		 * c.next() * c.next() + c.next();
		 */
		/*
		 * result: 23
		 */
		rvm.declare(new Function("main", 0, 0, 1, 6,
					new CodeBlock(vf)
						.CREATE("g")
						.STORELOC(0)
						.LOADCON(5)
						.LOADLOC(0)
						.INIT()
						.LOADLOC(0)
						.NEXT0()
						.LOADLOC(0)
						.NEXT0()
						.CALLPRIM(Primitive.product_num_num)
						.LOADLOC(0)
						.NEXT0()
						.CALLPRIM(Primitive.addition_num_num)
						.HALT()));
	
		rvm.executeProgram("main", new IValue[] {});
	}

}
