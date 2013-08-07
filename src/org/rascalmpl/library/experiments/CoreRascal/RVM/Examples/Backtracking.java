package org.rascalmpl.library.experiments.CoreRascal.RVM.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Function;
import org.rascalmpl.library.experiments.CoreRascal.RVM.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class Backtracking {

	public static void main(String[] args) {
		
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		
		/*
		 * TRUE(){ return true; }
		 */
		rvm.declare(new Function("TRUE", 0, 0, 0, 6,
				new CodeBlock(vf)
				.loadcon(true)
				.yield1()	
				.loadcon(true)
				.ret1()
				.halt()
				));
		
		/* 
		 * FALSE { return false; }
		 */
		
		rvm.declare(new Function("FALSE", 0, 0, 0, 6,
				new CodeBlock(vf)
				.loadcon(false)
				.ret1()	
				.halt()
				));
		/* 
		 * and(lhs, rhs) {
		 *    lhs.start();
		 *    rhs.start();
		 *    WHILE1: while(lhs.hasNext()){
		 *      		if(lhs.next()){
		 *    		WHILE2: while(rhs.hasNext()){
		 *    					if(rhs.next())
		 *    						yield true;
		 *          		}
		 *      	  }
		 *    }  
		 *    return false;
		 * }
		 */
		
		rvm.declare(new Function("and", 0, 2, 2, 10,
				new CodeBlock(vf)
				.loadloc(0)
				.init()
				.loadloc(1)
				.init()
			.label("WHILE1")
				.loadloc(0)
				.hasNext()
				.jmptrue("BODY1")
				.loadcon(false)
				.ret1()
			.label("BODY1")
				.loadloc(0)
				.next0()
				.jmpfalse("WHILE1")
			.label("WHILE2")
				.loadloc(1)
				.hasNext()
				.jmpfalse("WHILE1")
			.label("BODY2")
				.loadloc(1)
				.next0()
				.jmpfalse("WHILE2")
				.loadcon(true)
				.yield1()
				.jmp("WHILE1")
		));
		
		
		rvm.declare(new Function("strange", 0, 0, 3, 10,
				new CodeBlock(vf)
					.create("TRUE")
					.storeloc(0)
					.loadloc(0)
					.init()
					.loadloc(0)
					.next0()    // expect true
					.print("First next: $0")
					.loadloc(0)
					.hasNext() // expect false
					.print("hasNext: $0")
					.loadloc(0)
					.next0()	// expect runtime error (but we get true)
					.halt()
	));

				
		/*
		 * 
		 * main(){
		 *   lhs = create(TRUE);
		 *   rhs = create(TRUE);
		 *   e = create(and);
		 *   e.start(lhs, rhs);
		 *   e.next();
		 * }
		 */
		
		rvm.declare(new Function("main", 0, 0, 3, 10,
					new CodeBlock(vf)
						.create("TRUE")
						.storeloc(0)
						.loadloc(0)
						.init()
						
						.create("FALSE")
						.storeloc(1)
						.loadloc(1)
						.init()
						
						.create("and")
						.storeloc(2)
						.loadloc(0)
						.loadloc(1)
						.loadloc(2)
						.init()
						.loadloc(2)
						.next0()
						.halt()
		));
	
		rvm.executeProgram("strange", new IValue[] {});
	}

}
