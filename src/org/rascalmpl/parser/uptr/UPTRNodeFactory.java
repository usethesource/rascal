package org.rascalmpl.parser.uptr;

import java.net.URI;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;

import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.out.INodeConstructorFactory;
import org.rascalmpl.parser.gtd.util.ArrayList;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;

import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.ProductionAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;

public class UPTRNodeFactory implements INodeConstructorFactory<ITree, ISourceLocation>{
	private static final RascalValueFactory VF = (RascalValueFactory) ValueFactoryFactory.getValueFactory();
	private static final IConstructor SKIPPED = VF.constructor(RascalValueFactory.Production_Skipped, VF.constructor(RascalValueFactory.Symbol_IterStar, VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Range, VF.integer(1), VF.integer(Character.MAX_CODE_POINT))))));

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
			ITree result = VF.amb(ambSublist.done());

			// singleton amb clusters may have been reduced.
			if (result.isAmb()) {
				throw new Ambiguous(result);
			}
			else {
				return result;
			}
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

	// converting URIs to ISourceLocation is expensive (performance and memory wise)
	// so we keep a cache of the URI root
	private final Map<URI, ISourceLocation> uriLookup = new IdentityHashMap<>(4);
	
	public ISourceLocation createPositionInformation(URI input, int offset, int endOffset, PositionStore positionStore){
		int beginLine = positionStore.findLine(offset);
		int endLine = positionStore.findLine(endOffset);
		return VF.sourceLocation(uriLookup.computeIfAbsent(input, VF::sourceLocation), offset, endOffset - offset, beginLine + 1, endLine + 1, positionStore.getColumn(offset, beginLine), positionStore.getColumn(endOffset, endLine));
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

    @Override
    public ITree createSkippedNode(int[] characters) {
		return createLiteralNode(characters, SKIPPED);
	}

	public ITree createErrorNode(ArrayList<ITree> children, Object production) {
		IConstructor prod = (IConstructor) production;
		IConstructor errorProd = VF.constructor(RascalValueFactory.Production_Error, prod.get(0), prod, VF.integer(children.size()-1));
		return buildAppl(children, errorProd);
	}

}
