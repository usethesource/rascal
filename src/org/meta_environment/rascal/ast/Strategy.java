package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Strategy extends AbstractAST { 
public boolean isTopDown() { return false; }
static public class TopDown extends Strategy {
/* "top-down" -> Strategy {cons("TopDown")} */
	private TopDown() { }
	/*package*/ TopDown(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStrategyTopDown(this);
	}

	public boolean isTopDown() { return true; }	
}
static public class Ambiguity extends Strategy {
  private final java.util.List<org.meta_environment.rascal.ast.Strategy> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.Strategy> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.Strategy> getAlternatives() {
	return alternatives;
  }
} 
public boolean isTopDownBreak() { return false; }
static public class TopDownBreak extends Strategy {
/* "top-down-break" -> Strategy {cons("TopDownBreak")} */
	private TopDownBreak() { }
	/*package*/ TopDownBreak(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStrategyTopDownBreak(this);
	}

	public boolean isTopDownBreak() { return true; }	
} 
public boolean isBottomUp() { return false; }
static public class BottomUp extends Strategy {
/* "bottom-up" -> Strategy {cons("BottomUp")} */
	private BottomUp() { }
	/*package*/ BottomUp(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStrategyBottomUp(this);
	}

	public boolean isBottomUp() { return true; }	
} 
public boolean isBottomUpBreak() { return false; }
static public class BottomUpBreak extends Strategy {
/* "bottom-up-break" -> Strategy {cons("BottomUpBreak")} */
	private BottomUpBreak() { }
	/*package*/ BottomUpBreak(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStrategyBottomUpBreak(this);
	}

	public boolean isBottomUpBreak() { return true; }	
} 
public boolean isOutermost() { return false; }
static public class Outermost extends Strategy {
/* "outermost" -> Strategy {cons("Outermost")} */
	private Outermost() { }
	/*package*/ Outermost(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStrategyOutermost(this);
	}

	public boolean isOutermost() { return true; }	
} 
public boolean isInnermost() { return false; }
static public class Innermost extends Strategy {
/* "innermost" -> Strategy {cons("Innermost")} */
	private Innermost() { }
	/*package*/ Innermost(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStrategyInnermost(this);
	}

	public boolean isInnermost() { return true; }	
}
}