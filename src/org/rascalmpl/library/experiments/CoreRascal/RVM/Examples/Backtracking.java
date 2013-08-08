package org.rascalmpl.library.experiments.CoreRascal.RVM.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Function;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Primitive;
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
		 * and_b_b(lhs, rhs) {
		 * 	  lhs = create(lhs)
		 *    init(lhs);
		 *    WHILE1: while(hasNext(lhs)){
		 *      		if(next(lhs)){
		 *      			rhs = create(rhs);
		 *    				init(rhs);
		 *    		WHILE2: while(hasNext(rhs)){
		 *    					if(next(rhs))
		 *    						yield true;
		 *          		}
		 *      	  }
		 *    }  
		 *    return false;
		 * }
		 */
		
		rvm.declare(new Function("and_b_b", 0, 2, 2, 10,
				new CodeBlock(vf)
				.loadloc(0)
				.createdyn()
				.loadloc(0)
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
				.loadloc(1)
				.createdyn()
				.loadloc(1)
				.init()
				
			.label("WHILE2")
				.loadloc(1)
				.hasNext()
				.jmpfalse("WHILE1")
				.loadloc(1)
				.next0()
				.jmpfalse("WHILE2")
				.loadcon(true)
				.yield1()
				.jmp("WHILE2")
		));
		
		/* 
		 * and_n_n(lhs, rhs) {
		 *    return callprim("and_bool_bool", lhs, rhs);
		 * }
		 */
		
		rvm.declare(new Function("and_b_b", 0, 2, 2, 10,
				new CodeBlock(vf)
				.loadloc(0)
				.loadloc(1)
				.callprim(Primitive.and_bool_bool)
				.ret1()
				));
		
		/* 
		 * and_n_b(lhs, rhs) {
		 *    if(lhs){
		 *    		rhs = create(rhs);
		 *    		init(rhs);
		 *    		WHILE2: while(hasNext(rhs)){
		 *    					if(next(rhs))
		 *    						yield true;
		 *          		}
		 *      	  }
		 *    }  
		 *    return false;
		 * }
		 */
		
		rvm.declare(new Function("and_b_b", 0, 2, 2, 10,
				new CodeBlock(vf)
				.loadloc(0)
				.jmptrue("L")
				
			.label("RETURN")
				.loadcon(false)
				.ret1()
				
			.label("L")
				.loadloc(1)
				.createdyn()
				.loadloc(1)
				.init()
				
			.label("WHILE")
				.loadloc(1)
				.hasNext()
				.jmpfalse("RETURN")
				.loadloc(1)
				.next0()
				.jmpfalse("RETURN")
				.loadcon(true)
				.yield1()
				.jmp("WHILE")
		));
		
		/* 
		 * and_b_n(lhs, rhs) {        // Check rhs first?
		 *    lhs = create(lhs)    
		 *    init(lhs);
		 *    WHILE1: while(hasNext(lhs)){
		 *      		if(next(lhs)){
		 *    		       if(rhs){
		 *    					return true;
		 *          		}
		 *      	  }
		 *    }  
		 *    return false;
		 * }
		 */
		
		rvm.declare(new Function("and_b_n", 0, 2, 2, 10,
				new CodeBlock(vf)
				.loadloc(0)
				.jmptrue("L")
				
			.label("RETURN")
				.loadcon(false)
				.ret1()
				
			.label("L")
				.loadloc(1)
				.createdyn()
				.loadloc(1)
				.init()
				
			.label("WHILE")
				.loadloc(0)
				.hasNext()
				.jmpfalse("RETURN")
				.loadloc(0)
				.next0()
				.jmpfalse("RETURN")
				.loadcon(true)
				.yield1()
				.jmp("WHILE")
		));
		
		
				
		/*
		 * 
		 * main(){
		 *   e = create(and_b_b);
		 *   e.start(e, TRUE, TRUE);
		 *   next(e);
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
						
						.create("and_b_b")
						.storeloc(2)
						.loadloc(0)
						.loadloc(1)
						.loadloc(2)
						.init()
						.loadloc(2)
						.next0()
						.halt()
		));
	
		rvm.executeProgram("main", new IValue[] {});
	}

}
