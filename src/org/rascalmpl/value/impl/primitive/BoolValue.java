/*******************************************************************************
 * Copyright (c) 2012-2013 Centrum Wiskunde en Informatica (CWI)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Arnold Lankamp - interfaces and implementation
 *   * Jurgen Vinju
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.value.impl.primitive;

import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.impl.AbstractValue;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.visitors.IValueVisitor;

/*package*/ abstract class BoolValue extends AbstractValue implements IBool {
	/*package*/ final static BoolValue TRUE = new BoolValue() {
		@Override
		public boolean getValue() {
			return true;
		}

		public int hashCode() {
			return 1;
		}

		@Override
		public IBool not() {
			return FALSE;
		}

		@Override
		public IBool and(IBool other) {
			return other;
		}

		@Override
		public IBool or(IBool other) {
			return this;
		}

		@Override
		public IBool xor(IBool other) {
			return other == this ? FALSE : TRUE;
		}

		@Override
		public IBool implies(IBool other) {
			return other;
		}
	};
	/*package*/ final static BoolValue FALSE = new BoolValue() {
		@Override
		public boolean getValue() {
			return false;
		}

		@Override
		public IBool not() {
			return TRUE;
		}

		@Override
		public IBool and(IBool other) {
			return this;
		}

		@Override
		public IBool or(IBool other) {
			return other;
		}

		@Override
		public IBool xor(IBool other) {
			return other;
		}

		@Override
		public IBool implies(IBool other) {
			return TRUE;
		}

		public int hashCode() {
			return 2;
		}
	};
	private final static Type BOOL_TYPE = TypeFactory.getInstance().boolType();

	private BoolValue() {
		super();
	}

	/*package*/ static BoolValue getBoolValue(boolean bool) {
		return bool ? TRUE : FALSE;
	}

	public abstract int hashCode();

	public boolean equals(Object o) {
		return this == o;
	}

	@Override
	public Type getType() {
		return BOOL_TYPE;
	}

	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
		return v.visitBoolean(this);
	}

	@Override
	public boolean isEqual(IValue value) {
		return this == value;
	}

	@Override
	public IBool equivalent(IBool other) {
		return other == this ? TRUE : this;
	}

	@Override
	public String getStringRepresentation() {
		return toString();
	}

}
