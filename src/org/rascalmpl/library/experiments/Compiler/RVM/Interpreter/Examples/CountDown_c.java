package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Primitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
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
		rvm.declare(new Function("g", 1, 2, 2, 6,
					new CodeBlock(vf)
							.LABEL("LOOP")
							.LOADLOC(0)
							.LOADCON(0)
							.CALLPRIM(Primitive.greater_num_num, 2)
							.JMPTRUE("BODY")
							.LOADCON(0)
							.RETURN1()
							.LABEL("BODY")
							// call-by-reference check
							.LOADCON(1)
							.LOADLOCREF(1)
							.CALLPRIM(Primitive.addition_num_num, 2)
							.STORELOCREF(1)
							.POP()
							
							.LOADLOC(0)
							.YIELD1()
							.POP()
							.LOADLOC(0)
							.LOADCON(1)
							.CALLPRIM(Primitive.subtraction_num_num, 2)
							.STORELOC(0)
							.POP()
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
		rvm.declare(new Function("main", 2, 1, 4, 10,
					new CodeBlock(vf)
						.CREATE("g",0)
						.STORELOC(1)
						.POP()
						.LOADCON(0)
						.STORELOC(2)
						.POP()
						.LOADCON(5)
						// call-by-reference check
						.LOADLOCASREF(2)
						.LOADLOC(1)
						.INIT(1)
						.POP()
						.LOADCON(0)
						.STORELOC(3)
						.POP()
						.LABEL("LOOP")
						.LOADLOC(1)
						.HASNEXT()
						.JMPTRUE("BODY")
						.HALT()
						.LABEL("BODY")
						.LOADCON(2)
						.LOADLOC(2)
						.CALLPRIM(Primitive.addition_num_num, 2)
						.STORELOC(2)
						.POP()
						.LOADLOC(3)
						.LOADLOC(1)
						.NEXT0()
						.CALLPRIM(Primitive.addition_num_num, 2)
						.STORELOC(3)
						.POP()
						.JMP("LOOP")));
	
		rvm.declare(new Function("#module_init", 0, 0, 1, 6, 
				new CodeBlock(vf)
					.LOADLOC(0)
					.CALL("main")
					.RETURN1()
					.HALT()));

		rvm.executeProgram("main", new IValue[] {});
	}

}
