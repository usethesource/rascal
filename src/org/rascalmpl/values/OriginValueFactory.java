package org.rascalmpl.values;



import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.interpreter.StringTemplateConverter;
import org.rascalmpl.values.uptr.Factory;

import com.sun.tools.javac.util.List;

public class OriginValueFactory extends ValueFactory {
	private final static Type STRING_TYPE = TypeFactory.getInstance().stringType();

	private static class InstanceKeeper{
		public final static ValueFactory instance = new OriginValueFactory();
	}
	
	public static ValueFactory getInstance(){
		return InstanceKeeper.instance;
	}
	
	public abstract class TString implements IString {
		
		@Override
		public Type getType() {
			return STRING_TYPE;
		}

		@Override
		public <T> T accept(IValueVisitor<T> v) throws VisitorException {
			return v.visitString(this);
		}
		
		@Override
		public IString concat(IString other) {
			return new Concat(this, (TString)other);
		}
		
		@Override
		public boolean isEqual(IValue other) {
			if (!(other instanceof IString)) {
				return false;
			}
			return getValue().equals(((IString)other).getValue());
		}
		
		@Override
		public int compare(IString other){
			int result = getValue().compareTo(other.getValue());
			
			if(result > 0) return 1;
			if(result < 0) return -1;
			
			return 0;
		}
		
		protected abstract int length();
		
		public abstract IList getOrigins();
	}
	
	
	private class Value extends TString {
		private final String value;
		private final ISourceLocation origin;
	
		protected Value(ISourceLocation origin, String value) {
			this.origin = origin;
			this.value = value;
		}
		
		
		protected int length() {
			return value.length();
		}
		
		@Override
		public String getValue() {
			return value;
		}

		@Override
		public IList getOrigins() {
			return list(tuple(this, origin));
		}
		
	}
	
	
	
	private class Concat extends TString {

		private final TString lhs;
		private final TString rhs;
		private final int length;

		public Concat(TString lhs, TString rhs) {
			this.lhs = lhs;
			this.rhs = rhs;
			this.length = lhs.length() + rhs.length();
		}

		@Override
		protected int length() {
			return length;
		}
		
		@Override
		public String getValue() {
			return lhs.getValue() + rhs.getValue();
		}

		@Override
		public IList getOrigins() {
			return lhs.getOrigins().concat(rhs.getOrigins());
		}
		
	}
	
	public IString string(ISourceLocation origin, String s) {
		return new Value(origin, s);
	}
	
	

}
