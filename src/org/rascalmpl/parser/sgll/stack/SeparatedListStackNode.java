package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.IGLL;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.INode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.LinearIntegerKeyedMap;

public final class SeparatedListStackNode extends AbstractStackNode implements IListStackNode{
	private final IConstructor symbol;

	private final AbstractStackNode child;
	private final AbstractStackNode[] separators;
	private final boolean isPlusList;
	
	private ContainerNode result;
	
	public SeparatedListStackNode(int id, IConstructor symbol, AbstractStackNode child, AbstractStackNode[] separators, boolean isPlusList){
		super(id);
		
		this.symbol = symbol;
		
		this.child = child;
		this.separators = separators;
		this.isPlusList = isPlusList;
	}
	
	private SeparatedListStackNode(SeparatedListStackNode original, int newId){
		super(newId);
		
		symbol = original.symbol;

		child = original.child;
		separators = original.separators;
		isPlusList = original.isPlusList;
	}
	
	private SeparatedListStackNode(SeparatedListStackNode original){
		super(original);
		
		symbol = original.symbol;

		child = original.child;
		separators = original.separators;
		isPlusList = original.isPlusList;
	}
	
	private SeparatedListStackNode(SeparatedListStackNode original, LinearIntegerKeyedMap<ArrayList<Link>> prefixes){
		super(original, prefixes);
		
		symbol = original.symbol;

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
		AbstractStackNode psn = child.getCleanCopy();
		SeparatedListStackNode slpsn = new SeparatedListStackNode(this, id | IGLL.LIST_LIST_FLAG);
		
		AbstractStackNode from = psn;
		for(int i = 0; i < separators.length; i++){
			AbstractStackNode to = separators[i].getCleanCopy();
			from.addNext(to);
			from = to;
		}
		from.addNext(slpsn);
		
		psn.addEdge(this);
		slpsn.addEdge(this);
		
		psn.setStartLocation(startLocation);
		psn.setParentProduction(symbol);
		slpsn.setParentProduction(symbol);
		
		if(isPlusList){
			return new AbstractStackNode[]{psn};
		}
		
		EpsilonStackNode epsn = new EpsilonStackNode(DEFAULT_LIST_EPSILON_ID);
		epsn.addEdge(this);
		epsn.setStartLocation(startLocation);
		epsn.setParentProduction(symbol);
		
		return new AbstractStackNode[]{psn, epsn};
	}
	
	public INode getResult(){
		return result;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(symbol);
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(',');
		sb.append('?');
		sb.append(')');
		
		return sb.toString();
	}
}
