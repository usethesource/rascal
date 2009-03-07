package org.meta_environment.rascal.interpreter.LazySet;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.exceptions.ImplementationException;

abstract class LazySet implements ISet {
	protected final ISet base;
	private final Type fType;
	protected ISet partner;

	public LazySet(IValue V) {
		super();

		fType = V.getType();

		if (V instanceof ISet) {
			this.base = (ISet) V;
		} else if (V instanceof LazySet) {
			LazySet other = (LazySet) V;
			this.base = other;
		} else {
			throw new ImplementationException("Illegal value in LazySet");
		}
	}
	
	public void typecheckSet(ISet V){
		if (V instanceof ISet) {
			this.partner = (ISet) V;
		} else if (V instanceof LazySet) {
			this.partner = (LazySet) partner;
		} else {
			throw new ImplementationException("Illegal value in LazySet");
		}
	}
	
	public void typecheckElement(IValue V){
		
	}

	@Override
	public int hashCode() {
		return base.hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof ISet || o instanceof LazySet) {
			ISet other = (ISet) o;
			if (fType.comparable(other.getType())) {
				return base.isSubsetOf(other) && other.isSubsetOf(this);
			} else {
				return false;
			}
		}
		return false;
	}

	public boolean isEqual(IValue other) {
		return equals(other);
	}

	public Type getType() {
		return base.getType();
	}

	public Type getElementType() {
		return base.getElementType();
	}
	
	public IRelation product(ISet set) {
		Type elmType1 = getElementType();
		Type elmType2 = set.getElementType();
		Type resType = TypeFactory.getInstance().relType(elmType1, elmType2);
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		IRelationWriter w = resType.writer(vf);
		for(IValue x : this){
			for(IValue y : set){
				w.insert(vf.tuple(x,y));
			}
		}
		return w.done();
	}
	
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		//TODO: Is this ok?
		return v.visitSet(this);
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		String sep = "";
		for(IValue v : this){
			sb.append(sep).append(v.toString());
			sep = ", ";
		}
		sb.append("}");
		return sb.toString();
	}
}
