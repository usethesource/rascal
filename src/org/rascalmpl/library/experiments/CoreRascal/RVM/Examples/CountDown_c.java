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
							.LABEL("LOOP")
							.LOADLOC(0)
							.LOADCON(0)
							.CALLPRIM(Primitive.greater_num_num)
							.JMPTRUE("BODY")
							.LOADCON(0)
							.RETURN1()
							.LABEL("BODY")
							// call-by-reference check
							.LOADCON(1)
							.LOADLOCREF(1)
							.CALLPRIM(Primitive.addition_num_num)
							.STORELOCREF(1)
							
							.LOADLOC(0)
							.YIELD1()
							.LOADLOC(0)
							.LOADCON(1)
							.CALLPRIM(Primitive.substraction_num_num)
							.STORELOC(0)
							.JMP("LOOP")));
		
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
						.CREATE("g")
						.STORELOC(0)
						.LOADCON(0)
						.STORELOC(1)
						.LOADCON(5)
						// call-by-reference check
						.LOADCONREF(1)
						.LOADLOC(0)
						.INIT()
						.LOADCON(0)
						.STORELOC(2)
						.LABEL("LOOP")
						.LOADLOC(0)
						.HASNEXT()
						.JMPTRUE("BODY")
						.HALT()
						.LABEL("BODY")
						.LOADCON(2)
						.LOADLOC(1)
						.CALLPRIM(Primitive.addition_num_num)
						.STORELOC(1)
						.LOADLOC(2)
						.LOADLOC(0)
						.NEXT0()
						.CALLPRIM(Primitive.addition_num_num)
						.STORELOC(2)
						.JMP("LOOP")));
	
		rvm.executeProgram("main", new IValue[] {});
	}

}
