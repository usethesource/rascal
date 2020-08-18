package org.rascalmpl.core.library;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.RascalExecutionContext;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.ProductionAdapter;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;
import org.rascalmpl.values.parsetrees.visitors.TreeVisitor;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

/*
 * This class overrides methods from Prelude that need to be handled differenty in compiled code.
 * In most (all?) cases this will be library function with a @reflect{...} tag that makes them dependent on
 * IEvaluatorContext, the context of the Rascal interpreter.
 */
public class PreludeCompiled extends Prelude {

	public PreludeCompiled(IValueFactory values) {
		super(values, new PrintWriter(System.out),  new PrintWriter(System.err));
	}
	
	public PreludeCompiled(IValueFactory values, PrintWriter out, PrintWriter err) {
		super(values, out,  err);
	}
	
//	public void print(IValue arg, RascalExecutionContext rex){
//		PrintWriter currentOutStream = rex.getStdOut();
//		
//		try{
//			if(arg.getType().isString()){
//			    ((IString) arg).write(currentOutStream);
//			}
//			else if(arg.getType().isSubtypeOf(RascalValueFactory.Tree)){
//				currentOutStream.print(TreeAdapter.yield((IConstructor) arg));
//			}
//			else if (arg.getType().isSubtypeOf(RascalValueFactory.Type)) {
//				currentOutStream.print(SymbolAdapter.toString((IConstructor) ((IConstructor) arg).get("symbol"), false));
//			}
//			else{
//				currentOutStream.print(arg.toString());
//			}
//		}
//        catch (IOException e) {
//            throw RuntimeExceptionFactory.io(e.getMessage());
//        }
//		finally {
//			currentOutStream.flush();
//		}
//	}
//    
//	public void iprint(IValue arg, IInteger lineLimit, RascalExecutionContext rex){
//		StandardTextWriter w = new StandardTextWriter(true, 2);
//		Writer output = rex.getStdOut();
//		if (lineLimit.signum() > 0) {
//		    output = new LimitedLineWriter(output, lineLimit.longValue());
//		}
//		
//		try {
//			w.write(arg, output);
//		} 
//		catch (IOLimitReachedException e) {
//		    // ignore since we wanted this
//		}
//		catch (IOException e) {
//			throw RuntimeExceptionFactory.io("Could not print indented value");
//		}
//		finally {
//		    if (output != rex.getStdOut()) {
//		        try {
//                    output.flush();
//                    output.close();
//                }
//                catch (IOException e) {
//                }
//		    }
//			rex.getStdOut().flush();
//		}
//	}
//	
//	public void iprintln(IValue arg, IInteger lineLimit, RascalExecutionContext rex){
//	    iprint(arg, lineLimit, rex);
//		rex.getStdOut().println();
//		rex.getStdOut().flush();
//	}
//	
//	public void println(RascalExecutionContext rex) {
//		rex.getStdOut().println();
//		rex.getStdOut().flush();
//	}
//	
//	public void println(IValue arg, RascalExecutionContext rex){
//		PrintWriter currentOutStream = rex.getStdOut();
//		
//		try{
//			if(arg.getType().isString()){
//			    ((IString) arg).write(currentOutStream);
//			}
//			else if(arg.getType().isSubtypeOf(RascalValueFactory.Tree)){
//				currentOutStream.print(TreeAdapter.yield((IConstructor) arg));
//			}
//			else if (arg.getType().isSubtypeOf(RascalValueFactory.Type)) {
//				currentOutStream.print(SymbolAdapter.toString((IConstructor) ((IConstructor) arg).get("symbol"), false));
//			}
//			else{
//				currentOutStream.print(arg.toString());
//			}
//			currentOutStream.println();
//		}
//		catch (IOException e) {
//            throw RuntimeExceptionFactory.io(e.getMessage());
//        }
//		finally {
//			currentOutStream.flush();
//		}
//	}
//	
//	public void rprintln(IValue arg, RascalExecutionContext rex){
//		PrintWriter currentOutStream = rex.getStdOut();
//		
//		try {
//			currentOutStream.print(arg.toString());
//			currentOutStream.println();
//		}
//		finally {
//			currentOutStream.flush();
//		}
//	}
//	
//	public void rprint(IValue arg, RascalExecutionContext rex){
//		PrintWriter currentOutStream = rex.getStdOut();
//		
//		try {
//			currentOutStream.print(arg.toString());
//		}
//		finally {
//			currentOutStream.flush();
//		}
//	}
	
//	// Begin of sorting functions
//
//	/**
//	 * A mini class to wrap a lessThan function
//	 */
//	private class Less {
//		private final FunctionInstance2<IValue,IValue,IValue> less;
//
//		Less(FunctionInstance2<IValue,IValue,IValue> less) {
//			this.less = less;
//		}
//
//		public boolean less(IValue x, IValue y) {
//			return ((IBool)less.call(x, y)).getValue();
//		}
//	}
//
//	private class Sorting {
//		private final IValue[] array;
//		private final int size;
//		private final Less less;
//
//		private void swap(int i, int j) {
//			IValue tmp = array[i];
//			array[i] = array[j];
//			array[j] = tmp;
//		}
//
//		public Sorting(IValue[] array, Less less) {
//			this.array = array;
//			this.size = array.length;
//			this.less = less;
//		}
//
//		/**
//		 * @throws IllegalArgument if comparator is illegal (i.e., if pivot equals pivot)
//		 */
//		public Sorting sort() {
//			if (size == 0) {
//				return this;
//			}
//			if(less.less(array[0], array[0])) {
//				throw RuntimeExceptionFactory.illegalArgument(); // "Bad comparator: Did you use less-or-equals instead of less-than?"
//			}
//			sort(0, size - 1);
//
//			return this;
//		}
//
//		public Sorting shuffle() {
//			for (int i = 0; i < size; i++) {
//				swap(i, i + (int) (Math.random() * (size-i)));
//			}
//			return this;
//		}
//
//		private void sort(int low, int high) {
//			IValue pivot = array[low + (high-low)/2];
//			int oldLow = low;
//			int oldHigh = high;
//
//			while (low < high) {
//				for ( ; less.less(array[low], pivot); low++); 
//				for ( ; less.less(pivot, array[high]); high--); 
//
//				if (low <= high) {
//					swap(low, high);
//					low++;
//					high--;
//				}
//			}
//
//			if (oldLow < high)
//				sort(oldLow, high);
//			if (low < oldHigh)
//				sort(low, oldHigh);
//		}
//	}
//
//	public IList sort(IList l, FunctionInstance2<IValue,IValue,IValue> cmpv){
//		IValue[] tmpArr = new IValue[l.length()];
//		for(int i = 0 ; i < l.length() ; i++){
//			tmpArr[i] = l.get(i);
//		}
//
//		// we randomly swap some elements to make worst case complexity unlikely
//		new Sorting(tmpArr, new Less(cmpv)).shuffle().sort();
//
//
//		IListWriter writer = values.listWriter();
//		writer.append(tmpArr);
//		return writer.done();
//	}
//
//	public IList sort(ISet l, FunctionInstance2<IValue,IValue,IValue> cmpv) {
//		IValue[] tmpArr = new IValue[l.size()];
//		int i = 0;
//
//		// we assume that the set is reasonably randomly ordered, such
//		// that the worst case of quicksort is unlikely
//		for (IValue elem : l){
//			tmpArr[i++] = elem;
//		}
//
//		new Sorting(tmpArr, new Less(cmpv)).sort();
//
//		IListWriter writer = values.listWriter();
//		for(IValue v : tmpArr){
//			writer.append(v);
//		}
//
//		return writer.done();
//	}
//	// end of sorting functions
	
//	// parsing functions
//	public IValue parse(IValue start, IString input, ISourceLocation origin, IBool allowAmbiguity, IBool hasSideEffects, RascalExecutionContext rex) {
//        return rex.getParsingTools().parse(super.values.string(rex.getFullModuleName()), start, input, origin, allowAmbiguity.getValue(), hasSideEffects.getValue(), null, rex);
//    }
//	public IValue parse(IValue start, ISourceLocation input, IBool allowAmbiguity, IBool hasSideEffects, RascalExecutionContext rex) {
//		return rex.getParsingTools().parse(super.values.string(rex.getFullModuleName()), start, input, allowAmbiguity.getValue(), hasSideEffects.getValue(), null, rex);
//	}
//
//	public IValue parse(IValue start, IString input, IBool allowAmbiguity, IBool hasSideEffects, RascalExecutionContext rex) {
//		return rex.getParsingTools().parse(super.values.string(rex.getFullModuleName()), start, input, allowAmbiguity.getValue(), hasSideEffects.getValue(), null, rex);
//	}
//	// end parsing functions
//	
//	public IValue firstAmbiguity(IValue start, IString input, RascalExecutionContext rex) {
//	    try {
//	        return rex.getParsingTools().parse(super.values.string(rex.getFullModuleName()), start, input, true, false, null, rex);
//	    }
//	    catch (ParseError pe) {
//	        ISourceLocation errorLoc = values.sourceLocation(values.sourceLocation(pe.getLocation()), pe.getOffset(), pe.getLength(), pe.getBeginLine() + 1, pe.getEndLine() + 1, pe.getBeginColumn(), pe.getEndColumn());
//	        throw RuntimeExceptionFactory.parseError(errorLoc);
//	    }
//	    catch (Ambiguous e) {
//	        return e.getTree();
//	    }
//	}
//	
//	public IValue firstAmbiguity(IValue start, ISourceLocation input, RascalExecutionContext rex) {
//	    try {
//	        return rex.getParsingTools().parse(super.values.string(rex.getFullModuleName()), start, input, true, false, null, rex);
//	    }
//	    catch (ParseError pe) {
//	        ISourceLocation errorLoc = values.sourceLocation(values.sourceLocation(pe.getLocation()), pe.getOffset(), pe.getLength(), pe.getBeginLine() + 1, pe.getEndLine() + 1, pe.getBeginColumn(), pe.getEndColumn());
//	        throw RuntimeExceptionFactory.parseError(errorLoc);
//	    }
//	    catch (Ambiguous e) {
//	        return e.getTree();
//	    }
//	}
	
