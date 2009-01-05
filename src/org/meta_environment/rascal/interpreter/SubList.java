package org.meta_environment.rascal.interpreter;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.Value;
import org.eclipse.imp.pdb.facts.impl.hash.List;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public class SubList extends Value implements IList {
	private final int start;
	private final int len;
	private final int end;
	private final List base;
	
	public SubList(List base, int start, int len){
		super(base);

		this.base = base;
		this.start = start;
		this.len = len;
		this.end = start + len;
	}
	
	public boolean equals(Object o){
		System.err.println("equals" + this + ", " + o);
		if(o instanceof List || o instanceof SubList){
			List other = (List) o;
			if(fType.comparable(other.getType()) && equalAnnotations((Value) o) && (len == other.length())){
				for(int i = 0; i < len; i++){
					if(!base.get(start + i).equals(other.get(i))){
						System.err.println("false");
						return false;
					}
				}
				System.err.println("true");
				return true;
			} else {
				System.err.println("false");
				return false;
			}
		}
		System.err.println("false");
		return false;
	}
	
	@Override
	public int hashCode() {
		return base.hashCode() + start << 8 + len << 4;
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

	@Override
	protected IValue clone(String label, IValue value) {
		return null;
	}
	
	private void insertSubListElements(IListWriter w){
		for(int i = start; i < end; i++){
			w.insert(base.get(i));
		}
	}

	public IList append(IValue elem) {
		IListWriter w = ValueFactory.getInstance().listWriter(elem.getType().lub(getElementType()));
		insertSubListElements(w);
		w.append(elem);
		w.setAnnotations(fAnnotations);
		return w.done();
	}

	public IList concat(IList other) {
		IListWriter w = ValueFactory.getInstance().listWriter(other.getElementType().lub(getElementType()));
		insertSubListElements(w);
		w.appendAll(other);
		return w.done();
	}

	public IValue get(int i) throws IndexOutOfBoundsException {
		if(i < start || i >= end)
			new ArrayIndexOutOfBoundsException("SubList");
		return base.get(start + i);	
	}

	public Type getElementType() {
		return base.getElementType();
	}

	public IList insert(IValue elem) {
		IListWriter w = ValueFactory.getInstance().listWriter(elem.getType().lub(getElementType()));
		insertSubListElements(w);
		w.insert(elem);
		w.setAnnotations(fAnnotations);
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
		insertSubListElements(w);
		w.replaceAt(i, elem);
		return w.done();

	}

	public IList reverse() {
		IListWriter w = ValueFactory.getInstance().listWriter(getElementType());
		for(int i = end - 1; i >= start; i--){
			w.insert(base.get(start));
		}
		w.setAnnotations(fAnnotations);
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
		System.err.println("hasNext: " + (cursor < end -1));
		return cursor < end;
	}

	public IValue next() {
		if(cursor >= end){
			throw new RascalBug("next called on exhausted SubListIterator");
		}
		return sl.get(cursor++);
	}

	public void remove() {
		// TODO Auto-generated method stub
		
	}
}
