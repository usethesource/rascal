package org.rascalmpl.parser.uptr;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.out.INodeConstructorFactory;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.rascalmpl.values.uptr.TreeAdapter;

public class UPTRNodeFactory implements INodeConstructorFactory<IConstructor, ISourceLocation>{
	private final static RascalValueFactory VF = (RascalValueFactory) ValueFactoryFactory.getValueFactory();
	
	public UPTRNodeFactory(){
		super();
	}

	public IConstructor createCharNode(int charNumber){
		return VF.character(charNumber);
	}

	public IConstructor createLiteralNode(int[] characters, Object production){
		IListWriter listWriter = VF.listWriter();
		for(int i = characters.length - 1; i >= 0; --i){
			listWriter.insert(VF.character(characters[i]));
		}
		
		return VF.appl((IConstructor) production, listWriter.done());
	}
	
	private static IConstructor buildAppl(ArrayList<IConstructor> children, Object production){
		IListWriter childrenListWriter = VF.listWriter();
		for(int i = children.size() - 1; i >= 0; --i){
			childrenListWriter.insert(children.get(i));
		}
		
		return VF.appl((IConstructor) production, childrenListWriter.done());
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
	
	private static IConstructor buildAmbiguityNode(ArrayList<IConstructor> alternatives){
		ISetWriter ambSublist = VF.setWriter();
		for(int i = alternatives.size() - 1; i >= 0; --i){
			ambSublist.insert(alternatives.get(i));
		}
		
		return VF.amb(ambSublist.done());
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
	
	private static IConstructor buildCycle(int depth, Object production){
		return VF.cycle(ProductionAdapter.getType((IConstructor) production), depth);
	}

	public IConstructor createCycleNode(int depth, Object production){
		return buildCycle(depth, production);
	}

	public IConstructor createSubListCycleNode(Object production){
		return buildCycle(1, production);
	}

	public IConstructor createRecoveryNode(int[] characters){
		IListWriter listWriter = VF.listWriter();
		for(int i = characters.length - 1; i >= 0; --i){
			listWriter.insert(VF.character(characters[i]));
		}
		
		return VF.appl(VF.constructor(RascalValueFactory.Production_Skipped), listWriter.done());
	}
	
	public ISourceLocation createPositionInformation(URI input, int offset, int endOffset, PositionStore positionStore){
		int beginLine = positionStore.findLine(offset);
		int endLine = positionStore.findLine(endOffset);
		return VF.sourceLocation(input, offset, endOffset - offset, beginLine + 1, endLine + 1, positionStore.getColumn(offset, beginLine), positionStore.getColumn(endOffset, endLine));
	}
	
	public IConstructor addPositionInformation(IConstructor node, ISourceLocation location){
		return node.asAnnotatable().setAnnotation(RascalValueFactory.Location, location);
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
