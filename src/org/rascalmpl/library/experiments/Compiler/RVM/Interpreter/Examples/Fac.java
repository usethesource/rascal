package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.values.ValueFactoryFactory;


public class Fac {
		
	public static void main(String[] args) {
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		TypeFactory tf = TypeFactory.getInstance();
		rvm.declare(new Function("fac", tf.valueType(), null, 1, 1, 6, 
				new CodeBlock(vf).
					LOADLOC(0).
					LOADCON(1).
					CALLPRIM(RascalPrimitive.equal, 2).
					JMPFALSE("L").
					LOADCON(1).
					RETURN1(1).
					LABEL("L").
					LOADLOC(0).
					LOADLOC(0).
					LOADCON(1).
					CALLPRIM(RascalPrimitive.num_subtract_num, 2).
					CALL("fac", 1).
					CALLPRIM(RascalPrimitive.num_product_num, 2).
					RETURN1(1)));
		
		rvm.declare(new Function("main", tf.valueType(), null, 1, 1, 7,
				new CodeBlock(vf).
					LOADCON(4).
					CALL("fac", 1).
					HALT()));
		
		rvm.declare(new Function("main_repeat", tf.valueType(), null, 0, 2, 20,
				new CodeBlock(vf).
					LOADCON(10).
					STORELOC(0). // n
					POP().
					LOADCON(10).
					STORELOC(1). // cnt
					POP().
					LABEL("L").
					LOADLOC(1). // cnt
					LOADCON(0).
					CALLPRIM(RascalPrimitive.num_greater_num, 2).
					JMPTRUE("M").
					HALT().
					LABEL("M").
					LOADLOC(0).
					CALL( "fac", 1).
					POP().
					LOADLOC(1).
					LOADCON(1).
					CALLPRIM(RascalPrimitive.num_subtract_num, 2).
					STORELOC(1).
					POP().
					JMP("L")));
		
		rvm.declare(new Function("#module_init", tf.valueType(), null, 1, 1, 6, 
				new CodeBlock(vf)
					.LOADLOC(0)
					.CALL("main", 1)
					.RETURN1(1)
					.HALT()));
		
		long total = 0;
		int times = 20;
		
		for(int i = 0; i < times; i++){
			long start = System.currentTimeMillis();
			
			rvm.executeProgram("main",  new IValue[] {});
			long now = System.currentTimeMillis();
			total += now - start;
			
		}
		System.out.println("RVM: average elapsed time in msecs:" + total/times);
	}

}
