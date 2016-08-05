package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;


import java.io.IOException;
import java.util.Map;

import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class LazyPrePostIValueIterator  {
    private PositionStack stack;
    private Kind kind;
    private IValue value;
    private boolean beginning;
	private final IValueFactory vf;
    
    public LazyPrePostIValueIterator(IValue root) throws IOException {
        this(root, 1024);
    }

    public LazyPrePostIValueIterator(IValue root, int stackSize) throws IOException {
        stack = new PositionStack(stackSize);
        stack.push(root, Kind.getKind(root), true);
        beginning = false; // start out at fake end
        vf = ValueFactoryFactory.getValueFactory();
    }

    public boolean hasNext() {
        return !stack.isEmpty() || (beginning && kind != null && Kind.isCompound(kind));
    }

    public Kind next() throws IOException {
        if (beginning) {
            if (Kind.isCompound(kind)) {
                stack.push(value, kind, false);
                switch(kind){
				case ADT:
					break;
					
				case ALIAS:
					break;
				
				case CONSTRUCTOR:
					IConstructor cons = (IConstructor) value;
					
					if(cons.mayHaveKeywordParameters()){
						if(cons.asWithKeywordParameters().hasParameters()){
							for (Map.Entry<String, IValue> param : cons.asWithKeywordParameters().getParameters().entrySet()) {
								IString key = vf.string(param.getKey());
								IValue val = param.getValue();
								stack.push(val, Kind.getKind(val), true);
								stack.push(key, Kind.getKind(key), true);
							}
						}
					} else {
						if(cons.asAnnotatable().hasAnnotations()){
							for (Map.Entry<String, IValue> param : cons.asAnnotatable().getAnnotations().entrySet()) {
								IString key = vf.string(param.getKey());
								IValue val = param.getValue();
								stack.push(val, Kind.getKind(val), true);
								stack.push(key, Kind.getKind(key), true);
							}
						}
					}
					
					for(int i = cons.arity() - 1; i >= 0; i--){
						IValue elem = cons.get(i);
						stack.push(elem, Kind.getKind(elem), true);
					}
					break;
				
				case FUNCTION:
					break;
				
				case LIST:
					IList lst = (IList) value;
					for (int i = lst.length() - 1; i >= 0; i--) {
	                    IValue elem = lst.get(i);
	                    stack.push(elem, Kind.getKind(elem), true);
	                }
					break;
				
				case MAP:
					IMap map = (IMap) value;
					for(IValue key : map){
						IValue val = map.get(key);
						stack.push(val, Kind.getKind(val),  true);
						stack.push(key, Kind.getKind(key),  true);
					}
					break;
					
				case NODE:
					INode node = (INode) value;
					
					if(node.mayHaveKeywordParameters()){
						if(node.asWithKeywordParameters().hasParameters()){
							for (Map.Entry<String, IValue> param : node.asWithKeywordParameters().getParameters().entrySet()) {
								IString key = vf.string(param.getKey());
								IValue val = param.getValue();
								stack.push(val, Kind.getKind(val), true);
								stack.push(key, Kind.getKind(key), true);
							}
						}
					} else {
						if(node.asAnnotatable().hasAnnotations()){
							for (Map.Entry<String, IValue> param : node.asAnnotatable().getAnnotations().entrySet()) {
								IString key = vf.string(param.getKey());
								IValue val = param.getValue();
								stack.push(val, Kind.getKind(val), true);
								stack.push(key, Kind.getKind(key), true);
							}
						}
					}
					
					for(int i = node.arity() - 1; i >= 0; i--){
						IValue elem = node.get(i);
						stack.push(elem, Kind.getKind(elem), true);
					}
					break;
				case NONTERMINAL:
					break;
				
				case OVERLOADED:
					break;
				
				case REIFIED:
					break;
					
				case SET:
					ISet set = (ISet) value;
					for(IValue elem : set){
						stack.push(elem, Kind.getKind(elem), true);
					}
					break;
				
				case TUPLE:
					ITuple tuple = (ITuple) value;
					for(int i = tuple.arity() - 1; i >= 0; i--){
						IValue elem = tuple.get(i);
						stack.push(elem, Kind.getKind(elem), true);
					}
					
					break;
				
				default:
					throw new RuntimeException("Missing case");
                }
            }
        }
        value = stack.currentIValue();
        kind = stack.currentKind();
        beginning = stack.currentBeginning();
        stack.pop();
        return kind;
    }

    public Kind skipIValue() {
        assert beginning;
        beginning = false;
        return kind;
    }

    public boolean atBeginning() {
        return beginning;
    }

  
    public Kind currentKind() {
        return kind;
    }

    public IValue getIValue() {
        return value;
    }

}

