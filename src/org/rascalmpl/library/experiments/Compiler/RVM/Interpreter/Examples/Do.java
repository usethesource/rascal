package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Primitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.values.ValueFactoryFactory;


public class Do {
		
	public static void main(String[] args) {
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		
		rvm.declare(new Function("square", 1, 1, 1, 6, 
				new CodeBlock(vf).
					LOADLOC(0).
					LOADLOC(0).
					CALLPRIM(Primitive.product_num_num, 2).
					RETURN1()));
		
		rvm.declare(new Function("cube", 2, 1, 1, 6, 
				new CodeBlock(vf).
					LOADLOC(0).
					LOADLOC(0).
					CALLPRIM(Primitive.product_num_num, 2).
					LOADLOC(0).
					CALLPRIM(Primitive.product_num_num, 2).
					RETURN1()));
		
		rvm.declare(new Function("do", 3, 2, 2, 6, 
				new CodeBlock(vf).
					LOADLOC(1).
					LOADLOC(0).
					CALLDYN().
					RETURN1()));
		
		rvm.declare(new Function("main", 4, 1, 1, 7,
				new CodeBlock(vf).
					LOADFUN("cube").
					LOADCON(4).
					CALL("do").
					HALT()));
		
		rvm.declare(new Function("#module_init", 0, 0, 1, 6, 
				new CodeBlock(vf)
					.LOADLOC(0)
					.CALL("main")
					.RETURN1()
					.HALT()));
		
		rvm.executeProgram("main", new IValue[] {});
	}

}
