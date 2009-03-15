package org.meta_environment.rascal.interpreter;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;


public class SubList implements IList {
	private final Type fType;
	
	private final int start;
	private final int len;
	private final int end;
	private final IList base;

	public SubList(IValue V, int start, int len){
		super();
		
		fType = V.getType();
		
		if(start < 0 ) {
			throw RuntimeExceptionFactory.indexOutOfBounds(ValueFactoryFactory.getValueFactory().integer(start));
		}

		if(len < 0) {
			throw RuntimeExceptionFactory.indexOutOfBounds(ValueFactoryFactory.getValueFactory().integer(len));
		}
		
		if(V instanceof IList){
			this.base = (IList) V;
			this.start = start;
		} else if (V instanceof SubList){
			SubList other =  (SubList) V;
			this.base = other.base;
			this.start = other.start + start;
		} else {
			throw new ImplementationError("Illegal value in SubList");
		}
		this.len = len;
		this.end = start + len;
		if(this.start < 0){
			throw RuntimeExceptionFactory.indexOutOfBounds(ValueFactoryFactory.getValueFactory().integer(this.start));
		}
		if(this.end > base.length()){
			throw RuntimeExceptionFactory.indexOutOfBounds(ValueFactoryFactory.getValueFactory().integer(this.end));
		}
	}
	
	@Override
	public int hashCode() {
		if(len == 0) {
			//System.err.println("hashcode sublist: = " + ValueFactoryFactory.getValueFactory().list().hashCode() );
		
			return ValueFactoryFactory.getValueFactory().list().hashCode();
		}
		int hash = 123456789;
		
		for(int i = 0; i < len; i++){
		    hash = (hash << 1) ^ (hash >> 1) ^ base.get(start + i).hashCode();
		}

		return hash;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof IList || o instanceof SubList){
			IList other = (IList) o;
			if(fType.comparable(other.getType()) && (len == other.length())){
				for(int i = 0; i < len; i++){
					if(!base.get(start + i).equals(other.get(i))){
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	public boolean isEqual(IValue other) {
		return equals(other);
	}
	
	private void appendSubListElements(IListWriter w){
		for(int i = start; i < end; i++){
			w.append(base.get(i));
		}
	}

	public IList append(IValue elem) {
		IListWriter w = ValueFactoryFactory.getValueFactory().listWriter(elem.getType().lub(getElementType()));
		appendSubListElements(w);
		w.append(elem);
		return w.done();
	}

	public IList concat(IList other) {
		IListWriter w = ValueFactoryFactory.getValueFactory().listWriter(other.getElementType().lub(getElementType()));
		appendSubListElements(w);
		w.appendAll(other);
		return w.done();
	}

	public IValue get(int i) throws IndexOutOfBoundsException {
		if(start + i < 0 ) {
			throw RuntimeExceptionFactory.indexOutOfBounds(ValueFactoryFactory.getValueFactory().integer(start));
		}
		if(i >= end - start) {
			throw RuntimeExceptionFactory.indexOutOfBounds(ValueFactoryFactory.getValueFactory().integer(end));
		}
		return base.get(start + i);	
	}
	
	public Type getType(){
		return base.getType();
	}

	public Type getElementType() {
		return base.getElementType();
	}

	public IList insert(IValue elem) {
		IListWriter w = ValueFactoryFactory.getValueFactory().listWriter(elem.getType().lub(getElementType()));
		w.insert(elem);
		appendSubListElements(w);
		return w.done();
	}

	public boolean isEmpty() {
		return len == 0;
	}

	public int length() {
		return len;
	}

	public IList put(int i, IValue elem) throws FactTypeUseException,
			IndexOutOfBoundsException {
		
		IListWriter w = ValueFactoryFactory.getValueFactory().listWriter(elem.getType().lub(getElementType()));
		appendSubListElements(w);
		w.replaceAt(i, elem);
		return w.done();

	}

	public IList reverse() {
		IListWriter w = ValueFactoryFactory.getValueFactory().listWriter(getElementType());
		for(int i = end - 1; i >= start; i--){
			w.insert(base.get(start));
		}
		return w.done();
	}

	public Iterator<IValue> iterator() {
		return new SubListIterator(this);
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return v.visitList(this);
	}

	public boolean contains(IValue e) {
		for (IValue elem : this) {
			if (e.isEqual(elem)) {
				return true;
			}
		}
		return false;
	}

	public IList delete(IValue e) {
		IListWriter w = ValueFactoryFactory.getValueFactory().listWriter(getElementType());
		w.insertAll(this);
		w.delete(e);
		return w.done();
	}
}

class SubListIterator implements Iterator<IValue> {
	private SubList sl;
	private int cursor;
	private int end;
	
	SubListIterator(SubList sl){
		this.sl = sl;
		cursor = 0;
		end = sl.length();
	}

	public boolean hasNext() {
		return cursor < end;
	}

	public IValue next() {
		if(cursor >= end){
			throw new IndexOutOfBoundsException();
		}
		return sl.get(cursor++);
	}

	public void remove() {
		throw new UnsupportedOperationException("remove");
		
	}
}
