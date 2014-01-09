package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class CountDown_b {
	
	public static void main(String[] args) {
		
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		TypeFactory tf = TypeFactory.getInstance();
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
		
		rvm.declare(new Function("g", tf.valueType(), null, 1, 1, 6,
					new CodeBlock(vf)
							.LABEL("LOOP")
							.LOADLOC(0)
							.LOADCON(0)
							.CALLPRIM(RascalPrimitive.num_greater_num, 2)
							.JMPTRUE("BODY")
							.LOADCON(0)
							.RETURN1(1)
							.LABEL("BODY")
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
		 * h() {
		 * n = 9 + 1;
		 * c = create(g);
		 * c = init(c,n);
		 * return c;
		 * }
		 */
		
		rvm.declare(new Function("h", tf.valueType(), null, 0, 2, 6, 
					new CodeBlock(vf)
						.LOADCON(9)
						.LOADCON(1)
						.CALLPRIM(RascalPrimitive.num_add_num, 2)
						.STORELOC(0)
						.POP()
						.CREATE("g",0)
						.STORELOC(1)
						.POP()
						.LOADLOC(0)
						.LOADLOC(1)
						.INIT(1)
						.STORELOC(1)
						.POP()
						.LOADLOC(1)
						.RETURN1(1)));
		
		/*
		 * c1 = h();
		 * c2 = h();
		 * 
		 * count = 0;
		 * while(hasNext(c1)) {
		 * 		count = (next(c1) + next(c2)) + count;
		 * }
		 */
		/*
		 * result: 110
		 */
		rvm.declare(new Function("main", tf.valueType(), null, 1, 4, 6,
					new CodeBlock(vf)
						.CALL("h", 0)
						.STORELOC(1)
						.POP()
						.CALL("h", 0)
						.STORELOC(2)
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
						.LOADLOC(1)
						.NEXT0()
						.LOADLOC(2)
						.NEXT0()
						.CALLPRIM(RascalPrimitive.num_add_num, 2)
						.LOADLOC(3)
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
