package org.rascalmpl.values;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IAnnotatable;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.io.StandardTextWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.rascalmpl.interpreter.asserts.ImplementationError;

public abstract class OrgString implements IString, Iterable<Integer> {
	protected static final OriginValueFactory vf = (OriginValueFactory) ValueFactoryFactory.getValueFactory();
		
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
		if (length() == 0) {
			return other;
		}
		if (other.length() == 0) {
			return this;
		}
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
	
	@Override
	public int compare(IString other) {
		OrgString os = (OrgString)other;
		Iterator<Integer> iter1 = this.iterator();
		Iterator<Integer> iter2 = os.iterator();
		while (true) {
			if (iter1.hasNext() && iter2.hasNext()) {
				Integer x1 = iter1.next();
				Integer x2 = iter2.next();
				if (x1 > x2) {
					return 1; // I'm bigger than other
				}
				if (x1 < x2) {
					return -1; // I'm smaller
				}
				continue; // we don't know yet
			}
			if (iter1.hasNext()) {
				return 1; // I'm longer, hence bigger
			}
			if (iter2.hasNext()) {
				return -1; // Other is longer, hence I'm smaller.
			}
			return 0; // reached the end, we're equal.
		}
	}

	
	public abstract void accept(IOrgStringVisitor visitor);

	public IString replaceAll(String sub, IString string) {
		if (getValue().indexOf("public") != -1) {
			System.err.println("Bla");
		}
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
		return equals(other);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (this == other) {
			return true;
		}
		if (!(other instanceof IString)) {
			return false;
		}
		if (!(other instanceof OrgString)) {
			throw new ImplementationError("Mixing factories!!!");
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

	public IString slice(int first, int second, int end) {
		int inc = second - first;
		if (inc == 1) {
			return substring(first, end);
		}
		IString result = null;
		for (int i = first; i < end; i += inc) {
			if (result == null) {
				result = substring(i, i + 1);
			}
			else {
				result = result.concat(substring(i, i + 1));
			}
		}
		return result;
	}
	
	public String toString() {
		try(StringWriter stream = new StringWriter()) {
			new StandardTextWriter().write(this, stream);
			return stream.toString();
		} catch (IOException ioex) {
			// this never happens
		}
		return "";
	}

	public IList split(String sep) {
		IListWriter w = vf.listWriter();
		split(w, sep);
		return w.done();
	}
	
	public abstract OrgString capitalize();
	
	private void split(IListWriter w, String sep) {
		int ind = indexOf(sep);		
		if (ind != -1) {
			w.append(substring(0, ind));
			((OrgString)substring(ind + sep.length())).split(w, sep);
		}
		else {
			w.append(this);
		}
	}
	
	@Override
	public String getValue() {
		StringBuilder b = new StringBuilder();
		serialize(b);
		return b.toString();
	}

	
	public abstract void serialize(StringBuilder b);
	
	
	public OrgString escape(IMap substitutions) {
		// REALLY slow
		OrgString result = this;
		for (IValue k: substitutions) {
			IString s = ((IString)k);
			IString v = (IString)substitutions.get(s);
			result = (OrgString) result.replaceAll(s.getValue(), v);
		}
		return result;
	}
	
}