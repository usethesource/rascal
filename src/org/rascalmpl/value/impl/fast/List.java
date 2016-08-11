/*******************************************************************************
* Copyright (c) 2009 Centrum Wiskunde en Informatica (CWI)
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Arnold Lankamp - interfaces and implementation
*    Paul Klint - added new methods and SubList
*******************************************************************************/
package org.rascalmpl.value.impl.fast;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListRelation;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.exceptions.IllegalOperationException;
import org.rascalmpl.value.impl.AbstractValue;
import org.rascalmpl.value.impl.func.ListFunctions;
import org.rascalmpl.value.impl.util.collections.ShareableValuesList;
import org.rascalmpl.value.io.StandardTextWriter;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.visitors.IValueVisitor;

/**
 * Implementation of IList.
 * 
 * @author Arnold Lankamp
 */
/*package*/ class List extends AbstractValue implements IList{
	protected final static TypeFactory typeFactory = TypeFactory.getInstance();
	protected final static Type voidType = typeFactory.voidType();
	
	protected final Type listType;
	protected final Type elementType;
	
	protected final ShareableValuesList data;

	protected int hashCode = 0;

	/*package*/ static IList newList(Type elementType, ShareableValuesList data) {
		return new List(elementType, data);
	}
	
	private List(Type elementType, ShareableValuesList data){
		super();
		
		if (data.isEmpty())
			this.elementType = voidType;
		else
			this.elementType = elementType;		
		
		this.listType = typeFactory.listType(this.elementType);
				
		this.data = data;
		
	}
	
	/*package*/ static ListWriter createListWriter(Type eltType){
		return new ListWriter(eltType);
	}

	/*package*/ static ListWriter createListWriter(){
		return new ListWriter();
	}

	public Type getType(){
		return listType;
	}

	public Type getElementType(){
		return elementType;
	}

	public int length(){
		return data.size();
	}

	public boolean isEmpty(){
		return length() == 0;
	}

	public IValue get(int index){
		return data.get(index);
	}
	
	public boolean contains(IValue element){
		return data.contains(element);
	}

	public Iterator<IValue> iterator(){
		return data.iterator();
	}
	
	public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E{
			return v.visitList(this);
	}

	public IList append(IValue element){
		ShareableValuesList newData = new ShareableValuesList(data);
		newData.append(element);

		Type newElementType = elementType.lub(element.getType());
		return new ListWriter(newElementType, newData).done();
	}

	public IList concat(IList other){
		ShareableValuesList newData = new ShareableValuesList(data);
		Iterator<IValue> otherIterator = other.iterator();
		while(otherIterator.hasNext()){
			newData.append(otherIterator.next());
		}
		
		Type newElementType = elementType.lub(other.getElementType());
		return new ListWriter(newElementType, newData).done();
	}

	public IList insert(IValue element){
		ShareableValuesList newData = new ShareableValuesList(data);
		newData.insert(element);

		Type newElementType = elementType.lub(element.getType());
		return new ListWriter(newElementType, newData).done();
	}
	
	public IList put(int index, IValue element) throws IndexOutOfBoundsException{
		ShareableValuesList newData = new ShareableValuesList(data);
		newData.set(index, element);

		Type newElementType = elementType.lub(element.getType());
		return new ListWriter(newElementType, newData).done();
	}
	
	public IList replace(int first, int second, int end, IList repl)
			throws FactTypeUseException, IndexOutOfBoundsException {
		ShareableValuesList newData = new ShareableValuesList();
		int rlen = repl.length();
		int increment = Math.abs(second - first);
		if(first < end){
			int dataIndex = 0;
			// Before begin
			while(dataIndex < first){
				newData.append(data.get(dataIndex++));
			}
			int replIndex = 0;
			boolean wrapped = false;
			// Between begin and end
			while(dataIndex < end){
				newData.append(repl.get(replIndex++));
				if(replIndex == rlen){
					replIndex = 0;
					wrapped = true;
				}
				dataIndex++; //skip the replaced element
				for(int j = 1; j < increment && dataIndex < end; j++){
					newData.append(data.get(dataIndex++));
				}
			}
			if(!wrapped){
				while(replIndex < rlen){
					newData.append(repl.get(replIndex++));
				}
			}
			// After end
			int dlen = data.size();
			while( dataIndex < dlen){
				newData.append(data.get(dataIndex++));
			}
		} else {
			// Before begin (from right to left)
			int dataIndex = data.size() - 1;
			while(dataIndex > first){
				newData.insert(data.get(dataIndex--));
			}
			// Between begin (right) and end (left)
			int replIndex = 0;
			boolean wrapped = false;
			while(dataIndex > end){
				newData.insert(repl.get(replIndex++));
				if(replIndex == repl.length()){
					replIndex = 0;
					wrapped = true;
				}
				dataIndex--; //skip the replaced element
				for(int j = 1; j < increment && dataIndex > end; j++){
					newData.insert(data.get(dataIndex--));
				}
			}
			if(!wrapped){
				while(replIndex < rlen){
					newData.insert(repl.get(replIndex++));
				}
			}
			// Left of end
			while(dataIndex >= 0){
				newData.insert(data.get(dataIndex--));
			}
		}
		Type newElementType = elementType.lub(repl.getElementType());
		return new ListWriter(newElementType, newData).done();
	}
	
	public IList delete(int index){
		ShareableValuesList newData = new ShareableValuesList(data);
		newData.remove(index);
		
		Type newElementType = TypeFactory.getInstance().voidType();
		for(IValue el : newData)
			newElementType = newElementType.lub(el.getType());
		
		return new ListWriter(newElementType, newData).done();
	}
	
	public IList delete(IValue element){
		ShareableValuesList newData = new ShareableValuesList(data);
		
		if (newData.remove(element)) {
		  Type newElementType = TypeFactory.getInstance().voidType();
		  
		  for (IValue el : newData) {
		    newElementType = newElementType.lub(el.getType());
		  }
		  
		  return new ListWriter(newElementType, newData).done();
		}
		
		return this;
	}

	public IList reverse(){
		ShareableValuesList newData = new ShareableValuesList(data);
		newData.reverse();
		
		return new ListWriter(elementType, newData).done();
	}
	
	@Override
	public IList shuffle(Random rand) {
		ShareableValuesList newData = new ShareableValuesList(data);
		// we use Fisher–Yates shuffle (or Knuth shuffle)
		// unbiased and linear time, since set and get are O(1)
		for (int i= newData.size() - 1; i >= 1; i--) {
			// we use the stack as tmp variable :)
			newData.set(i, newData.set(rand.nextInt(i + 1), newData.get(i)));
		}
		return new ListWriter(elementType, newData).done();
	}
	
	public  IList sublist(int offset, int length){
		if(length < 4){
			return materializedSublist(offset, length);
		}
		return new SubList(this, offset, length);
	}
	
	IList materializedSublist(int offset, int length){
		ShareableValuesList newData = data.subList(offset, length);
		
		Type newElementType = TypeFactory.getInstance().voidType();
		for(IValue el : newData) {
		    if (newElementType.equals(this.elementType)) {
		        // the type can only get more specific
		        // once we've reached the type of the whole list, we can stop lubbing.
		        break;
		    }
			newElementType = newElementType.lub(el.getType());
		}
		
		return new ListWriter(newElementType, newData).done();
	}
	
	public int hashCode(){
		if (hashCode == 0) {
			hashCode = data.hashCode();
		}
		return hashCode;
	}

	public boolean equals(Object o){
		if(o == this) return true;
		if(o == null) return false;
		
		if(o instanceof List) {
			List otherList = (List) o;
			
			if (getType() != otherList.getType()) return false;
			
			if (hashCode() != otherList.hashCode()) return false;
			
			if (listType != otherList.listType) return false;
			
			return data.equals(otherList.data);
		}
		
		if(o instanceof SubList){
			IList otherList = (SubList) o;
			if (getType() != otherList.getType()) return false;
			
			if (hashCode() != otherList.hashCode()) return false;
			
			if (listType != otherList.getType()) return false;
			 	
			return ListFunctions.equals(ValueFactory.getInstance(), this, otherList);
		}
		
		return false;
	}

	public boolean isEqual(IValue value){
		if(value == this) return true;
		if(value == null) return false;
		
		if(value instanceof List){
			List otherList = (List) value;
			
			return data.isEqual(otherList.data);
		}
		else if (value instanceof IList) {
			return ListFunctions.isEqual(ValueFactory.getInstance(), this, value);
		}
		
		return false;
	}
	
	public IList product(IList lst){
		Type resultType = TypeFactory.getInstance().tupleType(getElementType(),lst.getElementType());
		ListWriter w = new ListWriter(resultType);

		for(IValue t1 : this){
			for(IValue t2 : lst){
				IValue vals[] = {t1, t2};
				ITuple t3 = Tuple.newTuple(resultType, vals);
				w.insert(t3);
			}
		}

		return (IList) w.done();
	}

	public IList intersect(IList other) {
		IListWriter w = ValueFactory.getInstance().listWriter();

		if(other instanceof List){
			List o = (List) other;

			for(IValue v : data){
				if(o.data.contains(v)){
					w.append(v);
				}
			}

			return w.done();
		}
		IList o = (IList) other;

		for(IValue v : data){
			if(o.contains(v)){
				w.append(v);
			}
		}
		return w.done();
	}
	
	public IList subtract(IList lst) {
		IListWriter w = ValueFactory.getInstance().listWriter();
		for (IValue v: this.data) {
			if (lst.contains(v)) {
				lst = lst.delete(v);
			} else
				w.append(v);
		}
		return w.done();
	}

	public boolean isSubListOf(IList lst) {
		int j = 0;
		nextchar:
			for(IValue elm : this.data){
				while(j < lst.length()){
					if(elm.isEqual(lst.get(j))){
						j++;
						continue nextchar;
					} else
						j++;
				}
				return false;
			}
		return true;
	}

	@Override
	public boolean isRelation() {
		return getType().isListRelation();
	}

	@Override
	public IListRelation<IList> asRelation() {
		if (!isRelation())
			throw new IllegalOperationException(
					"Cannot be viewed as a relation.", getType());

		return new RelationViewOnList(this);
	}
}

