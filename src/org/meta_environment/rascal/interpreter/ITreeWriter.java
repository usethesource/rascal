package org.meta_environment.rascal.interpreter;

import java.util.Map;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IWriter;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;

public class ITreeWriter implements IWriter {
	
	Stack<IValue> spine = new Stack<IValue>();
	
	ValueFactory vf = ValueFactory.getInstance();
	
	@Override
	public IValue done() {
		return spine.pop();
	}

	@Override
	public void insert(IValue... value) throws FactTypeError {
		for(int i = 0; i < value.length; i++){
			System.err.println("add " + value[i]);
			org.eclipse.imp.pdb.facts.type.Type type = value[i].getType();
			if(type.isTreeType()){
				ITree t = (ITree) value[i];
				String name = t.getName();
				int arity = t.arity();
				IValue args[] = new IValue[arity];
				for(int j = arity -1 ; j >= 0; j--){
					args[j] = spine.pop();
				}
				ITree newTree = ValueFactory.getInstance().tree(name, args);
				spine.push(newTree);
				// TODO: need implementations for list/set/... here as well.
			} else if(type.isListType()){
				IList l = (IList) value[i];
				IListWriter w = l.getType().writer(vf);
			
				for(int j = l.length() - 1 ; j >= 0; j--){
					w.insert(spine.pop());
				}
				spine.push(w.done());
			} else if(type.isSetType()){
				ISet s = (ISet) value[i];
				ISetWriter w = s.getType().writer(vf);
			
				for(int j = s.size() - 1 ; j >= 0; j--){
					w.insert(spine.pop());
				}
				spine.push(w.done());

			} else if(type.isMapType()){
				IMap m = (IMap) value[i];
				IMapWriter w = m.getType().writer(vf);
			
				for(int j = m.size() - 1 ; j >= 0; j--){
					w.insert(spine.pop());
					w.insert(spine.pop());
				}
				spine.push(w.done());
				
			} else if(type.isTupleType()){
				ITuple tp = (ITuple) value[i];
				int arity = tp.arity();
				IValue args[] = new IValue[arity];
			
				for(int j = arity - 1 ; j >= 0; j--){
					args[j] = spine.pop();
				}
				spine.push(vf.tuple(args));

			} else {
				spine.push(value[i]);
			}
		}
	}

	@Override
	public void insertAll(Iterable<IValue> collection) throws FactTypeError {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAnnotation(String label, IValue value) throws FactTypeError {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAnnotations(Map<String, IValue> annotations)
			throws FactTypeError {
		// TODO Auto-generated method stub

	}
	
	private void replace(int arity, IValue newValue){
		for(int i = 0; i <arity; i++){
			spine.pop();
		}
		spine.push(newValue);
	}
	
	public void replace(IValue oldValue, IValue newValue){
		org.eclipse.imp.pdb.facts.type.Type oldType = oldValue.getType();
		org.eclipse.imp.pdb.facts.type.Type newType = newValue.getType();
		
		System.err.println("replace " + oldValue + " by " + newValue);
		if(!newType.isSubtypeOf(oldType)){
			throw new RascalTypeError("replacing " + oldType + " by " + newType + " value");
		}
		if(newType.isTreeType()){
			replace(((ITree) oldValue).arity(), newValue);
		} else if(newType.isListType()){
			replace(((IList) oldValue).length(), newValue);
		} else if(newType.isSetType()){
			replace(((ISet) oldValue).size(), newValue);
		} else if(newType.isTupleType()){
			replace(((ITuple) oldValue).arity(), newValue);
		} else if(newType.isMapType()){
			replace(((IMap) oldValue).size(), newValue);
		} else {
			spine.pop();
			spine.push(newValue);
		}
	}

}
