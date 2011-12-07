package org.rascalmpl.library.experiments.resource.results.buffers;

import java.util.Iterator;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public class LazyRelation implements IRelation {
	private final int bufferSize;
	private ILazyFiller filler;
	private Type elementType;
	
	public LazyRelation(int bufferSize, ILazyFiller filler, Type elementType) {
		this.bufferSize = bufferSize;
		this.filler = filler;
		this.elementType = elementType;
	}

	@Override
	public Type getElementType() {
		return elementType;
	}

	@Override
	public boolean isEmpty() {
		throw new IllegalOperationException("isEmpty over buffered relation", getType());
	}

	@Override
	public int size() {
		throw new IllegalOperationException("size over buffered relation", getType());
	}

	@Override
	public boolean contains(IValue element) {
		throw new IllegalOperationException("contains over buffered relation", getType());
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel insert(IValue element) {
		throw new IllegalOperationException("insert over buffered relation", getType());
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel union(ISet set) {
		throw new IllegalOperationException("union over buffered relation", getType());
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel intersect(ISet set) {
		throw new IllegalOperationException("intersect over buffered relation", getType());
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel subtract(ISet set) {
		throw new IllegalOperationException("subtract over buffered relation", getType());
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel delete(IValue elem) {
		throw new IllegalOperationException("delete over buffered relation", getType());
	}

	@Override
	public IRelation product(ISet set) {
		throw new IllegalOperationException("product over buffered relation", getType());
	}

	@Override
	public boolean isSubsetOf(ISet other) {
		throw new IllegalOperationException("subset over buffered relation", getType());
	}

	@Override
	public Iterator<IValue> iterator() {
		LazyIterator bi = new LazyIterator(filler.getBufferedFiller(), bufferSize);
		bi.init();
		return bi;
	}

	@Override
	public Type getType() {
		return TypeFactory.getInstance().relTypeFromTuple(this.elementType);
	}

	@Override
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEqual(IValue other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int arity() {
		return this.elementType.getArity();
	}

	@Override
	public IRelation compose(IRelation rel) throws FactTypeUseException {
		throw new IllegalOperationException("compose over buffered relation", getType());
	}

	@Override
	public IRelation closure() throws FactTypeUseException {
		throw new IllegalOperationException("transitive closure over buffered relation", getType());
	}

	@Override
	public IRelation closureStar() throws FactTypeUseException {
		throw new IllegalOperationException("reflexive transitive closure over buffered relation", getType());
	}

	@Override
	public ISet carrier() {
		throw new IllegalOperationException("carrier over buffered relation", getType());
	}

	@Override
	public Type getFieldTypes() {
		return this.elementType;
	}

	@Override
	public ISet domain() {
		throw new IllegalOperationException("domain over buffered relation", getType());
	}

	@Override
	public ISet range() {
		throw new IllegalOperationException("range over buffered relation", getType());
	}

	@Override
	public ISet select(int... fields) {
		throw new IllegalOperationException("select over buffered relation", getType());
	}

	@Override
	public ISet select(String... fields) throws FactTypeUseException {
		throw new IllegalOperationException("select over buffered relation", getType());
	}

	@Override
	public String toString() {
		return "Buffered Relation";
	}
}