	private TypeStore typeStore = new TypeStore();
	
	public IConstructor makeConstructor(Type returnType, String name, RascalExecutionContext rex, IValue ...args) {
		// TODO: in general, the following should be the call to an overloaded function
		IValue value = values.constructor(typeStore.lookupConstructor(returnType, name, TypeFactory.getInstance().tupleType(args)), args, new HashMap<String, IValue>());
		Type type = value.getType();
		if (type.isAbstractData()) {
			return (IConstructor)value;
		}
		throw RuntimeExceptionFactory.implodeError("Calling of constructor " + name + " did not return a constructor");
	}
	
	/*** begin of implode **/
	
	public IConstructor makeConstructor(TypeStore store, Type returnType, String name, IValue ...args) {
		// TODO: in general, the following should be the call to an overloaded function
		IValue value = values.constructor(store.lookupConstructor(returnType, name, TypeFactory.getInstance().tupleType(args)), args, new HashMap<String, IValue>());
		Type type = value.getType();
		if (type.isAbstractData()) {
			return (IConstructor)value;
		}
		throw RuntimeExceptionFactory.implodeError("Calling of constructor " + name + " did not return a constructor");
	}
	
	public IValue implode(IValue reifiedType, IConstructor arg0, RascalExecutionContext rex) {
		ITree tree = (ITree) arg0;
		
		typeStore = new TypeStore();
		Type type = tr.valueToType((IConstructor) reifiedType, typeStore);
		try {
			IValue result = implode(typeStore, type, tree, false, rex); 
			if (isUntypedNodeType(type) && !type.isTop() && (TreeAdapter.isList(tree) || TreeAdapter.isOpt(tree))) {
				// Ensure the result is actually a node, even though
				// the tree given to implode is a list.
				result = values.node("", result);
			}
			return result;
		}
		catch (Backtrack b) {
			throw b.exception;
		}
	}

