package org.rascalmpl.library.experiments.CoreRascal.RVM.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Function;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Primitive;
import org.rascalmpl.library.experiments.CoreRascal.RVM.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class CountDown_c {
	public static void main(String[] args) {
		
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		
		/*
		 * g (n,ref) 
		 * { 
		 * 		while(n > 1) {
		 * 			ref = ref + 1; 
		 * 			yield n; 
		 * 			n = n - 1; 
		 * 		}; 
		 * 		return 0; 
		 * }
		 */
		rvm.declare(new Function("g", 0, 2, 2, 6,
					new CodeBlock(vf)
							.label("LOOP")
							.loadloc(0)
							.loadcon(0)
							.callprim(Primitive.greater_num_num)
							.jmptrue("BODY")
							.loadcon(0)
							.ret1()
							.label("BODY")
							// call-by-reference check
							.loadcon(1)
							.loadLocRef(1)
							.callprim(Primitive.addition_num_num)
							.storeLocRef(1)
							
							.loadloc(0)
							.yield1()
							.loadloc(0)
							.loadcon(1)
							.callprim(Primitive.substraction_num_num)
							.storeloc(0)
							.jmp("LOOP")));
		
		/*
		 * c = create(g);   // N0
		 * 
		 * ref = 0;         // N1
		 * c.init(5, ref);  // call-by-reference check
		 * 
		 * count = 0;       // N2
		 * while(hasNext(c)) {
		 * 		ref = ref + 2;
		 * 		count = count + c.next();
		 * }
		 */
		/*
		 * result: 
		 * 			-
		 * 			5 + 12 = 17
		 *          15
		 */
		rvm.declare(new Function("main", 0, 0, 3, 6,
					new CodeBlock(vf)
						.create("g")
						.storeloc(0)
						.loadcon(0)
						.storeloc(1)
						.loadcon(5)
						// call-by-reference check
						.loadConRef(1)
						.loadloc(0)
						.init()
						.loadcon(0)
						.storeloc(2)
						.label("LOOP")
						.loadloc(0)
						.hasNext()
						.jmptrue("BODY")
						.halt()
						.label("BODY")
						.loadcon(2)
						.loadloc(1)
						.callprim(Primitive.addition_num_num)
						.storeloc(1)
						.loadloc(2)
						.loadloc(0)
						.next0()
						.callprim(Primitive.addition_num_num)
						.storeloc(2)
						.jmp("LOOP")));
	
		rvm.executeProgram("main", new IValue[] {});
	}

}
