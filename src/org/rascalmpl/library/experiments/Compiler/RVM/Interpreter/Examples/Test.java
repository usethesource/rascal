package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class Test {

	public static void main(String[] args) {
		
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		
		rvm.declare(new Function("main", 1, 1, 6,
					new CodeBlock(vf).
						LOADCON(3).
						LOADCON("abc").
						PRINTLN(1).
						HALT()));
	
		rvm.declare(new Function("#module_init", 0, 1, 6, 
				new CodeBlock(vf)
					.LOADLOC(0)
					.CALL("main")
					.RETURN1()
					.HALT()));
		
		rvm.executeProgram("main", "#module_init", new IValue[] {});
	}

}