	@SuppressWarnings("serial")
	protected static class Backtrack extends RuntimeException {
		Throw exception;
		public Backtrack(Throw exception) {
			this.exception = exception;
		}
		@Override
		public synchronized Throwable fillInStackTrace() {
			return this;
		}
	}
	
	private IValue[] implodeArgs(TypeStore store, Type type, IList args, RascalExecutionContext rex) {
		int length = args.length();
		IValue implodedArgs[] = new IValue[length];
		for (int i = 0; i < length; i++) {
			Type argType = isUntypedNodeType(type) ? type : type.getFieldType(i);
			implodedArgs[i] = implode(store, argType, (ITree)args.get(i), false, rex);
		}
		return implodedArgs;
	}
	
	
	protected IValue implode(TypeStore store, Type type, IConstructor arg0, boolean splicing, RascalExecutionContext rex) {
		ITree tree = (ITree) arg0;
		
		// always yield if expected type is str, except if regular 
		if (type.isString() && !splicing) {
			return values.string(TreeAdapter.yield(tree));
		}

		if (SymbolAdapter.isStartSort(TreeAdapter.getType(tree))) {
			IList args = TreeAdapter.getArgs(tree);
			ITree before = (ITree) args.get(0);
			ITree ast = (ITree) args.get(1);
			ITree after = (ITree) args.get(2);
			IValue result = implode(store, type, ast, splicing, rex);
			if (result.getType().isNode()) {
				IMapWriter comments = values.mapWriter();
				comments.putAll((IMap)((INode)result).asWithKeywordParameters().getParameter("comments"));
				IList beforeComments = extractComments(before);
				if (!beforeComments.isEmpty()) {
					comments.put(values.integer(-1), beforeComments);
				}
				IList afterComments = extractComments(after);
				if (!afterComments.isEmpty()) {
					comments.put(values.integer(((INode)result).arity()), afterComments);
				}
				//result = ((INode)result).asAnnotatable().setAnnotation("comments", comments.done());
			}
			return result;
		}
		
		if (TreeAdapter.isLexical(tree)) {
			java.lang.String constructorName = unescapedConsName(tree);
			java.lang.String yield = TreeAdapter.yield(tree);
			if (constructorName != null) {
				// make a single argument constructor  with yield as argument
				// if there is a singleton constructor with a str argument
				if (!type.isAbstractData() && !isUntypedNodeType(type)) {
					throw RuntimeExceptionFactory.illegalArgument(tree, "Constructor (" + constructorName + ") should match with abstract data type and not with " + type);
				}
				
				if (isUntypedNodeType(type)) {
					return values.node(constructorName, values.string(yield));
				}
				
				Set<Type> conses = findConstructors(type, constructorName, 1, store);
				Iterator<Type> iter = conses.iterator();
				while (iter.hasNext()) {
					try {
						@SuppressWarnings("unused")
						Type cons = iter.next();
						ISourceLocation loc = TreeAdapter.getLocation(tree);
						IConstructor ast = makeConstructor(store, type, constructorName, values.string(yield));
						return ast.asWithKeywordParameters().setParameter("location", loc);
					}
					catch (Backtrack b) {
						continue;
					}
				}
				throw new Backtrack(RuntimeExceptionFactory.illegalArgument(tree, "Cannot find a constructor " + type));
			}
			if (type.isInteger()) {
				return values.integer(yield);
			}
			if (type.isReal()) {
				return values.real(yield);
			}
			if (type.isBool()) {
				if (yield.equals("true")) {
					return values.bool(true);
				}
				if (yield.equals("false")) {
					return values.bool(false);
				}
				throw new Backtrack(RuntimeExceptionFactory.illegalArgument(tree, "Bool type does not match with " + yield));
			}
			if (type.isString() || isUntypedNodeType(type)) {
				// NB: in "node space" all lexicals become strings
				return values.string(yield);
			}
			
			throw RuntimeExceptionFactory.illegalArgument(tree, "Missing lexical constructor");
		}
		
		//Set implementation added here by Jurgen at 19/07/12 16:45
		if (TreeAdapter.isList(tree)) {
			if (type.isList() || splicing || isUntypedNodeType(type)) {
				// if in node space, we also make a list; 
				// NB: this breaks type safety if the top-level tree
				// is itself a list.
				
				Type elementType = type;
				if (!splicing && !isUntypedNodeType(type)) {
					elementType = type.getElementType();
				}
				IListWriter w = values.listWriter();
				for (IValue arg: TreeAdapter.getListASTArgs(tree)) {
					w.append(implode(store, elementType, (ITree) arg, false, rex));
				}
				return w.done();
			}
			else if (type.isSet()) {
				Type elementType = splicing ? type : type.getElementType();
				ISetWriter w = values.setWriter();
				for (IValue arg: TreeAdapter.getListASTArgs(tree)) {
					w.insert(implode(store, elementType, (ITree) arg, false, rex));
				}
				return w.done();
			}
			else {
				throw new Backtrack(RuntimeExceptionFactory.illegalArgument(tree, "Cannot match list with " + type));
			}
		}
		//Changes end here
		
		if (TreeAdapter.isOpt(tree) && type.isBool()) {
			IList args = TreeAdapter.getArgs(tree);
			if (args.isEmpty()) {
				return values.bool(false);
			}
			return values.bool(true);
		}
		
		if (TreeAdapter.isOpt(tree)) {
			if (!type.isList() && !isUntypedNodeType(type)) {
				throw new Backtrack(RuntimeExceptionFactory.illegalArgument(tree, "Optional should match with a list and not " + type));
			}
			Type elementType = isUntypedNodeType(type) ? type : type.getElementType();
			IListWriter w = values.listWriter();
			for (IValue arg: TreeAdapter.getASTArgs(tree)) {
				IValue implodedArg = implode(store, elementType, (ITree) arg, true, rex);
				if (implodedArg instanceof IList) {
					// splicing
					for (IValue nextArg: (IList)implodedArg) {
						w.append(nextArg);
					}
				}
				else {
					w.append(implodedArg);
				}
				// opts should have one argument (if any at all)
				break;
			}
			return w.done();
		}
		
		if (TreeAdapter.isAmb(tree)) {
			if (!type.isSet()) {
				throw new Backtrack(RuntimeExceptionFactory.illegalArgument(tree, "Ambiguous node should match with set and not " + type));
			}
			Type elementType = type.getElementType();
			ISetWriter w = values.setWriter();
			for (IValue arg: TreeAdapter.getAlternatives(tree)) {
				w.insert(implode(store, elementType, (ITree) arg, false, rex));
			}
			return w.done();
		}
		
		if (ProductionAdapter.hasAttribute(TreeAdapter.getProduction(tree), RascalValueFactory.Attribute_Bracket)) {
			return implode(store, type, (ITree) TreeAdapter.getASTArgs(tree).get(0), false, rex);
		}
		
		if (TreeAdapter.isAppl(tree)) {
			IList args = TreeAdapter.getASTArgs(tree);
			
			int j = 0;
			IMapWriter cw = values.mapWriter();
			IListWriter aw = values.listWriter();
			for (IValue kid : TreeAdapter.getArgs(tree)) {
				if (TreeAdapter.isLayout((ITree) kid)) {
					IList cts = extractComments((ITree) kid);
					if (!cts.isEmpty()) {
					  cw.put(values.integer(j), cts);
					}
					j++;
				}
				else if (!TreeAdapter.isLiteral((ITree) kid) && 
						!TreeAdapter.isCILiteral((ITree) kid) && 
						!TreeAdapter.isEmpty((ITree) kid)) {
					aw.append(kid);
				}
			}
			args = aw.done();
			int length = args.length();
			IMap comments = cw.done();
			
//			// this could be optimized.
//			i = 0;
//			int length = args.length();
//			while (i < length) {
//				if (TreeAdapter.isEmpty((IConstructor) args.get(i))) {
//					length--;
//					args = args.delete(i);
//				}
//				else {
//					i++;
//				}
//			}
			
			
			java.lang.String constructorName = unescapedConsName(tree);			
			
			if (constructorName == null) {
				if (length == 1) {
					// jump over injection
					return implode(store, type, (ITree) args.get(0), splicing, rex);
				}
				
				
				// make a tuple if we're in node space
				if (isUntypedNodeType(type)) {
					return values.tuple(implodeArgs(store, type, args, rex));
				}

				if (!type.isTuple()) {
					throw new Backtrack(RuntimeExceptionFactory.illegalArgument(tree, "Constructor does not match with " + type));
				}
				
				if (length != type.getArity()) {
					throw new Backtrack(RuntimeExceptionFactory.arityMismatch(type.getArity(), length));
				}

				return values.tuple(implodeArgs(store, type, args, rex));
			}
			
			// if in node space, make untyped nodes
			if (isUntypedNodeType(type)) {
				INode ast = values.node(constructorName, implodeArgs(store, type, args, rex));
				return ast.asWithKeywordParameters().setParameter("location", TreeAdapter.getLocation(tree)).asWithKeywordParameters().setParameter("comments", comments);
			}
			
			// make a typed constructor
			if (!type.isAbstractData()) {
				throw new Backtrack(RuntimeExceptionFactory.illegalArgument(tree, "Constructor (" + constructorName + ") should match with abstract data type and not with " + type));
			}

			Set<Type> conses = findConstructors(type, constructorName, length, store);
			Iterator<Type> iter = conses.iterator();
			while (iter.hasNext()) {
				try {
					Type cons = iter.next();
					ISourceLocation loc = TreeAdapter.getLocation(tree);
					IValue[] implodedArgs = implodeArgs(store, cons, args, rex);
					IConstructor ast = makeConstructor(store, type, constructorName, implodedArgs);
					return ast.asWithKeywordParameters().setParameter("location", loc).asWithKeywordParameters().setParameter("comments", comments);
				}
				catch (Backtrack b) {
					continue;
				}
			}
			
		}
		
		throw new Backtrack(RuntimeExceptionFactory.illegalArgument(tree,
				"Cannot find a constructor for " + type));
	}
	
	private IList extractComments(IConstructor layout) {
		final IListWriter comments = values.listWriter();
		TreeVisitor<RuntimeException> visitor = new TreeVisitor<RuntimeException>() {

			@Override
			public ITree visitTreeAppl(ITree arg)
					 {
				if (TreeAdapter.isComment(arg)) {
					comments.append(values.string(TreeAdapter.yield(arg)));
				}
				else {
					for (IValue t: TreeAdapter.getArgs(arg)) {
						t.accept(this);
					}
				}
				return arg;
			}

			@Override
			public ITree visitTreeAmb(ITree arg)
					 {
				return arg;
			}

			@Override
			public ITree visitTreeChar(ITree arg)
					 {
				return arg;
			}

			@Override
			public ITree visitTreeCycle(ITree arg)
					 {
				return arg;
			}
			
		};
		
		layout.accept(visitor);
		return comments.done();
	}

	protected boolean isUntypedNodeType(Type type) {
		return (type.isNode() && !type.isConstructor() && !type.isAbstractData()) 
				|| type.isTop();
	}
	
	
	/*** end of implode ***/
}
