package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.values.ValueFactoryFactory;


public class Do {
		
	public static void main(String[] args) {
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		TypeFactory tf = TypeFactory.getInstance();
		rvm.declare(new Function("square", tf.valueType(), null, 1, 1, 6, 
				new CodeBlock(vf).
					LOADLOC(0).
					LOADLOC(0).
					CALLPRIM(RascalPrimitive.num_product_num, 2).
					RETURN1(1)));
		
		rvm.declare(new Function("cube", tf.valueType(), null, 1, 1, 6, 
				new CodeBlock(vf).
					LOADLOC(0).
					LOADLOC(0).
					CALLPRIM(RascalPrimitive.num_product_num, 2).
					LOADLOC(0).
					CALLPRIM(RascalPrimitive.num_product_num, 2).
					RETURN1(1)));
		
		rvm.declare(new Function("do", tf.valueType(), null, 2, 2, 6, 
				new CodeBlock(vf).
					LOADLOC(1).
					LOADLOC(0).
					CALLDYN(1).
					RETURN1(1)));
		
		rvm.declare(new Function("main", tf.valueType(), null, 1, 1, 7,
				new CodeBlock(vf).
					LOADFUN("cube").
					LOADCON(4).
					CALL("do", 2).
					HALT()));
		
		rvm.declare(new Function("#module_init", tf.valueType(), null, 1, 1, 6, 
				new CodeBlock(vf)
					.LOADLOC(0)
					.CALL("main",1)
					.RETURN1(1)
					.HALT()));
		
		rvm.executeProgram("main", new IValue[] {});
	}

}
