package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

public class LiteralNode extends AbstractNode{
	private final static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	private final IConstructor production;
	private final char[] content;
	
	public LiteralNode(IConstructor production, char[] content){
		super();
		
		this.production = production;
		this.content = content;
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
	
	private void printCharacter(int character, StringBuilder sb){
		sb.append("char(");
		sb.append(character);
		sb.append(')');
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("appl(");
		sb.append(production);
		sb.append(',');
		sb.append('[');
		printCharacter(CharNode.getNumericCharValue(content[0]), sb);
		for(int i = 1; i < content.length; ++i){
			sb.append(',');
			printCharacter(CharNode.getNumericCharValue(content[i]), sb);
		}
		sb.append(']');
		sb.append(')');
		
		return sb.toString();
	}
	
	public IConstructor toTerm(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore){
		int numberOfCharacters = content.length;
		
		IListWriter listWriter = vf.listWriter(Factory.Tree);
		for(int i = 0; i < numberOfCharacters; ++i){
			listWriter.append(vf.constructor(Factory.Tree_Char, vf.integer(CharNode.getNumericCharValue(content[i]))));
		}
		
		return vf.constructor(Factory.Tree_Appl, production, listWriter.done());
	}
}
