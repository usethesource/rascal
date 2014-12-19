package org.rascalmpl.library.experiments.Compiler;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CompilerError;

public class Profile {
	
		protected final IValueFactory values;
		
		public Profile(IValueFactory values){
			super();
			this.values = values;
		}
		
		public void startProfile(IEvaluatorContext ctx){
			throw new CompilerError("startProfile only implemented for compiled code");
		}
		
		public ISet stopProfile(IEvaluatorContext ctx){
			throw new CompilerError("stopProfile only implemented for compiled code");
		}
		
		public ISet getProfile(IEvaluatorContext ctx){
			throw new CompilerError("getProfile only implemented for compiled code");
		}
		
//		public void printProfile(IEvaluatorContext ctx){
//			throw new CompilerError("printProfile only implemented for compiled code");
//		}
}
