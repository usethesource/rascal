package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Strategy extends AbstractAST { 
  public boolean isTopDown() { return false; }
static public class TopDown extends Strategy {
/** "top-down" -> Strategy {cons("TopDown")} */
	public TopDown(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStrategyTopDown(this);
	}

	public boolean isTopDown() { return true; }	
}
static public class Ambiguity extends Strategy {
  private final java.util.List<org.rascalmpl.ast.Strategy> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Strategy> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Strategy> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitStrategyAmbiguity(this);
  }
} 
public boolean isTopDownBreak() { return false; }
static public class TopDownBreak extends Strategy {
/** "top-down-break" -> Strategy {cons("TopDownBreak")} */
	public TopDownBreak(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStrategyTopDownBreak(this);
	}

	public boolean isTopDownBreak() { return true; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isBottomUp() { return false; }
static public class BottomUp extends Strategy {
/** "bottom-up" -> Strategy {cons("BottomUp")} */
	public BottomUp(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStrategyBottomUp(this);
	}

	public boolean isBottomUp() { return true; }	
} 
public boolean isBottomUpBreak() { return false; }
static public class BottomUpBreak extends Strategy {
/** "bottom-up-break" -> Strategy {cons("BottomUpBreak")} */
	public BottomUpBreak(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStrategyBottomUpBreak(this);
	}

	public boolean isBottomUpBreak() { return true; }	
} 
public boolean isOutermost() { return false; }
static public class Outermost extends Strategy {
/** "outermost" -> Strategy {cons("Outermost")} */
	public Outermost(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStrategyOutermost(this);
	}

	public boolean isOutermost() { return true; }	
} 
public boolean isInnermost() { return false; }
static public class Innermost extends Strategy {
/** "innermost" -> Strategy {cons("Innermost")} */
	public Innermost(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStrategyInnermost(this);
	}

	public boolean isInnermost() { return true; }	
}
}