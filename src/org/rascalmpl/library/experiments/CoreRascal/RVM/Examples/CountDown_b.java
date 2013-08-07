package org.rascalmpl.library.experiments.CoreRascal.RVM.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Function;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Primitive;
import org.rascalmpl.library.experiments.CoreRascal.RVM.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class CountDown_b {
	
	public static void main(String[] args) {
		
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory v = rvm.vf;
		
		rvm.declareConst("0", v.integer(0));
		rvm.declareConst("1", v.integer(1));
		rvm.declareConst("9", v.integer(9));
		
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
		
		rvm.declare(new Function("g", 0, 1, 1, 6,
					new CodeBlock()
							.label("LOOP")
							.loadloc(0)
							.loadcon("0")
							.callprim(Primitive.greater_num_num)
							.jmptrue("BODY")
							.loadcon("0")
							.ret1()
							.label("BODY")
							.loadloc(0)
							.yield1()
							.loadloc(0)
							.loadcon("1")
							.callprim(Primitive.substraction_num_num)
							.storeloc(0)
							.jmp("LOOP")));
		/*
		 * h() {
		 * n = 9 + 1;
		 * c = create(g);
		 * c.init(n);
		 * return c;
		 * }
		 */
		
		rvm.declare(new Function("h", 0, 0, 2, 6, 
					new CodeBlock()
						.loadcon("9")
						.loadcon("1")
						.callprim(Primitive.addition_num_num)
						.storeloc(0)
						.create("g")
						.storeloc(1)
						.loadloc(0)
						.loadloc(1)
						.init()
						.pop()
						.loadloc(1)
						.ret1()));
		
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
		rvm.declare(new Function("main", 0, 0, 3, 6,
					new CodeBlock()
						.call("h")
						.storeloc(0)
						.call("h")
						.storeloc(1)
						.loadcon("0")
						.storeloc(2)
						
						.label("LOOP")
						.loadloc(0)
						.hasNext()
						//.loadloc(1)
						//.hasNext()
											
						.jmptrue("BODY")
						.halt()
						.label("BODY")
						.loadloc(0)
						.next0()
						.loadloc(1)
						.next0()
						.callprim(Primitive.addition_num_num)
						.loadloc(2)
						.callprim(Primitive.addition_num_num)
						.storeloc(2)
						
						.jmp("LOOP")));
	
		rvm.executeProgram("main", new IValue[] {});
	}
	
}
