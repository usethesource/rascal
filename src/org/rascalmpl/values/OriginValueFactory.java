package org.rascalmpl.values;



import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.values.origins.Factory;

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
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof IString)) {
				return false;
			}
			return compare((IString)obj) == 0;
		}
	}
	
	private abstract class Unit extends TString {
		private final String value;
		
		public Unit(String value) {
			this.value = value;
		}
		
		protected int length() {
			return value.length();
		}
		
		@Override
		public String getValue() {
			return value;
		}

	}
	
	private abstract class OrgUnit extends Unit {
		private final ISourceLocation origin;
		
		protected OrgUnit(ISourceLocation origin, String value) {
			super(value);
			this.origin = origin;
		}
		
		public ISourceLocation getOrigin() {
			return origin;
		}
		
	}
	
	private class Expression extends OrgUnit {
		protected Expression(ISourceLocation origin, String value) {
			super(origin, value);
		}

		public IList getOrigins() {
			return list(tuple(this, constructor(Factory.Origin_expression, getOrigin())));
		}
		
	}
	
	private class Literal extends OrgUnit {
		protected Literal(ISourceLocation origin, String value) {
			super(origin, value);
		}

		@Override
		public IList getOrigins() {
			return list(tuple(this, constructor(Factory.Origin_literal, getOrigin())));
		}
		
	}
	
	private class Anonymous extends Unit {
		protected Anonymous(String value) {
			super(value);
		}

		@Override
		public IList getOrigins() {
			return list(tuple(this, constructor(Factory.Origin_none)));
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
	
	@Override
	public IString string(String value) {
		return new Anonymous(value);
	}
	
	public IString literal(ISourceLocation origin, String s) {
		return new Literal(origin, s);
	}

	public IString expression(ISourceLocation origin, String s) {
		return new Expression(origin, s);
	}
	
	

}
