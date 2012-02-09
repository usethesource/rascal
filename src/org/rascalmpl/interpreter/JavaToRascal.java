/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Bert  B. Lisser - Bert.Lisser@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter;

import java.io.PrintWriter;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.values.ValueFactoryFactory;

public class JavaToRascal {

	private final static String SHELL_MODULE = "$shell$";
	private GlobalEnvironment heap = new GlobalEnvironment();

	final private Evaluator evaluator;
	
	protected final static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	

	public Evaluator getEvaluator() {
		return evaluator;
	}

	public JavaToRascal(PrintWriter stdout, PrintWriter stderr) {
		this.evaluator = new Evaluator(vf, stderr,
				stdout, new ModuleEnvironment(SHELL_MODULE, heap), heap);
	}
	
	public JavaToRascal(Evaluator evaluator) {
		this.evaluator = evaluator;
	}
	
//    public String call(String name, String... args) {
//    	IString[] vals = new IString[args.length];
//    	for (int i=0;i<args.length;i++) vals[i] = vf.string(args[i]);
//    	return evaluator.call(name, vals).toString();
//	}
	
	public IValue call(String name, IValue ...args) {
		return evaluator.call(name, args);
	}
    
    public String eval(String command, String location){
		Result<IValue> result = evaluator.eval(null, command, URI.create(location));
		// System.err.println("Result:"+result.getType()+" "+result.getType().isStringType()+" "+result.getType().isVoidType());
		if (result.getType().isVoidType()) {
			return "ok";			
		}
		if (result.getType().isStringType()) {
			return ((IString) (result.getValue())).getValue();		
		}		
		return result.toString();
	}
    
    public String eval(String command) {
    	return eval(command, "stdin:///");
    }
    
    public static void main(String[] args) {
    	final JavaToRascal jr = new JavaToRascal(new PrintWriter(System.out), new PrintWriter(System.err));
    	System.out.println(jr.eval("import List;"));
    	System.out.println(jr.eval("\"<2+3>\";"));
    	System.out.println(jr.eval("\"aap:<size([2,3])>\";"));
    	final IInteger d1 = vf.integer(1), d2 = vf.integer(2);
    	final IList l = vf.list(d1, d2);
    	System.out.println(jr.call("size", l));
    	// System.out.println(jr.call("+", d1, d2)); 
    }
}

