package org.meta_environment.rascal.interpreter.strategy;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public class VisitableNode implements IVisitable,INode {

	private INode node;

	public VisitableNode(INode node) {
		this.node = node;
	}

	public int getChildrenNumber() {
		return node.arity();
	}

	public void setChildren(List<IVisitable> newchildren)
	throws IndexOutOfBoundsException {
		for (int j = 0; j < node.arity(); j++) {
			node = node.set(j,newchildren.get(j));
		}
	}

	public void update(IValue oldvalue, IValue newvalue) {}

	public IVisitable getChildAt(int i) throws IndexOutOfBoundsException {
		return VisitableFactory.makeVisitable(node.get(i));
	}

	public void setChildAt(int i, IVisitable newChild)
	throws IndexOutOfBoundsException {
		node = node.set(i, newChild.getValue());
	}

	public IValue getValue() {
		return node;
	}

	public IValue get(int i) throws IndexOutOfBoundsException {
		return node.get(i);
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return node.accept(v);
	}

	public boolean equals(Object other) {
		return node.equals(other);
	}

	public IValue getAnnotation(String label) throws FactTypeUseException {
		return node.getAnnotation(label);
	}

	public Map<String, IValue> getAnnotations() {
		return node.getAnnotations();
	}

	public Iterable<IValue> getChildren() {
		return node.getChildren();
	}

	public String getName() {
		return node.getName();
	}

	public Type getType() {
		return node.getType();
	}

	public boolean hasAnnotation(String label) throws FactTypeUseException {
		return node.hasAnnotation(label);
	}

	public boolean hasAnnotations() {
		return node.hasAnnotations();
	}

	public boolean isEqual(IValue other) {
		return node.isEqual(other);
	}

	public Iterator<IValue> iterator() {
		return node.iterator();
	}

	public INode joinAnnotations(Map<String, IValue> annotations) {
		return node.joinAnnotations(annotations);
	}

	public INode removeAnnotation(String key) {
		return node.removeAnnotation(key);
	}

	public INode removeAnnotations() {
		return node.removeAnnotations();
	}

	public INode set(int i, IValue newChild) throws IndexOutOfBoundsException {
		return node.set(i, newChild);
	}

	public INode setAnnotation(String label, IValue newValue)
	throws FactTypeUseException {
		return node.setAnnotation(label, newValue);
	}

	public INode setAnnotations(Map<String, IValue> annotations) {
		return node.setAnnotations(annotations);
	}

	public String toString() {
		return node.toString();
	}

	public int arity() {
		return node.arity();
	}
	
	public IVisitable setValue(IValue value) {
		this.node = (INode) value;
		return this;
	}


}
