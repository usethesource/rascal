package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class Closure {

	public static void main(String[] args) {
		
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		TypeFactory tf = TypeFactory.getInstance();
		/* f() {
		 * 		n = 1;
		 * 		g() { 
		 * 			return n; 
		 * 		}
		 * 		return g;
		 * }
		 */
		rvm.declare(new Function("g", tf.valueType(), "f", 0, 0, 6,
				new CodeBlock(vf).
					LOADVAR("f",0).          // <<-
					RETURN1(1)
		));
		
		rvm.declare(new Function("f", tf.valueType(), null, 0, 1, 6,
				new CodeBlock(vf).
					LOADCON(1).
					STORELOC(0).
					LOADNESTEDFUN("g", "f"). // <<-
					RETURN1(1)
		));
		
		rvm.declare(new Function("main", tf.valueType(), null, 1, 1, 6,
					new CodeBlock(vf).
						CALL("f", 0).
						CALLDYN(1).
						RETURN1(1).
						HALT()));
	
		rvm.declare(new Function("#module_init", tf.valueType(), null, 1, 1, 6, 
				new CodeBlock(vf)
					.LOADLOC(0)
					.CALL("main", 1)
					.HALT()));

		rvm.executeProgram("main", new IValue[] {});
	}

}
