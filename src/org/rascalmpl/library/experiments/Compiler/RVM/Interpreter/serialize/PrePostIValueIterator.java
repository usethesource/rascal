package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;


import java.io.IOException;
import java.util.Map;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class PrePostIValueIterator extends PrePostIterator<IValue,ValueIteratorKind>  {
    
    private IValueFactory vf;

    public PrePostIValueIterator(IValue root) throws IOException {
        this(root, 1024);
    }

    public PrePostIValueIterator(IValue root, int stackSize) throws IOException {
        super(IValue.class, ValueIteratorKind.class, stackSize);
        stack.push(root, ValueIteratorKind.getKind(root), true);
        beginning = false; // start out at fake end
        vf = ValueFactoryFactory.getValueFactory();
    }

    public ValueIteratorKind next() throws IOException {
        if (beginning) {
            if (kind.isCompound()) {
                stack.push(item, kind, false);
                switch(kind){
                
                case CONSTRUCTOR: {
                    IConstructor cons = (IConstructor) item;
                    
                    if(cons.mayHaveKeywordParameters()){
                        if(cons.asWithKeywordParameters().hasParameters()){
                            for (Map.Entry<String, IValue> param : cons.asWithKeywordParameters().getParameters().entrySet()) {
                                IString key = vf.string(param.getKey());
                                IValue val = param.getValue();
                                stack.push(val, ValueIteratorKind.getKind(val), true);
                                stack.push(key, ValueIteratorKind.getKind(key), true);
                            }
                        }
                    } else {
                        if(cons.asAnnotatable().hasAnnotations()){
                            for (Map.Entry<String, IValue> param : cons.asAnnotatable().getAnnotations().entrySet()) {
                                IString key = vf.string(param.getKey());
                                IValue val = param.getValue();
                                stack.push(val, ValueIteratorKind.getKind(val), true);
                                stack.push(key, ValueIteratorKind.getKind(key), true);
                            }
                        }
                    }
                    
                    for(int i = cons.arity() - 1; i >= 0; i--){
                        IValue elem = cons.get(i);
                        stack.push(elem, ValueIteratorKind.getKind(elem), true);
                    }
                    break;
                }
                
                case LIST: {
                    IList lst = (IList) item;
                    for (int i = lst.length() - 1; i >= 0; i--) {
                        IValue elem = lst.get(i);
                        stack.push(elem, ValueIteratorKind.getKind(elem), true);
                    }
                    break;
                }
                
                case MAP: {
                    IMap map = (IMap) item;
                    for(IValue key : map){
                        IValue val = map.get(key);
                        stack.push(val, ValueIteratorKind.getKind(val),  true);
                        stack.push(key, ValueIteratorKind.getKind(key),  true);
                    }
                    break;
                }
                    
                case NODE: {
                    INode node = (INode) item;
                    
                    if(node.mayHaveKeywordParameters()){
                        if(node.asWithKeywordParameters().hasParameters()){
                            for (Map.Entry<String, IValue> param : node.asWithKeywordParameters().getParameters().entrySet()) {
                                IString key = vf.string(param.getKey());
                                IValue val = param.getValue();
                                stack.push(val, ValueIteratorKind.getKind(val), true);
                                stack.push(key, ValueIteratorKind.getKind(key), true);
                            }
                        }
                    } else {
                        if(node.asAnnotatable().hasAnnotations()){
                            for (Map.Entry<String, IValue> param : node.asAnnotatable().getAnnotations().entrySet()) {
                                IString key = vf.string(param.getKey());
                                IValue val = param.getValue();
                                stack.push(val, ValueIteratorKind.getKind(val), true);
                                stack.push(key, ValueIteratorKind.getKind(key), true);
                            }
                        }
                    }
                    
                    for(int i = node.arity() - 1; i >= 0; i--){
                        IValue elem = node.get(i);
                        stack.push(elem, ValueIteratorKind.getKind(elem), true);
                    }
                    break;
                }
                    
                case RATIONAL: {
                    IRational rat = (IRational) item;
                    IInteger num = rat.numerator();
                    IInteger denom = rat.denominator();
                    
                    stack.push(denom, ValueIteratorKind.INT, true);
                    stack.push(num, ValueIteratorKind.INT, true);
                    break;
                }
                
                case SET: {
                    ISet set = (ISet) item;
                    for(IValue elem : set){
                        stack.push(elem, ValueIteratorKind.getKind(elem), true);
                    }
                    break;
                }
                
                case TUPLE: {
                    ITuple tuple = (ITuple) item;
                    for(int i = tuple.arity() - 1; i >= 0; i--){
                        IValue elem = tuple.get(i);
                        stack.push(elem, ValueIteratorKind.getKind(elem), true);
                    }
                    
                    break;
                }

                default:
                    throw new RuntimeException("Missing case");
                }
            }
        }
        item = stack.currentItem();
        kind = stack.currentKind();
        beginning = stack.currentBeginning();
        stack.pop();
        return kind;
    }

}

