package org.rascalmpl.values;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IAnnotatable;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;

public abstract class OrgString implements IString, Iterable<Integer> {
	private final static Type STRING_TYPE = TypeFactory.getInstance().stringType();

	@Override
	public Type getType() {
		return STRING_TYPE;
	}
	
	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E {
		return v.visitString(this);
	}

	@Override
	public IString concat(IString other) {
		return new Concat(this, (OrgString)other);
	}

	@Override
	public IString substring(int start) {
		return substring(start, length());
	}
	
	@Override
	public boolean isAnnotatable() {
		return false;
	}

	@Override
	public IAnnotatable<? extends IValue> asAnnotatable() {
		throw new IllegalOperationException(
				"Cannot be viewed as annotatable.", getType());
	}

	
	public abstract void accept(IOrgStringVisitor visitor);

	public IString replaceAll(String sub, IString string) {
		int ind = indexOf(sub);
		if (ind != -1) {
			IString l = substring(0, ind);
			IString r = substring(ind + sub.length(), length());
			return l.concat(string.concat(((OrgString) r).replaceAll(sub, string)));
		}
		return this;
	}
	
	public abstract int indexOf(String str);
	
	@Override
	public boolean isEqual(IValue other) {
		if (!(other instanceof IString)) {
			return false;
		}
		if (length() != ((IString)other).length()) {
			return false;
		}
		OrgString os = (OrgString)other;
		Iterator<Integer> iter1 = iterator();
		Iterator<Integer> iter2 = os.iterator();
		while (iter1.hasNext() && iter2.hasNext()) {
			if (iter1.next() != iter2.next()) {
				return false;
			}
		}
		return true;
	}

}