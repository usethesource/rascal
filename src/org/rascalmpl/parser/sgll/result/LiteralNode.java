package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

public class LiteralNode implements INode{
	private final static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	private final IConstructor production;
	private final char[] content;
	
	public LiteralNode(IConstructor production, char[] content){
		super();
		
		this.production = production;
		this.content = content;
	}
	
	public void addAlternative(IConstructor production, INode[] children){
		throw new UnsupportedOperationException();
	}
	
	public boolean isEpsilon(){
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
		for(int i = 1; i < content.length; i++){
			sb.append(',');
			printCharacter(CharNode.getNumericCharValue(content[i]), sb);
		}
		sb.append(']');
		sb.append(')');
		
		return sb.toString();
	}
	
	public IValue toTerm(){
		int numberOfCharacters = content.length;
		IValue[] characters = new IValue[numberOfCharacters];
		for(int i = 0; i < numberOfCharacters; i++){
			IInteger characterValue = vf.integer(CharNode.getNumericCharValue(content[i]));
			characters[i] = vf.constructor(Factory.Tree_Char, characterValue);
		}
		
		IListWriter listWriter = vf.listWriter(Factory.Args);
		listWriter.append(characters);
		IList args = listWriter.done();
		
		return vf.constructor(Factory.Tree_Appl, production, args);
	}
}
