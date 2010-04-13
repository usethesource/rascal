package org.rascalmpl.parser.sgll.result;

import org.rascalmpl.parser.sgll.util.ArrayList;

public class ContainerNode implements INode{
	private final String name;
	private INode[] firstAlternative;
	private ArrayList<INode[]> alternatives;
	
	public ContainerNode(String name){
		super();
		
		this.name = name;
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
		int nrOfChildren = children.length;
		
		sb.append(name);
		sb.append('(');
		sb.append(children[0]);
		for(int i = 1; i < nrOfChildren; i++){
			sb.append(',');
			sb.append(children[i]);
		}
		sb.append(')');
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		if(alternatives == null){
			printAlternative(firstAlternative, sb);
		}else{
			sb.append('[');
			for(int i = alternatives.size() - 1; i >= 1; i--){
				printAlternative(alternatives.get(i), sb);
				sb.append(',');
			}
			printAlternative(alternatives.get(0), sb);
			sb.append(',');
			printAlternative(firstAlternative, sb);
			sb.append(']');
		}
		
		return sb.toString();
	}
}
