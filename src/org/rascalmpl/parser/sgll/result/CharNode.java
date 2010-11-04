package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.IActionExecutor;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

public class CharNode extends AbstractNode{
	private final static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	private final char character;
	
	public CharNode(char character){
		super();
		
		this.character = character;
	}
	
	public void addAlternative(IConstructor production, Link children){
		throw new UnsupportedOperationException();
	}
	
	public boolean isEpsilon(){
		return false;
	}
	
	public boolean isEmpty(){
		return false;
	}
	
	public boolean isSeparator(){
		return false;
	}
	
	public void setRejected(){
		throw new UnsupportedOperationException();
	}
	
	public boolean isRejected(){
		return false;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("char(");
		sb.append(getNumericCharValue(character));
		sb.append(')');
		
		return sb.toString();
	}
	
	public IConstructor toTerm(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, IActionExecutor actionExecutor){
		return vf.constructor(Factory.Tree_Char, vf.integer(getNumericCharValue(character)));
	}
	
	public static int getNumericCharValue(char character){
		return (character > 127) ? Character.getNumericValue(character) : ((int) character); // Just ignore the Unicode garbage when possible.
	}
}
