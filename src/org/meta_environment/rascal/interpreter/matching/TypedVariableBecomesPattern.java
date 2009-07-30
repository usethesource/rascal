package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.utils.Names;

public class TypedVariableBecomesPattern extends AbstractMatchingResult {
	private String name;
	private Type declaredType;
	private IMatchingResult pat;
	private boolean debug = false;
	private boolean iDeclaredItMyself = false;

	public TypedVariableBecomesPattern(IValueFactory vf, IEvaluatorContext ctx,
			org.eclipse.imp.pdb.facts.type.Type type, org.meta_environment.rascal.ast.Name aname, IMatchingResult pat){
		super(vf, ctx);
		this.name = Names.name(aname);
		this.declaredType = type;
		this.pat = pat;
	}
	
	@Override
	public void initMatch(Result<IValue> subject){
		super.initMatch(subject);
		pat.initMatch(subject);
		Environment env = ctx.getCurrentEnvt();
		if(!mayMatch(pat.getType(env), declaredType)) {
			throw new UnexpectedTypeError(pat.getType(env), declaredType, ctx.getCurrentAST());
		}
	}
	
	@Override
	public Type getType(Environment env) {
		return declaredType;
	}
	
	@Override
	public boolean hasNext(){
		return pat.hasNext();
	}

	@Override
	public boolean next() {
		if(debug) System.err.println("AbstractPatternTypedVariableBecomes:  next");
		
		if (!iDeclaredItMyself) {
			if (!ctx.getCurrentEnvt().declareVariable(declaredType, name)) {
				throw new RedeclaredVariableError(name, ctx.getCurrentAST());
			}
		}
		
		iDeclaredItMyself = true;
		
		if (pat.next()) {
			ctx.getCurrentEnvt().storeVariable(name, ResultFactory.makeResult(declaredType, subject.getValue(), ctx));
			return true;
		}
		
		return false;
	}

	@Override
	public IValue toIValue(Environment env) {
		return null;
	}
}