package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.IndexedStack;
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
	
	public IValue toTerm(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, LocationStore locationStore){
		int numberOfCharacters = content.length;
		IListWriter listWriter = vf.listWriter(Factory.Tree);
		for(int i = 0; i < numberOfCharacters; ++i){
			char character = content[i];
			
			if(character == END_LINE_CHAR){
				locationStore.hitEndLine();
			}else if(character == CARRIAGE_RETURN_CHAR){
				locationStore.hitCarriageReturn();
			}else{
				locationStore.hitCharacter();
			}
			
			IInteger characterValue = vf.integer(CharNode.getNumericCharValue(character));
			listWriter.append(vf.constructor(Factory.Tree_Char, characterValue));
		}
		
		return vf.constructor(Factory.Tree_Appl, production, listWriter.done());
	}
}
