package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class StructuredType extends AbstractAST { 
  public org.meta_environment.rascal.ast.TypeArg getTypeArg() { throw new UnsupportedOperationException(); } public boolean hasTypeArg() { return false; } public boolean isList() { return false; }
static public class List extends StructuredType {
/* "list" "[" typeArg:TypeArg "]" -> StructuredType {cons("List")} */
	private List() { }
	/*package*/ List(ITree tree, org.meta_environment.rascal.ast.TypeArg typeArg) {
		this.tree = tree;
		this.typeArg = typeArg;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStructuredTypeList(this);
	}

	@Override
	public boolean isList() { return true; }

	@Override
	public boolean hasTypeArg() { return true; }

private org.meta_environment.rascal.ast.TypeArg typeArg;
	@Override
	public org.meta_environment.rascal.ast.TypeArg getTypeArg() { return typeArg; }
	private void $setTypeArg(org.meta_environment.rascal.ast.TypeArg x) { this.typeArg = x; }
	public List setTypeArg(org.meta_environment.rascal.ast.TypeArg x) { 
		List z = new List();
 		z.$setTypeArg(x);
		return z;
	}	
}
static public class Ambiguity extends StructuredType {
  private final java.util.List<org.meta_environment.rascal.ast.StructuredType> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.StructuredType> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.StructuredType> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitStructuredTypeAmbiguity(this);
  }
} public boolean isSet() { return false; }
static public class Set extends StructuredType {
/* "set" "[" typeArg:TypeArg "]" -> StructuredType {cons("Set")} */
	private Set() { }
	/*package*/ Set(ITree tree, org.meta_environment.rascal.ast.TypeArg typeArg) {
		this.tree = tree;
		this.typeArg = typeArg;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStructuredTypeSet(this);
	}

	@Override
	public boolean isSet() { return true; }

	@Override
	public boolean hasTypeArg() { return true; }

private org.meta_environment.rascal.ast.TypeArg typeArg;
	@Override
	public org.meta_environment.rascal.ast.TypeArg getTypeArg() { return typeArg; }
	private void $setTypeArg(org.meta_environment.rascal.ast.TypeArg x) { this.typeArg = x; }
	public Set setTypeArg(org.meta_environment.rascal.ast.TypeArg x) { 
		Set z = new Set();
 		z.$setTypeArg(x);
		return z;
	}	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public org.meta_environment.rascal.ast.TypeArg getFirst() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.TypeArg getSecond() { throw new UnsupportedOperationException(); }
public boolean hasFirst() { return false; }
	public boolean hasSecond() { return false; }
public boolean isMap() { return false; }
static public class Map extends StructuredType {
/* "map" "[" first:TypeArg "," second:TypeArg "]" -> StructuredType {cons("Map")} */
	private Map() { }
	/*package*/ Map(ITree tree, org.meta_environment.rascal.ast.TypeArg first, org.meta_environment.rascal.ast.TypeArg second) {
		this.tree = tree;
		this.first = first;
		this.second = second;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStructuredTypeMap(this);
	}

	@Override
	public boolean isMap() { return true; }

	@Override
	public boolean hasFirst() { return true; }
	@Override
	public boolean hasSecond() { return true; }

private org.meta_environment.rascal.ast.TypeArg first;
	@Override
	public org.meta_environment.rascal.ast.TypeArg getFirst() { return first; }
	private void $setFirst(org.meta_environment.rascal.ast.TypeArg x) { this.first = x; }
	public Map setFirst(org.meta_environment.rascal.ast.TypeArg x) { 
		Map z = new Map();
 		z.$setFirst(x);
		return z;
	}
	private org.meta_environment.rascal.ast.TypeArg second;
	@Override
	public org.meta_environment.rascal.ast.TypeArg getSecond() { return second; }
	private void $setSecond(org.meta_environment.rascal.ast.TypeArg x) { this.second = x; }
	public Map setSecond(org.meta_environment.rascal.ast.TypeArg x) { 
		Map z = new Map();
 		z.$setSecond(x);
		return z;
	}	
} public java.util.List<org.meta_environment.rascal.ast.TypeArg> getArguments() { throw new UnsupportedOperationException(); } public boolean hasArguments() { return false; } public boolean isRelation() { return false; }
static public class Relation extends StructuredType {
/* "rel" "[" arguments:{TypeArg ","}+ "]" -> StructuredType {cons("Relation")} */
	private Relation() { }
	/*package*/ Relation(ITree tree, java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments) {
		this.tree = tree;
		this.arguments = arguments;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStructuredTypeRelation(this);
	}

	@Override
	public boolean isRelation() { return true; }

	@Override
	public boolean hasArguments() { return true; }

private java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments;
	@Override
	public java.util.List<org.meta_environment.rascal.ast.TypeArg> getArguments() { return arguments; }
	private void $setArguments(java.util.List<org.meta_environment.rascal.ast.TypeArg> x) { this.arguments = x; }
	public Relation setArguments(java.util.List<org.meta_environment.rascal.ast.TypeArg> x) { 
		Relation z = new Relation();
 		z.$setArguments(x);
		return z;
	}	
} public boolean isTuple() { return false; }
static public class Tuple extends StructuredType {
/* "tuple" "[" arguments:{TypeArg ","}+  "]" -> StructuredType {cons("Tuple")} */
	private Tuple() { }
	/*package*/ Tuple(ITree tree, java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments) {
		this.tree = tree;
		this.arguments = arguments;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStructuredTypeTuple(this);
	}

	@Override
	public boolean isTuple() { return true; }

	@Override
	public boolean hasArguments() { return true; }

private java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments;
	@Override
	public java.util.List<org.meta_environment.rascal.ast.TypeArg> getArguments() { return arguments; }
	private void $setArguments(java.util.List<org.meta_environment.rascal.ast.TypeArg> x) { this.arguments = x; }
	public Tuple setArguments(java.util.List<org.meta_environment.rascal.ast.TypeArg> x) { 
		Tuple z = new Tuple();
 		z.$setArguments(x);
		return z;
	}	
}
}