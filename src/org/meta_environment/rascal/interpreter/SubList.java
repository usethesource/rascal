package org.meta_environment.rascal.interpreter;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.List;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;

public class SubList implements IList {
	private final Type fType;
	
	private final int start;
	private final int len;
	private final int end;
	private final IList base;
	
	public SubList(List L, int start, int len){
		super();
		
		fType = L.getType();
		
		this.start = this.end = this.len = 0;
		this.base = null;
	}

	public SubList(IValue V, int start, int len){
		super();
		
		fType = V.getType();
		
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
		if(this.start < 0 || this.len > base.length()){
			throw new ImplementationError("Out of bounds");
		}
	}
	
	@Override
	public int hashCode() {
		return base.hashCode() + start << 8 + len << 4;
	}

	public boolean equals(Object o){
		if(o instanceof List || o instanceof SubList){
			List other = (List) o;
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
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		String sep = "";
		
		sb.append("[");
		sb.append("/type=" + getType() + ", base=" + base + ", start=" + start + ", end=" + end + "/ ");
		for(int i = start; i < end; i++){
			sb.append(sep);
			sep = ",";
			sb.append(base.get(i));
		}
		sb.append("]");
	
		return sb.toString();
	}

	private void appendSubListElements(IListWriter w){
		for(int i = start; i < end; i++){
			w.append(base.get(i));
		}
	}

	public IList append(IValue elem) {
		IListWriter w = ValueFactory.getInstance().listWriter(elem.getType().lub(getElementType()));
		appendSubListElements(w);
		w.append(elem);
		return w.done();
	}

	public IList concat(IList other) {
		IListWriter w = ValueFactory.getInstance().listWriter(other.getElementType().lub(getElementType()));
		appendSubListElements(w);
		w.appendAll(other);
		return w.done();
	}

	public IValue get(int i) throws IndexOutOfBoundsException {
		if(i < start || i >= end)
			new ArrayIndexOutOfBoundsException("SubList");
		return base.get(start + i);	
	}
	
	public Type getType(){
		return base.getType();
	}

	public Type getElementType() {
		return base.getElementType();
	}

	public IList insert(IValue elem) {
		IListWriter w = ValueFactory.getInstance().listWriter(elem.getType().lub(getElementType()));
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

	public IList put(int i, IValue elem) throws FactTypeError,
			IndexOutOfBoundsException {
		
		IListWriter w = ValueFactory.getInstance().listWriter(elem.getType().lub(getElementType()));
		appendSubListElements(w);
		w.replaceAt(i, elem);
		return w.done();

	}

	public IList reverse() {
		IListWriter w = ValueFactory.getInstance().listWriter(getElementType());
		for(int i = end - 1; i >= start; i--){
			w.insert(base.get(start));
		}
		return w.done();
	}

	public Iterator<IValue> iterator() {
		return new SubListIterator(this);
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		//TODO: Is this ok?
		return v.visitList(this);
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
			throw new ImplementationError("next called on exhausted SubListIterator");
		}
		return sl.get(cursor++);
	}

	public void remove() {
		throw new UnsupportedOperationException("remove");
		
	}
}
