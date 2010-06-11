package org.rascalmpl.parser.sgll.result.struct;

import org.rascalmpl.parser.sgll.result.INode;
import org.rascalmpl.parser.sgll.util.ArrayList;

public class Link{
	public final ArrayList<Link> prefixes;
	public final INode node;
	public final int productionStart;
	
	public Link(ArrayList<Link> prefixes, INode node, int productionStart){
		super();
		
		this.prefixes = prefixes;
		this.node = node;
		this.productionStart = productionStart;
	}
}