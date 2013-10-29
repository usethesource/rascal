package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Examples;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.values.ValueFactoryFactory;

public class Backtracking {

	public static void main(String[] args) {
		
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		IValueFactory vf = rvm.vf;
		TypeFactory tf = TypeFactory.getInstance();
		/*
		 * TRUE(){ return true; }
		 */
		rvm.declare(new Function("TRUE", tf.valueType(), null, 0, 0, 6,
				new CodeBlock(vf)
				.LOADCON(true)
				.RETURN1(1)
				.HALT()
				));
		
		/* 
		 * FALSE { return false; }
		 */
		
		rvm.declare(new Function("FALSE", tf.valueType(), null, 0, 0, 6,
				new CodeBlock(vf)
				.LOADCON(false)
				.RETURN1(1)	
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
		
		rvm.declare(new Function("and_b_b", tf.valueType(), null, 2, 2, 10,
				new CodeBlock(vf)
				
			.LABEL("WHILE1")
				.LOADLOC(0)
				.HASNEXT()
				.JMPTRUE("BODY1")
				.LOADCON(false)
				.RETURN1(1)
			.LABEL("BODY1")
				.LOADLOC(0)
				.NEXT0()
				.JMPFALSE("WHILE1")
				
			.LABEL("WHILE2")
				.LOADLOC(1)
				.HASNEXT()
				.JMPFALSE("WHILE1")
				.LOADLOC(1)
				.NEXT0()
				.JMPFALSE("WHILE2")
				.LOADCON(true)
				.YIELD1(1)
				.POP()
				.JMP("WHILE2")
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
		
//		rvm.declare(new Function("and_b_b", 2, 2, 10,
//				new CodeBlock(vf)
//				.LOADLOC(0)
//				.JMPTRUE("L")
//				
//			.LABEL("RETURN")
//				.LOADCON(false)
//				.RETURN1()
//				
//			.LABEL("L")
//				.LOADLOC(1)
//				.CREATEDYN(0)
//				.STORELOC(1)
//				.POP()
//				.LOADLOC(1)
//				.INIT(0)
//				.STORELOC(1)
//				.POP()
//				
//			.LABEL("WHILE")
//				.LOADLOC(1)
//				.HASNEXT()
//				.JMPFALSE("RETURN")
//				.LOADLOC(1)
//				.NEXT0()
//				.JMPFALSE("RETURN")
//				.LOADCON(true)
//				.YIELD1()
//				.POP()
//				.JMP("WHILE")
//		));
		
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
		
		rvm.declare(new Function("and_b_n", tf.valueType(), null, 2, 2, 10,
				new CodeBlock(vf)
				.LOADLOC(0)
				.JMPTRUE("L")
				
			.LABEL("RETURN")
				.LOADCON(false)
				.RETURN1(1)
				
			.LABEL("L")
				.LOADLOC(1)
				.CREATEDYN(0)
				.STORELOC(1)
				.POP()
				.LOADLOC(1)
				.INIT(0)
				.STORELOC(1)
				.POP()
				
			.LABEL("WHILE")
				.LOADLOC(0)
				.HASNEXT()
				.JMPFALSE("RETURN")
				.LOADLOC(0)
				.NEXT0()
				.JMPFALSE("RETURN")
				.LOADCON(true)
				.YIELD1(1)
				.POP()
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
		
		rvm.declare(new Function("main", tf.valueType(), null, 1, 4, 10,
					new CodeBlock(vf)
						.CREATE("TRUE",0)
						.STORELOC(1)
						.POP()
						.LOADLOC(1)
						.INIT(0)
						.STORELOC(1)
						.POP()
						
						.CREATE("FALSE",0)
						.STORELOC(2)
						.POP()
						.LOADLOC(2)
						.INIT(0)
						.STORELOC(2)
						.POP()
						
						.CREATE("and_b_b",0)
						.STORELOC(3)
						.POP()
						.LOADLOC(1)
						.LOADLOC(2)
						.LOADLOC(3)
						.INIT(2)
						.STORELOC(3)
						.POP()
						.LOADLOC(3)
						.NEXT0()
						.HALT()
		));
	
		rvm.declare(new Function("#module_init", tf.valueType(), null, 1, 1, 6, 
				new CodeBlock(vf)
					.LOADLOC(0)
					.CALL("main", 1)
					.RETURN1(1)
					.HALT()));

		rvm.executeProgram("main", new IValue[] {});
	}

}
