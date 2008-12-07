package org.meta_environment.rascal.interpreter;

import java.util.ArrayList;
import java.util.LinkedList;

import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.ListType;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Type;
import org.meta_environment.rascal.ast.Expression.CallOrTree;
import org.meta_environment.rascal.ast.Expression.List;
import org.meta_environment.rascal.ast.Expression.Literal;
import org.meta_environment.rascal.ast.Expression.Map;
import org.meta_environment.rascal.ast.Expression.QualifiedName;
import org.meta_environment.rascal.ast.Expression.Set;
import org.meta_environment.rascal.ast.Expression.Tuple;
import org.meta_environment.rascal.ast.Expression.TypedVariable;
import org.meta_environment.rascal.ast.Generator.Expression;

/* package */enum PatternKind {UNDEFINED, LITERAL, TREE, LIST, SET, TUPLE, MAP, QUALIFIEDNAME, TYPEDVARIABLE}

/* package */ interface PatternValue {
	
	public PatternKind getKind();
}

/* package */ class PatternLiteral implements PatternValue {
	private final PatternKind kind = PatternKind.LITERAL;
	private IValue literal;
	
	PatternLiteral(IValue literal){
		this.literal = literal;
	}
	
	public PatternKind getKind(){
		return kind;
	}
	
	public IValue getLiteral(){
		return literal;
	}
}

/* package */ class PatternTree implements PatternValue {
	private final PatternKind kind = PatternKind.TREE;
	private org.meta_environment.rascal.ast.QualifiedName name;
	private java.util.List<PatternValue> children;
	
	PatternTree(org.meta_environment.rascal.ast.QualifiedName qualifiedName, java.util.List<PatternValue> children){
		this.name = qualifiedName;
		this.children = children;
	}
	
	public PatternKind getKind(){
		return kind;
	}
	
	public org.meta_environment.rascal.ast.QualifiedName getQualifiedName(){
		return name;
	}
	
	public java.util.List<PatternValue> getChildren(){
		return children;
	}
}

/* package */ class PatternList implements PatternValue {
	private final PatternKind kind = PatternKind.LIST;
	private org.meta_environment.rascal.ast.QualifiedName name;
	private java.util.List<PatternValue> children;
	
	PatternList(java.util.List<PatternValue> children){
		this.children = children;
	}
	
	public PatternKind getKind(){
		return kind;
	}
	
	public java.util.List<PatternValue> getChildren(){
		return children;
	}
}

/* package */ class PatternSet implements PatternValue {
	private final PatternKind kind = PatternKind.SET;
	private java.util.List<PatternValue> children;
	
	PatternSet(java.util.List<PatternValue> children){
		this.children = children;
	}
	
	public PatternKind getKind(){
		return kind;
	}
	
	public java.util.List<PatternValue> getChildren(){
		return children;
	}
}

/* package */ class PatternTuple implements PatternValue {
	private final PatternKind kind = PatternKind.TUPLE;
	private java.util.List<PatternValue> children;
	
	PatternTuple(java.util.List<PatternValue> children){
		this.children = children;
	}
	
	public PatternKind getKind(){
		return kind;
	}
	
	public java.util.List<PatternValue> getChildren(){
		return children;
	}
}

/* package */ class PatternMap implements PatternValue {
	private final PatternKind kind = PatternKind.MAP;
	private java.util.List<PatternValue> children;
	
	PatternMap(java.util.List<PatternValue> children){
		this.children = children;
	}
	
	public PatternKind getKind(){
		return kind;
	}
	
	public java.util.List<PatternValue> getChildren(){
		return children;
	}
}

/* package */ class PatternQualifiedName implements PatternValue {
	private final PatternKind kind = PatternKind.QUALIFIEDNAME;
	private org.meta_environment.rascal.ast.QualifiedName name;
	
	PatternQualifiedName(org.meta_environment.rascal.ast.QualifiedName qualifiedName){
		this.name = qualifiedName;
	}
	public PatternKind getKind(){
		return kind;
	}
	
	public org.meta_environment.rascal.ast.QualifiedName getQualifiedName(){
		return name;
	}
}

/* package */ class PatternTypedVariable implements PatternValue {
	private final PatternKind kind = PatternKind.TYPEDVARIABLE;
	private Name name;
	org.eclipse.imp.pdb.facts.type.Type type;
	
	PatternTypedVariable(org.eclipse.imp.pdb.facts.type.Type type2, Name name){
		this.type = type2;
		this.name = name;
	}
	
	public PatternKind getKind(){
		return kind;
	}
	
	public Name getName(){
		return name;
	}
}

public class PatternEvaluator	extends NullASTVisitor<PatternValue> {

	private Evaluator ev;
	private IValueFactory vf;
	
	PatternEvaluator(Evaluator evaluator){
		ev = evaluator;
		vf = evaluator.vf;
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