class SubList extends AbstractValue implements IList {

	private final IList base;
	private final int offset;
	private final int length;
	private int hashCode;
	private final Type elementType;

	SubList(IList base, int offset, int length){
		this.base = base;
		this.offset = offset;
		this.length = length;
		
		int end = offset + length;
		
		if(offset < 0) throw new IndexOutOfBoundsException("Offset may not be smaller than 0.");
		if(length < 0) throw new IndexOutOfBoundsException("Length may not be smaller than 0.");
		if(end > base.length()) throw new IndexOutOfBoundsException("'offset + length' may not be larger than 'list.size()'");

		Type newElementType = TypeFactory.getInstance().voidType();
		Type baseElementType = base.getElementType();
		
		for(int i = offset; i < end; i++){
			IValue el = base.get(i);
		    if (newElementType.equals(baseElementType)) {
		        // the type can only get more specific
		        // once we've reached the type of the whole list, we can stop lubbing.
		        break;
		    }
			newElementType = newElementType.lub(el.getType());
		}
		elementType = newElementType;
	}
	
	IList materialize(){
		ListWriter w = new ListWriter();
		int end = offset + length;
		for(int i = offset; i < end; i++){
			w.append(base.get(i));
		}
		return w.done();
	}
	
	IList maybeMaterialize(IList lst){
		if(lst instanceof SubList){
			return ((SubList) lst).materialize();
		}
		return lst;
	}
	
