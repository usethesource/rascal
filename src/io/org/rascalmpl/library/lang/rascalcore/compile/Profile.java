package org.rascalmpl.library.experiments.Compiler;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.InternalCompilerError;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValueFactory;

public class Profile {
	
		protected final IValueFactory values;
		
		public Profile(IValueFactory values){
			super();
			this.values = values;
		}
		
		public void startProfile(IEvaluatorContext ctx){
			throw new InternalCompilerError("startProfile only implemented for compiled code");
		}
		
		public ISet stopProfile(IEvaluatorContext ctx){
			throw new InternalCompilerError("stopProfile only implemented for compiled code");
		}
		
		public IList getProfile(IEvaluatorContext ctx){
			throw new InternalCompilerError("getProfile only implemented for compiled code");
		}
		
		public void reportProfile(IEvaluatorContext ctx){
			throw new InternalCompilerError("reportProfile only implemented for compiled code");
		}
		
		public void reportProfile(IList data, IEvaluatorContext ctx){
			throw new InternalCompilerError("reportProfile only implemented for compiled code");
		}
}
