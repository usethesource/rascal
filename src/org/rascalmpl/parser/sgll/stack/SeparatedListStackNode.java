package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.IGLL;
import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.LinearIntegerKeyedMap;

public final class SeparatedListStackNode extends AbstractStackNode implements IListStackNode{
	private final IConstructor production;

	private final AbstractStackNode child;
	private final AbstractStackNode[] separators;
	private final boolean isPlusList;
	
	private ContainerNode result;
	
	public SeparatedListStackNode(int id, IConstructor production, AbstractStackNode child, AbstractStackNode[] separators, boolean isPlusList){
		super(id);
		
		this.production = production;
		
		this.child = child;
		this.separators = separators;
		this.isPlusList = isPlusList;
	}
	
	private SeparatedListStackNode(SeparatedListStackNode original){
		super(original);
		
		production = original.production;

		child = original.child;
		separators = original.separators;
		isPlusList = original.isPlusList;
	}
	
	private SeparatedListStackNode(SeparatedListStackNode original, LinearIntegerKeyedMap<ArrayList<Link>> prefixes){
		super(original, prefixes);
		
		production = original.production;

		child = original.child;
		separators = original.separators;
		isPlusList = original.isPlusList;
	}
	
	public String getMethodName(){
		throw new UnsupportedOperationException();
	}
	
	public boolean reduce(char[] input){
		throw new UnsupportedOperationException();
	}
	
	public boolean isClean(){
		return (result == null);
	}
	
	public AbstractStackNode getCleanCopy(){
		return new SeparatedListStackNode(this);
	}
	
	public AbstractStackNode getCleanCopyWithPrefix(){
		return new SeparatedListStackNode(this, prefixesMap);
	}
	
	public void setResultStore(ContainerNode resultStore){
		result = resultStore;
	}
	
	public ContainerNode getResultStore(){
		return result;
	}
	
	public int getLength(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode[] getChildren(){
		AbstractStackNode listNode = child.getCleanCopy();
		
		listNode.addEdge(this);
		listNode.addPrefix(null, startLocation);
		listNode.setStartLocation(startLocation);
		listNode.setParentProduction(production);
		
		AbstractStackNode from = listNode;
		AbstractStackNode to = separators[0].getCleanCopy();
		AbstractStackNode firstSeparator = to;
		from.addNext(to);
		from = to;
		for(int i = 1; i < separators.length; i++){
			to = separators[i].getCleanCopy();
			from.addNext(to);
			from = to;
		}
		from.addNext(listNode);
		
		listNode.addNext(firstSeparator);
		
		if(isPlusList){
			return new AbstractStackNode[]{listNode};
		}
		
		EpsilonStackNode empty = new EpsilonStackNode(IGLL.DEFAULT_LIST_EPSILON_ID);
		
		empty.addEdge(this);
		empty.setStartLocation(startLocation);
		empty.setParentProduction(production);
		
		return new AbstractStackNode[]{listNode, empty};
	}
	
	public AbstractNode getResult(){
		return result;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(production);
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(',');
		sb.append('?');
		sb.append(')');
		
		return sb.toString();
	}
}
