package org.meta_environment.rascal.interpreter.strategy;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;


public class VisitableConstructor implements IVisitable, IConstructor {
	
		private IConstructor constructor;

		public boolean declaresAnnotation(TypeStore store, String label) {
			return constructor.declaresAnnotation(store, label);
		}

		public IValue get(String label) {
			return constructor.get(label);
		}

		public Type getChildrenTypes() {
			return constructor.getChildrenTypes();
		}

		public Type getConstructorType() {
			return constructor.getConstructorType();
		}

		public boolean has(String label) {
			return constructor.has(label);
		}

		public IConstructor set(String label, IValue newChild)
				throws FactTypeUseException {
			return constructor.set(label, newChild);
		}

		public VisitableConstructor(IConstructor node) {
			this.constructor = node;
		}

		public int arity() {
			return constructor.arity();
		}

		public IValue get(int i) throws IndexOutOfBoundsException {
			return constructor.get(i);
		}

		public IVisitable getChildAt(int i) throws IndexOutOfBoundsException {
			return VisitableFactory.makeVisitable(constructor.get(i));
		}

		public IVisitable setChildAt(int i, IVisitable newChild)
				throws IndexOutOfBoundsException {
			return new VisitableNode(constructor.set(i, newChild.getValue()));
		}

		public IValue getValue() {
			return constructor;
		}

		public <T> T accept(IValueVisitor<T> v) throws VisitorException {
			return constructor.accept(v);
		}

		public boolean equals(Object other) {
			return constructor.equals(other);
		}

		public IValue getAnnotation(String label) throws FactTypeUseException {
			return constructor.getAnnotation(label);
		}

		public Map<String, IValue> getAnnotations() {
			return constructor.getAnnotations();
		}

		public Iterable<IValue> getChildren() {
			return constructor.getChildren();
		}

		public String getName() {
			return constructor.getName();
		}

		public Type getType() {
			return constructor.getType();
		}

		public boolean hasAnnotation(String label) throws FactTypeUseException {
			return constructor.hasAnnotation(label);
		}

		public boolean hasAnnotations() {
			return constructor.hasAnnotations();
		}

		public boolean isEqual(IValue other) {
			return constructor.isEqual(other);
		}

		public Iterator<IValue> iterator() {
			return constructor.iterator();
		}

		public IConstructor joinAnnotations(Map<String, IValue> annotations) {
			return constructor.joinAnnotations(annotations);
		}

		public IConstructor removeAnnotation(String key) {
			return constructor.removeAnnotation(key);
		}

		public IConstructor removeAnnotations() {
			return constructor.removeAnnotations();
		}

		public IConstructor set(int i, IValue newChild) throws IndexOutOfBoundsException {
			return constructor.set(i, newChild);
		}

		public IConstructor setAnnotation(String label, IValue newValue)
				throws FactTypeUseException {
			return constructor.setAnnotation(label, newValue);
		}

		public IConstructor setAnnotations(Map<String, IValue> annotations) {
			return constructor.setAnnotations(annotations);
		}

		public String toString() {
			return constructor.toString();
		}

		public IVisitable setChildren(List<IVisitable> newchildren)
		throws IndexOutOfBoundsException {
			for (int j = 0; j < constructor.arity(); j++) {
				constructor = constructor.set(j,newchildren.get(j));
			}
			return this;
		}

	}