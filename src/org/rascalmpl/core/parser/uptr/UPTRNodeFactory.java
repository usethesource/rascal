package org.rascalmpl.core.parser.uptr;

import java.net.URI;

import org.rascalmpl.core.errors.Ambiguous;
import org.rascalmpl.core.parser.gtd.location.PositionStore;
import org.rascalmpl.core.parser.gtd.result.out.INodeConstructorFactory;
import org.rascalmpl.core.parser.gtd.util.ArrayList;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import org.rascalmpl.core.values.ValueFactoryFactory;
import org.rascalmpl.core.values.uptr.ITree;
import org.rascalmpl.core.values.uptr.ProductionAdapter;
import org.rascalmpl.core.values.uptr.RascalValueFactory;
import org.rascalmpl.core.values.uptr.TreeAdapter;

public class UPTRNodeFactory implements INodeConstructorFactory<ITree, ISourceLocation>{
	private final static RascalValueFactory VF = (RascalValueFactory) ValueFactoryFactory.getValueFactory();
	private boolean allowAmb;
	
	public UPTRNodeFactory(boolean allowAmbiguity){
		super();
		this.allowAmb = allowAmbiguity;
	}

	public ITree createCharNode(int charNumber){
		return VF.character(charNumber);
	}

	public ITree createLiteralNode(int[] characters, Object production){
		IListWriter listWriter = VF.listWriter();
		for(int i = characters.length - 1; i >= 0; --i){
			listWriter.insert(VF.character(characters[i]));
		}
		
		return VF.appl((IConstructor) production, listWriter.done());
	}
	
	private static ITree buildAppl(ArrayList<ITree> children, Object production){
		return VF.appl((IConstructor) production, children);
	}

	public ITree createSortNode(ArrayList<ITree> children, Object production){
		return buildAppl(children, production);
	}

	public ITree createSubListNode(ArrayList<ITree> children, Object production){
		return buildAppl(children, production);
	}

	public ITree createListNode(ArrayList<ITree> children, Object production){
		return buildAppl(children, production);
		
	}
	
	private static ITree buildAmbiguityNode(ArrayList<ITree> alternatives, boolean allowAmb){
		ISetWriter ambSublist = VF.setWriter();
		for(int i = alternatives.size() - 1; i >= 0; --i){
			ambSublist.insert(alternatives.get(i));
		}
		
		if (allowAmb) {
			return VF.amb(ambSublist.done());
		}
		else {
			throw new Ambiguous(VF.amb(ambSublist.done()));
		}
	}

	public ITree createAmbiguityNode(ArrayList<ITree> alternatives){
		return buildAmbiguityNode(alternatives, allowAmb);
	}

	public ITree createSubListAmbiguityNode(ArrayList<ITree> alternatives){
		return buildAmbiguityNode(alternatives, allowAmb);
	}

	public ITree createListAmbiguityNode(ArrayList<ITree> alternatives){
		return buildAmbiguityNode(alternatives, allowAmb);
	}
	
	private static ITree buildCycle(int depth, Object production){
		return VF.cycle(ProductionAdapter.getType((IConstructor) production), depth);
	}

	public ITree createCycleNode(int depth, Object production){
		return buildCycle(depth, production);
	}

	public ITree createSubListCycleNode(Object production){
		return buildCycle(1, production);
	}

	public ITree createRecoveryNode(int[] characters){
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
	
	public ITree addPositionInformation(ITree node, ISourceLocation location){
		return (ITree) node.asWithKeywordParameters().setParameter(RascalValueFactory.Location, location);
	}
	
	public ArrayList<ITree> getChildren(ITree node){
		IList args = TreeAdapter.getArgs(node);
		ArrayList<ITree> children = new ArrayList<>(args.length());
		for(int i = 0; i < args.length(); ++i){
			children.add((ITree) args.get(i));
		}
		return children;
	}
	
	public Object getRhs(Object production){
		return ProductionAdapter.getType((IConstructor) production);
	}
	
	public boolean isAmbiguityNode(ITree node){
		return TreeAdapter.isAmb(node);
	}
	
	public Object getProductionFromNode(ITree node){
		return TreeAdapter.getProduction(node);
	}
}
