package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Comprehension extends AbstractAST { 
  public java.util.List<org.rascalmpl.ast.Expression> getResults() { throw new UnsupportedOperationException(); } public java.util.List<org.rascalmpl.ast.Expression> getGenerators() { throw new UnsupportedOperationException(); } public boolean hasResults() { return false; } public boolean hasGenerators() { return false; } public boolean isSet() { return false; }
static public class Set extends Comprehension {
/** "{" results:{Expression ","}+ "|" generators:{Expression ","}+ "}" -> Comprehension {cons("Set")} */
	public Set(INode node, java.util.List<org.rascalmpl.ast.Expression> results, java.util.List<org.rascalmpl.ast.Expression> generators) {
		this.node = node;
		this.results = results;
		this.generators = generators;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitComprehensionSet(this);
	}

	@Override
	public boolean isSet() { return true; }

	@Override
	public boolean hasResults() { return true; }
	@Override
	public boolean hasGenerators() { return true; }

private final java.util.List<org.rascalmpl.ast.Expression> results;
	@Override
	public java.util.List<org.rascalmpl.ast.Expression> getResults() { return results; }
	private final java.util.List<org.rascalmpl.ast.Expression> generators;
	@Override
	public java.util.List<org.rascalmpl.ast.Expression> getGenerators() { return generators; }	
}
static public class Ambiguity extends Comprehension {
  private final java.util.List<org.rascalmpl.ast.Comprehension> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Comprehension> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Comprehension> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitComprehensionAmbiguity(this);
  }
} public boolean isList() { return false; }
static public class List extends Comprehension {
/** "[" results:{Expression ","}+ "|" generators:{Expression ","}+ "]" -> Comprehension {cons("List")} */
	public List(INode node, java.util.List<org.rascalmpl.ast.Expression> results, java.util.List<org.rascalmpl.ast.Expression> generators) {
		this.node = node;
		this.results = results;
		this.generators = generators;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitComprehensionList(this);
	}

	@Override
	public boolean isList() { return true; }

	@Override
	public boolean hasResults() { return true; }
	@Override
	public boolean hasGenerators() { return true; }

private final java.util.List<org.rascalmpl.ast.Expression> results;
	@Override
	public java.util.List<org.rascalmpl.ast.Expression> getResults() { return results; }
	private final java.util.List<org.rascalmpl.ast.Expression> generators;
	@Override
	public java.util.List<org.rascalmpl.ast.Expression> getGenerators() { return generators; }	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public org.rascalmpl.ast.Expression getFrom() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.Expression getTo() { throw new UnsupportedOperationException(); } public boolean hasFrom() { return false; }
	public boolean hasTo() { return false; } public boolean isMap() { return false; }
static public class Map extends Comprehension {
/** "(" from:Expression ":" to:Expression "|" generators:{Expression ","}+ ")" -> Comprehension {cons("Map")} */
	public Map(INode node, org.rascalmpl.ast.Expression from, org.rascalmpl.ast.Expression to, java.util.List<org.rascalmpl.ast.Expression> generators) {
		this.node = node;
		this.from = from;
		this.to = to;
		this.generators = generators;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitComprehensionMap(this);
	}

	@Override
	public boolean isMap() { return true; }

	@Override
	public boolean hasFrom() { return true; }
	@Override
	public boolean hasTo() { return true; }
	@Override
	public boolean hasGenerators() { return true; }

private final org.rascalmpl.ast.Expression from;
	@Override
	public org.rascalmpl.ast.Expression getFrom() { return from; }
	private final org.rascalmpl.ast.Expression to;
	@Override
	public org.rascalmpl.ast.Expression getTo() { return to; }
	private final java.util.List<org.rascalmpl.ast.Expression> generators;
	@Override
	public java.util.List<org.rascalmpl.ast.Expression> getGenerators() { return generators; }	
}
}