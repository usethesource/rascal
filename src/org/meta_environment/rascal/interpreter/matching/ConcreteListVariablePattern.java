package org.meta_environment.rascal.interpreter.matching;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.types.ConcreteSyntaxType;
import org.meta_environment.rascal.interpreter.utils.Names;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.SymbolAdapter;
import org.meta_environment.uptr.TreeAdapter;

public class ConcreteListVariablePattern extends AbstractMatchingResult {
	private String name;
	private ConcreteSyntaxType declaredType;
	
	private boolean anonymous = false;
	private boolean debug = false;
	private boolean iDeclaredItMyself;
	
	public ConcreteListVariablePattern(IValueFactory vf, IEvaluatorContext ctx, 
			org.eclipse.imp.pdb.facts.type.Type type, org.meta_environment.rascal.ast.Name name) {
		super(vf, ctx);
		this.name = Names.name(name);
		this.declaredType = (ConcreteSyntaxType) type;
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
				SymbolAdapter sym = new SymbolAdapter(declaredType.getSymbol()).getSymbol();
				if (sym.isIterPlus() || sym.isIterPlusSep()) {
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
		
		TreeAdapter subjectTree = new TreeAdapter((IConstructor) subject.getValue());
		if (subjectTree.isList()) {
			if ((subjectTree.getArgs()).isEmpty()) {
				SymbolAdapter sym = new SymbolAdapter(declaredType.getSymbol()).getSymbol();
				if (sym.isIterPlus() || sym.isIterPlusSep()) {
					return false;
				}
			}
			if (subjectTree.getProduction().getRhs().getTree().isEqual(declaredType.getSymbol())) {
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
		return Factory.Tree_Appl.make(vf, Factory.Production_List.make(vf, declaredType.getSymbol()), subject);
	}

	@Override
	public String toString() {
		return declaredType + " " + name + ":=" + subject;
	}
}
