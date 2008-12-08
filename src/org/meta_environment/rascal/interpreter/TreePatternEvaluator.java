package org.meta_environment.rascal.interpreter;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
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

/* package */ class BasicTreePattern {
	
	boolean matchChildren(Iterator<IValue> subjChildren, Iterator<PatternValue> patChildren, Evaluator ev){
		while (patChildren.hasNext()) {
			if (!patChildren.next().match(subjChildren.next(), ev)){
				return false;
			}
		}
		return true;
	}
}
/* package */ class TreePatternLiteral extends BasicTreePattern implements PatternValue {
	private IValue literal;
	
	TreePatternLiteral(IValue literal){
		this.literal = literal;
	}
	
	public boolean match(IValue subj, Evaluator ev){
			if (subj.getType().isSubtypeOf(literal.getType())) {
				return ev.equals(ev.result(subj), ev.result(literal));
			}
			return false;
	}
}

/* package */ class TreePatternTree extends BasicTreePattern implements PatternValue {
	private org.meta_environment.rascal.ast.QualifiedName name;
	private java.util.List<PatternValue> children;
	
	TreePatternTree(org.meta_environment.rascal.ast.QualifiedName qualifiedName, java.util.List<PatternValue> children){
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

/* package */ class TreePatternList extends BasicTreePattern implements PatternValue {
	private java.util.List<PatternValue> children;
	
	TreePatternList(java.util.List<PatternValue> children){
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

/* package */ class TreePatternSet extends BasicTreePattern implements PatternValue {
	private java.util.List<PatternValue> children;
	
	TreePatternSet(java.util.List<PatternValue> children){
		this.children = children;
	}
	
	public boolean match(IValue subj, Evaluator ev){
		throw new RascalBug("PatternSet.match not implemented");
	}
}

/* package */ class TreePatternTuple extends BasicTreePattern implements PatternValue {
	private java.util.List<PatternValue> children;
	
	TreePatternTuple(java.util.List<PatternValue> children){
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

/* package */ class TreePatternMap extends BasicTreePattern implements PatternValue {
	private java.util.List<PatternValue> children;
	
	TreePatternMap(java.util.List<PatternValue> children){
		this.children = children;
	}
	
	public boolean match(IValue subj, Evaluator ev){
		throw new RascalBug("PatternMap.match not implemented");
	}
}

/* package */ class TreePatternQualifiedName extends BasicTreePattern implements PatternValue {
	private org.meta_environment.rascal.ast.QualifiedName name;
	
	TreePatternQualifiedName(org.meta_environment.rascal.ast.QualifiedName qualifiedName){
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

/* package */class TreePatternTypedVariable extends BasicTreePattern implements PatternValue {
	private Name name;
	org.eclipse.imp.pdb.facts.type.Type type;

	TreePatternTypedVariable(org.eclipse.imp.pdb.facts.type.Type type2, Name name) {
		this.type = type2;
		this.name = name;
		System.err.println("TypedVar: " + type2 + ", " + name);
	}

	public boolean match(IValue subj, Evaluator ev) {
		if (subj.getType().isSubtypeOf(type)) {
			ev.env.storeVariable(name, ev.result(type, subj));
			return true;
		}
		return false;
	}
}

public class TreePatternEvaluator extends NullASTVisitor<PatternValue> {

	private Evaluator ev;
	
	TreePatternEvaluator(Evaluator evaluator){
		ev = evaluator;
	}
	
	@Override
	public PatternValue visitExpressionLiteral(Literal x) {
		return new TreePatternLiteral(x.getLiteral().accept(ev).value);
	}
	
	
	@Override
	public PatternValue visitExpressionCallOrTree(CallOrTree x) {
		return new TreePatternTree(x.getQualifiedName(), visitElements(x.getArguments()));
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
		return new TreePatternList(visitElements(x.getElements()));
	}
	
	@Override
	public PatternValue visitExpressionSet(Set x) {
		return new TreePatternSet(visitElements(x.getElements()));
	}
	
	@Override
	public PatternValue visitExpressionTuple(Tuple x) {
		return new TreePatternTuple(visitElements(x.getElements()));
	}
	
	@Override
	public PatternValue visitExpressionMap(Map x) {
		throw new RascalBug("Map in pattern not yet implemented");
	}
	
	@Override
	public PatternValue visitExpressionQualifiedName(QualifiedName x) {
		return new TreePatternQualifiedName(x.getQualifiedName());
	}
	
	@Override
	public PatternValue visitExpressionTypedVariable(TypedVariable x) {
		System.err.println("x = " + x);
		System.err.println("type = " + x.getType());
		System.err.println("treeType = " + ev.tf.treeType());
		System.err.println("ev.te = " + ev.te);
		return new TreePatternTypedVariable(x.getType().accept(ev.te), x.getName());
	}
}
