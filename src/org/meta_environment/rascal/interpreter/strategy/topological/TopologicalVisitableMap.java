package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public class TopologicalVisitableMap extends TopologicalVisitable<IMap> implements
		IMap {
	
	public TopologicalVisitableMap(RelationContext context, IMap value,
			List<TopologicalVisitable<?>> children) {
		super(context, value, children);
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return value.accept(v);
	}

	public IMap common(IMap other) {
		return value.common(other);
	}

	public IMap compose(IMap other) {
		return value.compose(other);
	}

	public boolean containsKey(IValue key) {
		return value.containsKey(key);
	}

	public boolean containsValue(IValue value) {
		return this.value.containsValue(value);
	}

	public Iterator<Entry<IValue, IValue>> entryIterator() {
		return value.entryIterator();
	}

	public IValue get(IValue key) {
		return value.get(key);
	}

	public Type getKeyType() {
		return value.getKeyType();
	}

	public Type getType() {
		return value.getType();
	}

	public Type getValueType() {
		return value.getValueType();
	}

	public boolean isEmpty() {
		return value.isEmpty();
	}

	public boolean isEqual(IValue other) {
		return value.isEqual(other);
	}

	public boolean isSubMap(IMap other) {
		return value.isSubMap(other);
	}

	public Iterator<IValue> iterator() {
		return value.iterator();
	}

	public IMap join(IMap other) {
		return value.join(other);
	}

	public IMap put(IValue key, IValue value) {
		return this.value.put(key, value);
	}

	public IMap remove(IMap other) {
		return value.remove(other);
	}

	public int size() {
		return value.size();
	}

	public String toString() {
		return value.toString();
	}

	public Iterator<IValue> valueIterator() {
		return value.valueIterator();
	}


}
