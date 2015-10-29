package org.rascalmpl.library.experiments.Compiler;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CompilerError;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.IValueFactory;

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
		
		public IList getProfile(IEvaluatorContext ctx){
			throw new CompilerError("getProfile only implemented for compiled code");
		}
		
		public void reportProfile(IEvaluatorContext ctx){
			throw new CompilerError("reportProfile only implemented for compiled code");
		}
		
		public void reportProfile(IList data, IEvaluatorContext ctx){
			throw new CompilerError("reportProfile only implemented for compiled code");
		}
}
