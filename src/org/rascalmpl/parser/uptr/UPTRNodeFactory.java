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
import org.rascalmpl.values.uptr.RascalValueFactory.Tree;
import org.rascalmpl.values.uptr.TreeAdapter;

public class UPTRNodeFactory implements INodeConstructorFactory<Tree, ISourceLocation>{
	private final static RascalValueFactory VF = (RascalValueFactory) ValueFactoryFactory.getValueFactory();
	
	public UPTRNodeFactory(){
		super();
	}

	public Tree createCharNode(int charNumber){
		return VF.character(charNumber);
	}

	public Tree createLiteralNode(int[] characters, Object production){
		IListWriter listWriter = VF.listWriter();
		for(int i = characters.length - 1; i >= 0; --i){
			listWriter.insert(VF.character(characters[i]));
		}
		
		return VF.appl((IConstructor) production, listWriter.done());
	}
	
	private static Tree buildAppl(ArrayList<Tree> children, Object production){
		return VF.appl((IConstructor) production, children);
	}

	public Tree createSortNode(ArrayList<Tree> children, Object production){
		return buildAppl(children, production);
	}

	public Tree createSubListNode(ArrayList<Tree> children, Object production){
		return buildAppl(children, production);
	}

	public Tree createListNode(ArrayList<Tree> children, Object production){
		return buildAppl(children, production);
		
	}
	
	private static Tree buildAmbiguityNode(ArrayList<Tree> alternatives){
		ISetWriter ambSublist = VF.setWriter();
		for(int i = alternatives.size() - 1; i >= 0; --i){
			ambSublist.insert(alternatives.get(i));
		}
		
		return VF.amb(ambSublist.done());
	}

	public Tree createAmbiguityNode(ArrayList<Tree> alternatives){
		return buildAmbiguityNode(alternatives);
	}

	public Tree createSubListAmbiguityNode(ArrayList<Tree> alternatives){
		return buildAmbiguityNode(alternatives);
	}

	public Tree createListAmbiguityNode(ArrayList<Tree> alternatives){
		return buildAmbiguityNode(alternatives);
	}
	
	private static Tree buildCycle(int depth, Object production){
		return VF.cycle(ProductionAdapter.getType((IConstructor) production), depth);
	}

	public Tree createCycleNode(int depth, Object production){
		return buildCycle(depth, production);
	}

	public Tree createSubListCycleNode(Object production){
		return buildCycle(1, production);
	}

	public Tree createRecoveryNode(int[] characters){
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
	
	public Tree addPositionInformation(Tree node, ISourceLocation location){
		return (Tree) node.asAnnotatable().setAnnotation(RascalValueFactory.Location, location);
	}
	
	public ArrayList<Tree> getChildren(Tree node){
		IList args = TreeAdapter.getArgs(node);
		ArrayList<Tree> children = new ArrayList<>(args.length());
		for(int i = 0; i < args.length(); ++i){
			children.add((Tree) args.get(i));
		}
		return children;
	}
	
	public Object getRhs(Object production){
		return ProductionAdapter.getType((IConstructor) production);
	}
	
	public boolean isAmbiguityNode(Tree node){
		return TreeAdapter.isAmb(node);
	}
	
	public Object getProductionFromNode(Tree node){
		return TreeAdapter.getProduction(node);
	}
}
