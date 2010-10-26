package org.rascalmpl.interpreter.matching;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.RedeclaredVariableError;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

public class TypedVariablePattern extends AbstractMatchingResult {
	private String name;
	org.eclipse.imp.pdb.facts.type.Type declaredType;
	private boolean anonymous = false;
	private boolean debug = false;
	private boolean iDeclaredItMyself;
	private Environment cachedEnv;

	public TypedVariablePattern(IEvaluatorContext ctx, Expression x, 
			org.eclipse.imp.pdb.facts.type.Type type, org.rascalmpl.ast.Name name) {
		super(ctx, x);
		this.name = Names.name(name);
		this.declaredType = type;
		this.anonymous = Names.name(name).equals("_");
		this.iDeclaredItMyself = false;
		if(debug) System.err.println("AbstractPatternTypedVariabe: " + name);
	}
	
	@Override
	public Type getType(Environment env) {
		return declaredType;
	}
	
	@Override
	public java.util.List<String> getVariables(){
		java.util.LinkedList<String> res = new java.util.LinkedList<String>();
		res.addFirst(name);
		return res;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isAnonymous(){
		return anonymous;
	}

	@Override
	public boolean next() {
		if(debug)System.err.println("AbstractTypedVariable.next");
		checkInitialized();
		if(!hasNext)
			return false;
		hasNext = false;
		if(debug) {
			System.err.println("Subject: " + subject + " name: " + name + " getType: ");
			System.err.println("AbstractTypedVariable.next: " + subject + "(type=" + subject.getType() + ") with " + declaredType + " " + name);
		}

		if (!anonymous && !iDeclaredItMyself && !ctx.getCurrentEnvt().declareVariable(declaredType, name)) {
			throw new RedeclaredVariableError(name, ctx.getCurrentAST());
		}

		// in some circumstances, apparently, the environment that held the declared variable is cleaned up between
		// calls to next. In this case, we need to redeclare it. 
		if (cachedEnv != ctx.getCurrentEnvt()) {
			ctx.getCurrentEnvt().declareVariable(declaredType, name);
		}
		
		cachedEnv = ctx.getCurrentEnvt();
		iDeclaredItMyself = true;
		
		// isSubtypeOf does not know about concrete syntax types
		// so deal with it here explicitly
		if (declaredType instanceof NonTerminalType) {
			Type subjectType = subject.getValue().getType(); 
			if (subjectType.isSubtypeOf(Factory.Tree) && ((IConstructor)subject.getValue()).getConstructorType() == Factory.Tree_Appl) {
				IConstructor tree = (IConstructor)subject.getValue();

				if (((NonTerminalType)declaredType).getSymbol().isEqual(ProductionAdapter.getRhs(TreeAdapter.getProduction(tree)))) {
					if(anonymous) { 
						return true;
					}				
					ctx.getCurrentEnvt().storeVariable(name, ResultFactory.makeResult(declaredType, subject.getValue(), ctx));
					return true;
				}
			}
			return false;
		}
		
		if (subject.getValue().getType().isSubtypeOf(declaredType)) {
			if(debug)System.err.println("matches");
			
			if(anonymous) {
				return true;
			}
			
			ctx.getCurrentEnvt().storeVariable(name, ResultFactory.makeResult(declaredType, subject.getValue(), ctx));
			return true;
		}
		
		if(debug)System.err.println("no match");
		return false;
	}
	
	@Override
	public String toString(){
		return declaredType + " " + name + "==" + subject;
	}
}