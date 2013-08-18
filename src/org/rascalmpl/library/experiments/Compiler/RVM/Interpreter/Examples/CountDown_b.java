package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Primitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class CountDown_b {
	
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
		
		rvm.declare(new Function("g", 1, 1, 1, 6,
					new CodeBlock(vf)
							.LABEL("LOOP")
							.LOADLOC(0)
							.LOADCON(0)
							.CALLPRIM(Primitive.greater_num_num, 2)
							.JMPTRUE("BODY")
							.LOADCON(0)
							.RETURN1()
							.LABEL("BODY")
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
		 * h() {
		 * n = 9 + 1;
		 * c = create(g);
		 * c.init(n);
		 * return c;
		 * }
		 */
		
		rvm.declare(new Function("h", 2, 0, 2, 6, 
					new CodeBlock(vf)
						.LOADCON(9)
						.LOADCON(1)
						.CALLPRIM(Primitive.addition_num_num, 2)
						.STORELOC(0)
						.POP()
						.CREATE("g",0)
						.STORELOC(1)
						.POP()
						.LOADLOC(0)
						.LOADLOC(1)
						.INIT(1)
						.POP()
						.LOADLOC(1)
						.RETURN1()));
		
		/*
		 * c1 = h();
		 * c2 = h();
		 * 
		 * count = 0;
		 * while(hasNext(c1)) {
		 * 		count = (c1.resume() + c2.resume()) + count;
		 * }
		 */
		/*
		 * result: 0
		 */
		rvm.declare(new Function("main", 3, 1, 4, 6,
					new CodeBlock(vf)
						.CALL("h")
						.STORELOC(1)
						.POP()
						.CALL("h")
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
						.CALLPRIM(Primitive.addition_num_num, 2)
						.LOADLOC(3)
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
