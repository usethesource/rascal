package org.rascalmpl.parser;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.iguana.traversal.NodeListener;
import org.iguana.traversal.PositionInfo;
import org.iguana.traversal.Result;
import org.rascalmpl.values.uptr.IRascalValueFactory;

public class ParsetreeBuilder implements NodeListener<SerializableValue, IConstructor> {
  
  private final IRascalValueFactory vf;

  public ParsetreeBuilder() {
    this.vf = IRascalValueFactory.getInstance();
  }
  
  @Override
  public void startNode(SerializableValue type) {
    // do nothing
  }

  @Override
  public Result<IConstructor> endNode(SerializableValue type, Iterable<IConstructor> children, PositionInfo node) {
	
	if(type == null) {
		throw new IllegalArgumentException("type cannot be null.");
	}
	  
	if(children == null) {
		throw new IllegalArgumentException("children cannot be null.");
	}
	
    IListWriter args = vf.listWriter();
    args.appendAll(children);
    ISourceLocation sourceLocation = vf.sourceLocation(vf.sourceLocation(node.getURI()), 
    		 										   node.getOffset(), 
    		 										   node.getLength(), 
    		 										   node.getLineNumber(), 
    		 										   node.getEndLineNumber(), 
    		 										   node.getColumn() - 1, 
    		 										   node.getEndColumnNumber() - 1);
	IConstructor tree = vf.appl((IConstructor) type.get(), args.done()).asAnnotatable().setAnnotation("loc", sourceLocation);
	return (Result<IConstructor>) Result.accept(tree);
  }

  @Override
  public Result<IConstructor> buildAmbiguityNode(Iterable<IConstructor> children, PositionInfo node) {
    ISetWriter set = vf.setWriter();
    set.insertAll(children);
    return Result.accept(vf.amb(set.done()));
  }

  @Override
  public Result<IConstructor> terminal(int c, PositionInfo node) {
    return Result.accept(vf.character(c));
  }
  
}
