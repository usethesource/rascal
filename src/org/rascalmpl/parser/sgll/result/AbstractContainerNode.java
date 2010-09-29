package org.rascalmpl.parser.sgll.result;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;

public abstract class AbstractContainerNode extends AbstractNode{
	protected final URI input;
	protected final int offset;
	protected final int endOffset;
	
	protected boolean rejected;
	
	protected final boolean isNullable;
	protected final boolean isSeparator;
	protected final boolean isLayout;

	protected Link firstAlternative;
	protected IConstructor firstProduction;
	protected ArrayList<Link> alternatives;
	protected ArrayList<IConstructor> productions;
	
	public AbstractContainerNode(URI input, int offset, int endOffset, boolean isNullable, boolean isSeparator, boolean isLayout){
		super();
		
		this.input = input;
		this.offset = offset;
		this.endOffset = endOffset;
		
		this.isNullable = isNullable;
		this.isSeparator = isSeparator;
		this.isLayout = isLayout;
	}
	
	public void addAlternative(IConstructor production, Link children){
		if(firstAlternative == null){
			firstAlternative = children;
			firstProduction = production;
		}else{
			if(alternatives == null){
				alternatives = new ArrayList<Link>(1);
				productions = new ArrayList<IConstructor>(1);
			}
			alternatives.add(children);
			productions.add(production);
		}
	}
	
	public boolean isEpsilon(){
		return false;
	}
	
	public boolean isNullable(){
		return isNullable;
	}
	
	public boolean isSeparator(){
		return isSeparator;
	}
	
	public void setRejected(){
		rejected = true;
		
		// Clean up.
		firstAlternative = null;
		alternatives = null;
		productions = null;
	}
	
	public boolean isRejected(){
		return rejected;
	}
}
