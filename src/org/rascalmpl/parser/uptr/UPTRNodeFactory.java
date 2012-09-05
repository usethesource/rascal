package org.rascalmpl.parser.uptr;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.out.INodeConstructorFactory;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

public class UPTRNodeFactory implements INodeConstructorFactory<IConstructor, ISourceLocation>{
	private final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	
	public UPTRNodeFactory(){
		super();
	}

	public IConstructor createCharNode(int charNumber){
		return VF.constructor(Factory.Tree_Char, VF.integer(charNumber));
	}

	public IConstructor createLiteralNode(int[] characters, Object production){
		IListWriter listWriter = VF.listWriter(Factory.Tree);
		for(int i = characters.length - 1; i >= 0; --i){
			listWriter.insert(VF.constructor(Factory.Tree_Char, VF.integer(characters[i])));
		}
		
		return VF.constructor(Factory.Tree_Appl, (IConstructor) production, listWriter.done());
	}
	
	private IConstructor buildAppl(ArrayList<IConstructor> children, Object production){
		IListWriter childrenListWriter = VF.listWriter(Factory.Tree);
		for(int i = children.size() - 1; i >= 0; --i){
			childrenListWriter.insert(children.get(i));
		}
		
		return VF.constructor(Factory.Tree_Appl, (IConstructor) production, childrenListWriter.done());
	}

	public IConstructor createSortNode(ArrayList<IConstructor> children, Object production){
		return buildAppl(children, production);
	}

	public IConstructor createSubListNode(ArrayList<IConstructor> children, Object production){
		return buildAppl(children, production);
	}

	public IConstructor createListNode(ArrayList<IConstructor> children, Object production){
		return buildAppl(children, production);
		
	}
	
	private IConstructor buildAmbiguityNode(ArrayList<IConstructor> alternatives){
		ISetWriter ambSublist = VF.setWriter(Factory.Tree);
		for(int i = alternatives.size() - 1; i >= 0; --i){
			ambSublist.insert(alternatives.get(i));
		}
		
		return VF.constructor(Factory.Tree_Amb, ambSublist.done());
	}

	public IConstructor createAmbiguityNode(ArrayList<IConstructor> alternatives){
		return buildAmbiguityNode(alternatives);
	}

	public IConstructor createSubListAmbiguityNode(ArrayList<IConstructor> alternatives){
		return buildAmbiguityNode(alternatives);
	}

	public IConstructor createListAmbiguityNode(ArrayList<IConstructor> alternatives){
		return buildAmbiguityNode(alternatives);
	}
	
	private IConstructor buildCycle(int depth, Object production){
		return VF.constructor(Factory.Tree_Cycle, ProductionAdapter.getType((IConstructor) production), VF.integer(depth));
	}

	public IConstructor createCycleNode(int depth, Object production){
		return buildCycle(depth, production);
	}

	public IConstructor createSubListCycleNode(Object production){
		return buildCycle(1, production);
	}

	public IConstructor createRecoveryNode(int[] characters){
		IListWriter listWriter = VF.listWriter(Factory.Tree);
		for(int i = characters.length - 1; i >= 0; --i){
			listWriter.insert(VF.constructor(Factory.Tree_Char, VF.integer(characters[i])));
		}
		
		return VF.constructor(Factory.Tree_Appl, Factory.Production_Skipped.make(VF), listWriter.done());
	}
	
	public ISourceLocation createPositionInformation(URI input, int offset, int endOffset, PositionStore positionStore){
		int beginLine = positionStore.findLine(offset);
		int endLine = positionStore.findLine(endOffset);
		return VF.sourceLocation(input, offset, endOffset - offset, beginLine + 1, endLine + 1, positionStore.getColumn(offset, beginLine), positionStore.getColumn(endOffset, endLine));
	}
	
	public IConstructor addPositionInformation(IConstructor node, ISourceLocation location){
		return node.setAnnotation(Factory.Location, location);
	}
	
	public ArrayList<IConstructor> getChildren(IConstructor node){
		IList args = TreeAdapter.getArgs(node);
		ArrayList<IConstructor> children = new ArrayList<IConstructor>(args.length());
		for(int i = 0; i < args.length(); ++i){
			children.add((IConstructor) args.get(i));
		}
		return children;
	}
	
	public Object getRhs(Object production){
		return ProductionAdapter.getType((IConstructor) production);
	}
	
	public boolean isAmbiguityNode(IConstructor node){
		return TreeAdapter.isAmb(node);
	}
	
	public Object getProductionFromNode(IConstructor node){
		return TreeAdapter.getProduction(node);
	}
}
