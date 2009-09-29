package org.meta_environment.rascal.interpreter.matching;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.types.NonTerminalType;
import org.meta_environment.rascal.interpreter.utils.Names;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.ProductionAdapter;
import org.meta_environment.uptr.SymbolAdapter;
import org.meta_environment.uptr.TreeAdapter;

public class ConcreteListVariablePattern extends AbstractMatchingResult {
	private String name;
	private NonTerminalType declaredType;
	
	private boolean anonymous = false;
	private boolean debug = false;
	private boolean iDeclaredItMyself;
	
	public ConcreteListVariablePattern(IEvaluatorContext ctx, 
			org.eclipse.imp.pdb.facts.type.Type type, org.meta_environment.rascal.ast.Name name) {
		super(ctx);
		this.name = Names.name(name);
		this.declaredType = (NonTerminalType) type;
		this.anonymous = name.toString().equals("_");
		this.iDeclaredItMyself = false;
	}

	@Override
	public Type getType(Environment env) {
		return declaredType;
	}

	@Override
	public java.util.List<String> getVariables() {
		java.util.LinkedList<String> res = new java.util.LinkedList<String>();
		res.addFirst(name);
		return res;
	}

	@Override
	public IValue toIValue(Environment env) {
		throw new UnsupportedOperationException("toIValue on Variable");
	}

	public String getName() {
		return name;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	@Override
	public boolean next() {
		if (debug) {
			System.err.println("AbstractConcreteSyntaxListVariable.next");
		}
		checkInitialized();
		if (!hasNext)
			return false;
		hasNext = false;
		
		
		if (debug) {
			System.err.println("Subject: " + subject + " name: " + name
					+ " getType: ");
			
			System.err.println("AbstractConcreteSyntaxListVariable.next: " + subject
					+ "(type=" + subject.getType() + ") with " + declaredType
					+ " " + name);
		}
	
		if (!anonymous && !iDeclaredItMyself && !ctx.getCurrentEnvt().declareVariable(declaredType, name)) {
			throw new RedeclaredVariableError(name, ctx.getCurrentAST());
		}
		
		iDeclaredItMyself = true;
		
		if (subject.getType().isSubtypeOf(Factory.Args)) {
			if (((IList)subject.getValue()).isEmpty()) {
				IConstructor sym = SymbolAdapter.getSymbol(declaredType.getSymbol());
				if (SymbolAdapter.isIterPlus(sym) || SymbolAdapter.isIterPlusSep(sym)) {
					return false;
				}
			}
			if (!anonymous)
				ctx.getCurrentEnvt().storeVariable(name, makeResult(declaredType,
						wrapWithListProd(subject.getValue()), ctx));
			if (debug) {
				System.err.println("matches");
			}
			return true;
		}
		
		IConstructor subjectTree = (IConstructor) subject.getValue();
		if (TreeAdapter.isList(subjectTree)) {
			if ((TreeAdapter.getArgs(subjectTree)).isEmpty()) {
				IConstructor sym = SymbolAdapter.getSymbol(declaredType.getSymbol());
				if (SymbolAdapter.isIterPlus(sym) || SymbolAdapter.isIterPlusSep(sym)) {
					return false;
				}
			}
			if (ProductionAdapter.getRhs(TreeAdapter.getProduction(subjectTree)).isEqual(declaredType.getSymbol())) {
				ctx.getCurrentEnvt().storeVariable(name, subject);
			}
			if (debug)
				System.err.println("matches");
			return true;
		}
		
// 		if (debug)
//			System.err.println("no match");
//		 return false;
		return true;
	}


	private IValue wrapWithListProd(IValue subject) {
		IList args = (IList) subject;
		IValue prod = Factory.Production_List.make(ctx.getValueFactory(), declaredType.getSymbol());
		
		if (args.length() == 1) {
			IConstructor arg = (IConstructor) args.get(0);
			
			if (TreeAdapter.isList(arg) && ProductionAdapter.getTree(TreeAdapter.getProduction(arg)).isEqual(prod)) {
				return arg;
			}
		}
		
		return Factory.Tree_Appl.make(ctx.getValueFactory(), prod, subject);
	}

	@Override
	public String toString() {
		return declaredType + " " + name + ":=" + subject;
	}
}
