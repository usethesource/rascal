package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class CountDown_c {
	public static void main(String[] args) {
		
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		TypeFactory tf = TypeFactory.getInstance();
		/*
		 * g (n,r) 
		 * { 
		 * 		while(n > 1) {
		 * 			deref r = deref r + 1; 
		 * 			yield n; 
		 * 			n = n - 1; 
		 * 		}; 
		 * 		return 0; 
		 * }
		 */
		rvm.declare(new Function("g", tf.valueType(), null, 2, 2, 6,
					new CodeBlock(vf)
							.LABEL("LOOP")
							.LOADLOC(0)
							.LOADCON(0)
							.CALLPRIM(RascalPrimitive.num_greater_num, 2)
							.JMPTRUE("BODY")
							.LOADCON(0)
							.RETURN1(1)
							.LABEL("BODY")
							// call-by-reference check
							.LOADCON(1)
							.LOADLOCDEREF(1)
							.CALLPRIM(RascalPrimitive.num_add_num, 2)
							.STORELOCDEREF(1)
							.POP()
							
							.LOADLOC(0)
							.YIELD1(1)
							.POP()
							.LOADLOC(0)
							.LOADCON(1)
							.CALLPRIM(RascalPrimitive.num_subtract_num, 2)
							.STORELOC(0)
							.POP()
							.JMP("LOOP")));
		
		/*
		 * c = create(g);   // N0
		 * 
		 * r = 0;         // N1
		 * c = init(c,5,ref r);  // call-by-reference check
		 * 
		 * count = 0;       // N2
		 * while(hasNext(c)) {
		 * 		r = r + 2;
		 * 		count = count + next(c);
		 * }
		 */
		/*
		 * result: 
		 * 			-
		 * 			5 + 12 = 17
		 *          15
		 */
		rvm.declare(new Function("main", tf.valueType(), null, 1, 4, 10,
					new CodeBlock(vf)
						.CREATE("g",0)
						.STORELOC(1)
						.POP()
						.LOADCON(0)
						.STORELOC(2)
						.POP()
						.LOADCON(5)
						// call-by-reference check
						.LOADLOCREF(2)
						.LOADLOC(1)
						.INIT(2)
						.STORELOC(1)
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
						.CALLPRIM(RascalPrimitive.num_add_num, 2)
						.STORELOC(2)
						.POP()
						.LOADLOC(3)
						.LOADLOC(1)
						.NEXT0()
						.CALLPRIM(RascalPrimitive.num_add_num, 2)
						.STORELOC(3)
						.POP()
						.JMP("LOOP")));
	
		rvm.declare(new Function("#module_init", tf.valueType(), null, 1, 1, 6, 
				new CodeBlock(vf)
					.LOADLOC(0)
					.CALL("main", 1)
					.RETURN1(1)
					.HALT()));

		rvm.executeProgram("main", new IValue[] {});
	}

}
