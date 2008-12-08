package org.meta_environment.rascal.interpreter;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Expression.CallOrTree;
import org.meta_environment.rascal.ast.Expression.List;
import org.meta_environment.rascal.ast.Expression.Literal;
import org.meta_environment.rascal.ast.Expression.Map;
import org.meta_environment.rascal.ast.Expression.QualifiedName;
import org.meta_environment.rascal.ast.Expression.Set;
import org.meta_environment.rascal.ast.Expression.Tuple;
import org.meta_environment.rascal.ast.Expression.TypedVariable;

/* package */ interface PatternValue {
	public boolean match(IValue subj, Evaluator ev);
}

/* package */ class BasicPattern {
	
	boolean matchChildren(Iterator<IValue> subjChildren, Iterator<PatternValue> patChildren, Evaluator ev){
		while (patChildren.hasNext()) {
			if (!patChildren.next().match(subjChildren.next(), ev)){
				return false;
			}
		}
		return true;
	}
}
/* package */ class PatternLiteral extends BasicPattern implements PatternValue {
	private IValue literal;
	
	PatternLiteral(IValue literal){
		this.literal = literal;
	}
	
	public boolean match(IValue subj, Evaluator ev){
			if (subj.getType().isSubtypeOf(literal.getType())) {
				return ev.equals(ev.result(subj), ev.result(literal));
			}
			return false;
	}
}

/* package */ class PatternTree extends BasicPattern implements PatternValue {
	private org.meta_environment.rascal.ast.QualifiedName name;
	private java.util.List<PatternValue> children;
	
	PatternTree(org.meta_environment.rascal.ast.QualifiedName qualifiedName, java.util.List<PatternValue> children){
		this.name = qualifiedName;
		this.children = children;
	}
	
	public boolean match(IValue subj, Evaluator ev){
		if (!subj.getType().isTreeType()) {
			return false;
		}

		ITree subjTree = (ITree) subj;
		
		if (name.toString().equals(subjTree.getName().toString()) && 
			children.size() == subjTree.arity()){
			return matchChildren(subjTree.getChildren().iterator(), children.iterator(), ev);
		}
		return false;
	}
}

/* package */ class PatternList extends BasicPattern implements PatternValue {
	private java.util.List<PatternValue> children;
	
	PatternList(java.util.List<PatternValue> children){
		this.children = children;
	}
	
	public boolean match(IValue subj, Evaluator ev){
		
		if (!subj.getType().isListType()) {
			return false;
		}
		
		IList subjList = (IList) subj;
		if ( children.size() == subjList.length()){
				return matchChildren(subjList.iterator(), children.iterator(), ev);
			}
		return false;
	}
}

/* package */ class PatternSet extends BasicPattern implements PatternValue {
	private java.util.List<PatternValue> children;
	
	PatternSet(java.util.List<PatternValue> children){
		this.children = children;
	}
	
	public boolean match(IValue subj, Evaluator ev){
		throw new RascalBug("PatternSet.match not implemented");
	}
}

/* package */ class PatternTuple extends BasicPattern implements PatternValue {
	private java.util.List<PatternValue> children;
	
	PatternTuple(java.util.List<PatternValue> children){
		this.children = children;
	}
	
	public boolean match(IValue subj, Evaluator ev) {

		if (subj.getType().isTupleType()
				&& ((ITuple) subj).arity() == children.size()) {
			return matchChildren(((ITuple) subj).iterator(), children.iterator(), ev);
		}
		return false;
	}
}

/* package */ class PatternMap extends BasicPattern implements PatternValue {
	private java.util.List<PatternValue> children;
	
	PatternMap(java.util.List<PatternValue> children){
		this.children = children;
	}
	
	public boolean match(IValue subj, Evaluator ev){
		throw new RascalBug("PatternMap.match not implemented");
	}
}

/* package */ class PatternQualifiedName extends BasicPattern implements PatternValue {
	private org.meta_environment.rascal.ast.QualifiedName name;
	
	PatternQualifiedName(org.meta_environment.rascal.ast.QualifiedName qualifiedName){
		this.name = qualifiedName;
	}
	
	public boolean match(IValue subj, Evaluator ev){
        EvalResult patRes = ev.env.getVariable(name);
         
        if((patRes != null) && (patRes.value != null)){
        	 IValue patVal = patRes.value;
        	 if (subj.getType().isSubtypeOf(patVal.getType())) {
        		 return ev.equals(ev.result(subj), ev.result(patVal));
        	 } else {
        		 return false;
        	 }
         } else {
        	 ev.env.storeVariable(name,ev.result(subj.getType(), subj));
        	 return true;
         }
	}
}

/* package */class PatternTypedVariable extends BasicPattern implements PatternValue {
	private Name name;
	org.eclipse.imp.pdb.facts.type.Type type;

	PatternTypedVariable(org.eclipse.imp.pdb.facts.type.Type type2, Name name) {
		this.type = type2;
		this.name = name;
	}

	public boolean match(IValue subj, Evaluator ev) {
		if (subj.getType().isSubtypeOf(type)) {
			ev.env.storeVariable(name, ev.result(type, subj));
			return true;
		}
		return false;
	}
}

public class PatternEvaluator extends NullASTVisitor<PatternValue> {

	private Evaluator ev;
	
	PatternEvaluator(Evaluator evaluator){
		ev = evaluator;
	}
	
	@Override
	public PatternValue visitExpressionLiteral(Literal x) {
		return new PatternLiteral(x.getLiteral().accept(ev).value);
	}
	
	
	@Override
	public PatternValue visitExpressionCallOrTree(CallOrTree x) {
		return new PatternTree(x.getQualifiedName(), visitElements(x.getArguments()));
	}
	
	private java.util.List<PatternValue> visitElements(java.util.List<org.meta_environment.rascal.ast.Expression> elements){
		ArrayList<PatternValue> args = new java.util.ArrayList<PatternValue>(elements.size());
		
		int i = 0;
		for(org.meta_environment.rascal.ast.Expression e : elements){
			args.add(i++, e.accept(this));
		}
		return args;
	}
	
	@Override
	public PatternValue visitExpressionList(List x) {
		return new PatternList(visitElements(x.getElements()));
	}
	
	@Override
	public PatternValue visitExpressionSet(Set x) {
		return new PatternSet(visitElements(x.getElements()));
	}
	
	@Override
	public PatternValue visitExpressionTuple(Tuple x) {
		return new PatternTuple(visitElements(x.getElements()));
	}
	
	@Override
	public PatternValue visitExpressionMap(Map x) {
		throw new RascalBug("Map in pattern not yet implemented");
	}
	
	@Override
	public PatternValue visitExpressionQualifiedName(QualifiedName x) {
		return new PatternQualifiedName(x.getQualifiedName());
	}
	
	@Override
	public PatternValue visitExpressionTypedVariable(TypedVariable x) {
		return new PatternTypedVariable(x.getType().accept(ev.te), x.getName());
	}
}
