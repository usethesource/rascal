package org.rascalmpl.parser.sgll.result;

import org.rascalmpl.parser.sgll.util.ArrayList;

public class ContainerNode implements INode{
	private final String production;
	private INode[] firstAlternative;
	private ArrayList<INode[]> alternatives;
	
	public ContainerNode(String production){
		super();
		
		this.production = production;
	}
	
	public void addAlternative(INode[] children){
		if(firstAlternative == null){
			firstAlternative = children;
		}else{
			if(alternatives == null) alternatives = new ArrayList<INode[]>(1);
			alternatives.add(children);
		}
	}
	
	public boolean isEpsilon(){
		return false;
	}
	
	private void printAlternative(INode[] children, StringBuilder sb){
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
			printAlternative(firstAlternative, sb);
		}else{
			sb.append("amb({");
			for(int i = alternatives.size() - 1; i >= 1; i--){
				printAlternative(alternatives.get(i), sb);
				sb.append(',');
			}
			printAlternative(alternatives.get(0), sb);
			sb.append(',');
			printAlternative(firstAlternative, sb);
			sb.append('}');
			sb.append(')');
		}
		
		return sb.toString();
	}
}
