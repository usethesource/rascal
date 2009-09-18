package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.rascal.interpreter.strategy.IVisitable;

public class TopologicalVisitableNode extends TopologicalVisitable<INode> implements
INode {

	public TopologicalVisitableNode(IRelation root, INode value,
			List<IVisitable> children) {
		super(root, value, children);
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return value.accept(v);
	}

	public int arity() {
		return value.arity();
	}

	public boolean equals(Object other) {
		return value.equals(other);
	}

	public IValue get(int i) throws IndexOutOfBoundsException {
		return value.get(i);
	}

	public IValue getAnnotation(String label) throws FactTypeUseException {
		return value.getAnnotation(label);
	}

	public Map<String, IValue> getAnnotations() {
		return value.getAnnotations();
	}

	public Iterable<IValue> getChildren() {
		return value.getChildren();
	}

	public String getName() {
		return value.getName();
	}

	public Type getType() {
		return value.getType();
	}

	public boolean hasAnnotation(String label) throws FactTypeUseException {
		return value.hasAnnotation(label);
	}

	public boolean hasAnnotations() {
		return value.hasAnnotations();
	}

	public boolean isEqual(IValue other) {
		return value.isEqual(other);
	}

	public Iterator<IValue> iterator() {
		return value.iterator();
	}

	public INode joinAnnotations(Map<String, IValue> annotations) {
		return value.joinAnnotations(annotations);
	}

	public INode removeAnnotation(String key) {
		return value.removeAnnotation(key);
	}

	public INode removeAnnotations() {
		return value.removeAnnotations();
	}

	public INode set(int i, IValue newChild) throws IndexOutOfBoundsException {
		return value.set(i, newChild);
	}

	public INode setAnnotation(String label, IValue newValue)
	throws FactTypeUseException {
		return value.setAnnotation(label, newValue);
	}

	public INode setAnnotations(Map<String, IValue> annotations) {
		return value.setAnnotations(annotations);
	}

	public String toString() {
		return value.toString();
	}


}
