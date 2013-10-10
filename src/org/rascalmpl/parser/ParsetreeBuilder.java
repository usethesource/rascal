package org.rascalmpl.parser;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.jgll.traversal.NodeListener;
import org.jgll.traversal.PositionInfo;
import org.jgll.traversal.Result;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

public class ParsetreeBuilder implements NodeListener<IConstructor,IConstructor> {
  private final IValueFactory vf;

  public ParsetreeBuilder() {
    this.vf = ValueFactoryFactory.getValueFactory();
  }
  
  @Override
  public void startNode(IConstructor type) {
    // do nothing
  }

  @SuppressWarnings("unchecked")
  @Override
  public Result<IConstructor> endNode(IConstructor type, Iterable<IConstructor> children, PositionInfo node) {
	
	if(type == null) {
		throw new IllegalArgumentException("type cannot be null.");
	}
	  
	if(children == null) {
		throw new IllegalArgumentException("children cannot be null.");
	}
	
    IListWriter args = vf.listWriter();
    args.appendAll(children);
    return (Result<IConstructor>) Result.accept(vf.constructor(Factory.Tree_Appl, type, args.done()).asAnnotatable().setAnnotation("loc", vf.sourceLocation(URI.create("dunno:///"), 0, 1, 1, 1, 1, 1)));
  }

  @Override
  public Result<IConstructor> buildAmbiguityNode(Iterable<IConstructor> children, PositionInfo node) {
    ISetWriter set = vf.setWriter();
    set.insertAll(children);
    return Result.accept(vf.constructor(Factory.Tree_Amb, set.done()));
  }

  @Override
  public Result<IConstructor> terminal(int c, PositionInfo node) {
    return Result.accept(vf.constructor(Factory.Tree_Char, vf.integer(c)));
  }
  
}