	@Override
	public Type getType() {
		return TypeFactory.getInstance().listType(elementType);
	}
	
	@Override
	public Type getElementType(){
		return elementType;
	}
	
	public int hashCode(){
		if (hashCode == 0) {
			hashCode = ListFunctions.hashCode( ValueFactory.getInstance(), this);
		}
		return hashCode;
	}
	
	public boolean equals(Object o){
		if(o == this) return true;
		if(o == null) return false;
		
		if(o instanceof List) {			
			return ((List) o).equals(this);
		}
		
		if(o instanceof SubList){
			SubList otherList = (SubList) o;
			
			return base.equals(otherList.base) && offset == otherList.offset && length == otherList.length;
		}
		
		return false;
	}
	
	@Override
	public boolean isEqual(IValue value){
		if(value == this) return true;
		if(value == null) return false;
		
		if(value instanceof List){
			List otherList = (List) value;
			
			return otherList.isEqual(this);
		}
		else if (value instanceof IList) {
			return ListFunctions.isEqual(ValueFactory.getInstance(), this, value);
		}
		
		return false;
	}
	
	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
		return v.visitList(this);
	}

	public String toString() {
		return StandardTextWriter.valueToString(this);
	}
	
	@Override
	public Iterator<IValue> iterator() {
		return new SubListIterator(base, offset, length);
	}

	@Override
	public IList append(IValue val) {
		ListWriter w = new ListWriter();
		int end = offset + length;
		for(int i = offset; i < end; i++){
			w.append(base.get(i));
		}
		w.append(val);
		return w.done();
	}

	@Override
	public IListRelation<IList> asRelation() {
		return materialize().asRelation();
	}

	@Override
	public IList concat(IList lst) {
		ListWriter w = new ListWriter();
		int end = offset + length;
		for(int i = offset; i < end; i++){
			w.append(base.get(i));
		}
		for(IValue v : lst){
			w.append(v);
		}
		
		return w.done();
	}

	@Override
	public boolean contains(IValue val) {
		int end = offset + length;
		for(int i = offset; i < end; i++){
			if(base.get(i).equals(val)){
				return true;
			}
		}
		return false;
	}

	@Override
	public IList delete(IValue val) {
		ListWriter w = new ListWriter();
		int end = offset + length;
		for(int i = offset; i < end; i++){
			IValue elm = base.get(i);
			if(!elm.equals(val)){
				w.append(elm);
			}
		}
		return w.done();
	}

	@Override
	public IList delete(int n) {
		ListWriter w = new ListWriter();
		int end = offset + length;
		for(int i = offset; i < end; i++){
			if(i != n){
				w.append(base.get(i));
			}
		}
		return w.done();
	}

	@Override
	public IValue get(int n) throws IndexOutOfBoundsException {
		return base.get(offset + n);
	}

	@Override
	public IList insert(IValue val) {
		ListWriter w = new ListWriter();
		int end = offset + length;
		w.insert(val);;
		for(int i = offset; i < end; i++){
			w.append(base.get(i));
		}
		return w.done();
	}

	@Override
	public IList intersect(IList arg0) {
		return materialize().intersect(maybeMaterialize(arg0));
	}

	@Override
	public boolean isEmpty() {
		return length == 0;
	}

	@Override
	public boolean isRelation() {
		return getType().isListRelation();
	}

	@Override
	public boolean isSubListOf(IList lst) {
		int end = offset + length;
		int j = 0;
		nextchar:
			for(int i = offset; i < end; i++){
				IValue elm = base.get(i);
				while(j < lst.length()){
					if(elm.isEqual(lst.get(j))){
						j++;
						continue nextchar;
					} else
						j++;
				}
				return false;
			}
		return true;
	}

	@Override
	public int length() {
		return length;
	}

	@Override
	public IList product(IList arg0) {
		return materialize().product(maybeMaterialize(arg0));
	}

	@Override
	public IList put(int n, IValue val) throws FactTypeUseException, IndexOutOfBoundsException {
		ListWriter w = new ListWriter();
		int end = offset + length;
		for(int i = offset; i < end; i++){
			if(i == n){
				w.append(val);
			} else {
				w.append(base.get(i));
			}
		}
		return w.done();
	}

	@Override
	public IList replace(int arg0, int arg1, int arg2, IList arg3)
			throws FactTypeUseException, IndexOutOfBoundsException {
		return materialize().replace(arg0,  arg1,  arg2, arg3);
	}

	@Override
	public IList reverse() {
		ListWriter w = new ListWriter();
		for(int i = offset + length - 1; i >= offset; i--){	
			w.append(base.get(i));
		}
		return w.done();
	}

	@Override
	public IList shuffle(Random arg0) {
		return materialize().shuffle(arg0);
	}

	@Override
	public IList sublist(int offset, int length) {
		return new SubList(base, this.offset + offset, length);
	}

	@Override
	public IList subtract(IList lst) {
		IListWriter w = ValueFactory.getInstance().listWriter();
		int end = offset + length;
		for(int i = offset; i < end; i++){
			IValue v = base.get(i);
			if (lst.contains(v)) {
				lst = lst.delete(v);
			} else
				w.append(v);
		}
		return w.done();
	}
}

final class SubListIterator implements Iterator<IValue> {
	private int cursor;
	private final int end;
	private final IList base;

	public SubListIterator(IList base, int offset, int length) {
		this.base = base;
		this.cursor = offset;
		this.end = offset + length;
	}

	public boolean hasNext() {
		return this.cursor < end;
	}

	public IValue next() {
		if(this.hasNext()) {
			return base.get(cursor++);
		}
		throw new NoSuchElementException();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}

