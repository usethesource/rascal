package org.rascalmpl.library.experiments.CoreRascal.RVM.Examples;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Function;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Primitive;
import org.rascalmpl.library.experiments.CoreRascal.RVM.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class Test {

	public static void main(String[] args) {
		
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory v = rvm.vf;
		
		rvm.declareConst("TRUE", v.bool(true));
		rvm.declareConst("FALSE", v.bool(false));
		
		rvm.declareConst("LST", v.list(v.integer(0), v.integer(1), v.integer(2)));
		
		rvm.declareConst("0", v.integer(0));
		rvm.declareConst("1", v.integer(1));
		rvm.declareConst("2", v.integer(2));
		rvm.declareConst("3", v.integer(3));
		rvm.declareConst("4", v.integer(4));
		
		rvm.declare(new Function("main", 0, 0, 1, 6,
					new CodeBlock().
						loadcon("1").
						loadcon("2").
						loadcon("3").
						loadcon("3").
						callprim(Primitive.make_list).
						halt()));
	
		rvm.executeProgram("main", new IValue[] {});
	}

}
