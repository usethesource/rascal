package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class CountDown_a {
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
							.CALLPRIM(RascalPrimitive.num_greater_num,2)
							.JMPTRUE("BODY")
							.LOADCON(0)
							.RETURN1(1)
							.LABEL("BODY")
							.LOADLOC(0)
							.YIELD1(1)
							.POP()        // added pop with respect to the new NEXT0's default bahviour on the stack
							.LOADLOC(0)
							.LOADCON(1)
							.CALLPRIM(RascalPrimitive.num_subtract_num,2)
							.STORELOC(0)
							.POP()       // added pop with respect to the new STORELOC's default bahviour on the stack
							.JMP("LOOP")));
		
		/*
		 * c = create(g);
		 * c = init(c,5);
		 * 
		 * count = 0;
		 * while(hasNext(c)) {
		 * 		count = count + next(c);
		 * }
		 * 
		 * return count;
		 */
		/*
		 * result: 5 + 4 + 3 + 2 + 1 = 15
		 */
		rvm.declare(new Function("main", tf.valueType(), null, 1, 3, 6,
					new CodeBlock(vf)
						.CREATE("g",0)
						.STORELOC(1)
						.LOADCON(5)
						.LOADLOC(1)
						.INIT(1)
						.STORELOC(1)
						.POP()
						.POP()      // added pop with respect to the new INIT's default bahviour on the stack
						.LOADCON(0)
						.STORELOC(2)
						.POP()      // added pop with respect to the new STORELOC's default bahviour on the stack
						.LABEL("LOOP")
						.LOADLOC(1)
						.HASNEXT()
						.JMPTRUE("BODY")
						.LOADLOC(2)
						.RETURN1(1)
						.LABEL("BODY")
						.LOADLOC(2)
						.LOADLOC(1)
						.NEXT0()
						.CALLPRIM(RascalPrimitive.num_add_num,2)
						.STORELOC(2)
						.POP()     // added pop with respect to the new STORELOC's default bahviour on the stack
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
