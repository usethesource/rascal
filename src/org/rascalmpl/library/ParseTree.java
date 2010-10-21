package org.rascalmpl.library;

import java.io.IOException;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.uptr.ParsetreeAdapter;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

public class ParseTree {
	private final IValueFactory values;
	
	public ParseTree(IValueFactory values){
		super();
		this.values = values;
	}

	public IValue parse(IConstructor start, ISourceLocation input, IEvaluatorContext ctx) {
		Type reified = start.getType();
		IConstructor startSort = checkPreconditions(start, reified);
		
		IConstructor pt = ctx.getEvaluator().parseObject(startSort, input.getURI());
		pt = ParsetreeAdapter.getTop(pt);
		if (TreeAdapter.isAppl(pt)) {
			if (SymbolAdapter.isStart(ProductionAdapter.getRhs(TreeAdapter.getProduction(pt)))) {
				pt = (IConstructor) TreeAdapter.getArgs(pt).get(1);
			}
		}
		return pt;
	}
	
	public IValue parse(IConstructor start, IString input, IEvaluatorContext ctx) {
		Type reified = start.getType();
		IConstructor startSort = checkPreconditions(start, reified);
		
		IConstructor pt = ctx.getEvaluator().parseObject(startSort, input.getValue());
		return ((IList) ParsetreeAdapter.getTop(pt).get("args")).get(1);
	}
	
	public IValue parseExperimental(IConstructor start, ISourceLocation input, IEvaluatorContext ctx) throws IOException{
		Type reified = start.getType();
		IConstructor startSort = checkPreconditions(start, reified);
		
		URI inputURI = input.getURI();
		IConstructor pt = (IConstructor) ctx.getEvaluator().parseObjectExperimental(startSort, inputURI);
		return ParsetreeAdapter.getTop(pt);
	}
	
	public IValue parseExperimental(IConstructor start, IString input, IEvaluatorContext ctx) {
		Type reified = start.getType();
		IConstructor startSort = checkPreconditions(start, reified);
		
		IConstructor pt = (IConstructor) ctx.getEvaluator().parseObjectExperimental(startSort, URI.create("file://-"), input.getValue());
		return ParsetreeAdapter.getTop(pt);
	}
	
	public IString unparse(IConstructor tree) {
		return values.string(TreeAdapter.yield(tree));
	}
	
	private static IConstructor checkPreconditions(IConstructor start, Type reified) {
		if (!(reified instanceof ReifiedType)) {
		   throw RuntimeExceptionFactory.illegalArgument(start, null, null);
		}
		
		Type nt = reified.getTypeParameters().getFieldType(0);
		
		if (!(nt instanceof NonTerminalType)) {
			throw RuntimeExceptionFactory.illegalArgument(start, null, null);
		}
		
		IConstructor symbol = ((NonTerminalType) nt).getSymbol();
		
		return symbol;
	}
}
