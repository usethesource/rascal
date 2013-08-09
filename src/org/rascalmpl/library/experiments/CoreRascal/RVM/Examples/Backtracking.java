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
				.LOADCON(true)
				.RETURN1()
				.HALT()
				));
		
		/* 
		 * FALSE { return false; }
		 */
		
		rvm.declare(new Function("FALSE", 0, 0, 0, 6,
				new CodeBlock(vf)
				.LOADCON(false)
				.RETURN1()	
				.HALT()
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
				.LOADLOC(0)
				.CREATEDYN()
				.LOADLOC(0)
				.INIT()
				
			.LABEL("WHILE1")
				.LOADLOC(0)
				.HASNEXT()
				.JMPTRUE("BODY1")
				.LOADCON(false)
				.RETURN1()
			.LABEL("BODY1")
				.LOADLOC(0)
				.NEXT0()
				.JMPFALSE("WHILE1")
				.LOADLOC(1)
				.CREATEDYN()
				.LOADLOC(1)
				.INIT()
				
			.LABEL("WHILE2")
				.LOADLOC(1)
				.HASNEXT()
				.JMPFALSE("WHILE1")
				.LOADLOC(1)
				.NEXT0()
				.JMPFALSE("WHILE2")
				.LOADCON(true)
				.YIELD1()
				.JMP("WHILE2")
		));
		
		/* 
		 * and_n_n(lhs, rhs) {
		 *    return callprim("and_bool_bool", lhs, rhs);
		 * }
		 */
		
		rvm.declare(new Function("and_b_b", 0, 2, 2, 10,
				new CodeBlock(vf)
				.LOADLOC(0)
				.LOADLOC(1)
				.CALLPRIM(Primitive.and_bool_bool)
				.RETURN1()
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
				.LOADLOC(0)
				.JMPTRUE("L")
				
			.LABEL("RETURN")
				.LOADCON(false)
				.RETURN1()
				
			.LABEL("L")
				.LOADLOC(1)
				.CREATEDYN()
				.LOADLOC(1)
				.INIT()
				
			.LABEL("WHILE")
				.LOADLOC(1)
				.HASNEXT()
				.JMPFALSE("RETURN")
				.LOADLOC(1)
				.NEXT0()
				.JMPFALSE("RETURN")
				.LOADCON(true)
				.YIELD1()
				.JMP("WHILE")
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
				.LOADLOC(0)
				.JMPTRUE("L")
				
			.LABEL("RETURN")
				.LOADCON(false)
				.RETURN1()
				
			.LABEL("L")
				.LOADLOC(1)
				.CREATEDYN()
				.LOADLOC(1)
				.INIT()
				
			.LABEL("WHILE")
				.LOADLOC(0)
				.HASNEXT()
				.JMPFALSE("RETURN")
				.LOADLOC(0)
				.NEXT0()
				.JMPFALSE("RETURN")
				.LOADCON(true)
				.YIELD1()
				.JMP("WHILE")
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
						.CREATE("TRUE")
						.STORELOC(0)
						.LOADLOC(0)
						.INIT()
						
						.CREATE("FALSE")
						.STORELOC(1)
						.LOADLOC(1)
						.INIT()
						
						.CREATE("and_b_b")
						.STORELOC(2)
						.LOADLOC(0)
						.LOADLOC(1)
						.LOADLOC(2)
						.INIT()
						.LOADLOC(2)
						.NEXT0()
						.HALT()
		));
	
		rvm.executeProgram("main", new IValue[] {});
	}

}
