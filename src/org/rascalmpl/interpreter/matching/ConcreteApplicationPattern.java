package org.rascalmpl.interpreter.matching;

import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.Expression.CallOrTree;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.utils.IUPTRAstToSymbolConstructor;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.interpreter.utils.IUPTRAstToSymbolConstructor.NonGroundSymbolException;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.TreeAdapter;

public class ConcreteApplicationPattern extends AbstractMatchingResult {
	private Expression.CallOrTree callOrTree;
	private boolean hasNext;
	private QualifiedName qname;
	private List<IMatchingResult> children;
	private boolean firstMatch;
	private boolean debug = false;

	public ConcreteApplicationPattern(
			IEvaluatorContext ctx, CallOrTree x,
			List<IMatchingResult> list) {
		
		super(ctx);
		this.qname = x.getExpression().getQualifiedName();
		this.children = list;
		callOrTree = x;
		if(debug){
			System.err.println("ConcreteApplicationPattern: " + qname + ", #children:" + list.size());
			for(IMatchingResult r : list){
				System.err.println("\t" + r);
			}
		}
	}

	@Override
	public void initMatch(Result<IValue> subject) {
		hasNext = false;
		Type subjectType = subject.getType();
		super.initMatch(subject);
		if(subjectType.isAbstractDataType()){
			IConstructor treeSubject = (IConstructor)subject.getValue();
			if(TreeAdapter.isAppl(treeSubject) &&  ((IList) treeSubject.get(1)).length() == children.size()){

				if(!Names.name(Names.lastName(qname)).equals(treeSubject.getName().toString())) {
					return;
				}
				IList treeSubjectChildren = (IList) treeSubject.get(1);

				for (int i = 0; i < children.size(); i += 2){ // skip layout
					IValue childValue = treeSubjectChildren.get(i);
					// TODO: see if we can use a static type here!?
					children.get(i).initMatch(ResultFactory.makeResult(childValue.getType(), childValue, ctx));
				}
				firstMatch = hasNext = true;
				return;
			}
		}
	}
	
	@Override
	public boolean hasNext(){
		if(!initialized)
			return false;
		if(firstMatch)
			return true;
		if(!hasNext)
			return false;

		if(children.size() > 0){
			for (int i = 0; i < children.size(); i += 2) { // skip layout
				if(children.get(i).hasNext()){
					return true;
				}
			}
		}
		hasNext = false;
		return false;
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		
		if(!(firstMatch || hasNext))
			return false;
		
		if(children.size() == 0){
			boolean res = firstMatch;
			firstMatch = hasNext = false;
			return res;	
		}
	   
		firstMatch = false;
		hasNext = matchChildren();
		
		return hasNext;
	}
	
	boolean matchChildren(){
		for (int i = 0; i < children.size(); i += 2) {
			if(!children.get(i).next()){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public Type getType(Environment env) {
		CallOrTree prod = (CallOrTree) callOrTree.getArguments().get(0);
		
		String name = Names.name(Names.lastName(prod.getExpression().getQualifiedName()));
		CallOrTree rhs;
		
		if (name.equals("prod")) {
			rhs = (CallOrTree) prod.getArguments().get(1);
		}
		else if (name.equals("list")) {
			rhs = (CallOrTree) prod.getArguments().get(0);
		}
		else {
			return Factory.Tree;
		}
		
		try {
			return RascalTypeFactory.getInstance().nonTerminalType(rhs.accept(new IUPTRAstToSymbolConstructor(ctx.getValueFactory())));
		}
		catch (NonGroundSymbolException e) {
			return Factory.Tree;
		}
	}

	@Override
	public IValue toIValue(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}
