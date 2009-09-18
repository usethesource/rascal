package org.meta_environment.rascal.interpreter.strategy;

import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public class VisitableMap implements IVisitable, IMap {

	private IMap map;

	public VisitableMap(IMap map) {
		this.map = map;
	}

	public int arity() {
		return map.size();
	}

	public IVisitable getChildAt(int i) throws IndexOutOfBoundsException {
		int index = 0;
		Iterator<Entry<IValue, IValue>> entries = map.entryIterator();
		while(entries.hasNext()) {
			Entry<IValue, IValue> e = entries.next();
			if (index==i) {
				ITuple tuple = ValueFactory.getInstance().tuple(e.getKey(),e.getValue());
				return new VisitableTuple(tuple);
			}
			index ++;
		}
		throw new IndexOutOfBoundsException();
	}

	public IValue getValue() {
		return map;
	}

	public IVisitable setChildAt(int i, IVisitable newChild)
	throws IndexOutOfBoundsException {
		if (i>=arity()) {
			throw new IndexOutOfBoundsException();
		}
		int index = 0;
		IMapWriter writer = ValueFactory.getInstance().mapWriter(map.getKeyType(), map.getValueType());
		Iterator<Entry<IValue, IValue>> entries = map.entryIterator();
		while(entries.hasNext()) {
			Entry<IValue, IValue> e = entries.next();
			IValue key = e.getKey();
			IValue value = e.getValue();
			if (index==i) {
				// safe cast
				// because strategies are type preserving
				ITuple newtuple = (ITuple) (newChild.getValue());
				writer.put(newtuple.get(0), newtuple.get(1));
			} else {
				writer.put(key, value);
			}
			index ++;
		}
		return new VisitableMap(writer.done());
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return map.accept(v);
	}

	public IMap common(IMap other) {
		return map.common(other);
	}

	public IMap compose(IMap other) {
		return map.compose(other);
	}

	public boolean containsKey(IValue key) {
		return map.containsKey(key);
	}

	public boolean containsValue(IValue value) {
		return map.containsValue(value);
	}

	public Iterator<Entry<IValue, IValue>> entryIterator() {
		return map.entryIterator();
	}

	public boolean equals(Object other) {
		return map.equals(other);
	}

	public IValue get(IValue key) {
		return map.get(key);
	}

	public Type getKeyType() {
		return map.getKeyType();
	}

	public Type getType() {
		return map.getType();
	}

	public Type getValueType() {
		return map.getValueType();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean isEqual(IValue other) {
		return map.isEqual(other);
	}

	public boolean isSubMap(IMap other) {
		return map.isSubMap(other);
	}

	public Iterator<IValue> iterator() {
		return map.iterator();
	}

	public IMap join(IMap other) {
		return map.join(other);
	}

	public IMap put(IValue key, IValue value) {
		return map.put(key, value);
	}

	public IMap remove(IMap other) {
		return map.remove(other);
	}

	public int size() {
		return map.size();
	}

	public String toString() {
		return map.toString();
	}

	public Iterator<IValue> valueIterator() {
		return map.valueIterator();
	}



}
