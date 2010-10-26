package org.rascalmpl.library;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
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
		
		if (TreeAdapter.isAppl(pt)) {
			if (SymbolAdapter.isStart(ProductionAdapter.getRhs(TreeAdapter.getProduction(pt)))) {
				pt = (IConstructor) TreeAdapter.getArgs(pt).get(1);
			}
		}
		
		return pt;
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
