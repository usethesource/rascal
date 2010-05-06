package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.util.ArrayList;

public class ContainerNode implements INode{
	private IConstructor firstProduction;
	private INode[] firstAlternative;
	private ArrayList<IConstructor> productions;
	private ArrayList<INode[]> alternatives;
	
	public ContainerNode(){
		super();
	}
	
	public void addAlternative(IConstructor production, INode[] children){
		if(firstAlternative == null){
			firstProduction = production;
			firstAlternative = children;
		}else{
			if(alternatives == null){
				productions = new ArrayList<IConstructor>(1);
				alternatives = new ArrayList<INode[]>(1);
			}
			productions.add(production);
			alternatives.add(children);
		}
	}
	
	public boolean isEpsilon(){
		return false;
	}
	
	private void printAlternative(IConstructor production, INode[] children, StringBuilder sb){
		sb.append("appl(prod(");
		sb.append(production);
		sb.append(')');
		sb.append(',');
		sb.append('[');
		sb.append(children[0]);
		for(int i = 1; i < children.length; i++){
			sb.append(',');
			sb.append(children[i]);
		}
		sb.append(']');
		sb.append(')');
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		if(alternatives == null){
			printAlternative(firstProduction, firstAlternative, sb);
		}else{
			sb.append("amb({");
			for(int i = alternatives.size() - 1; i >= 1; i--){
				printAlternative(productions.get(i), alternatives.get(i), sb);
				sb.append(',');
			}
			printAlternative(productions.get(0), alternatives.get(0), sb);
			sb.append(',');
			printAlternative(firstProduction, firstAlternative, sb);
			sb.append('}');
			sb.append(')');
		}
		
		return sb.toString();
	}
}
