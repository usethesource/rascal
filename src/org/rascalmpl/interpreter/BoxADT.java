/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter;

import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

public class BoxADT {
	static final boolean DEBUG = false;
	static final private TypeStore ts;
	static final private TypeFactory tf;
	static final private Type boxlst, str;
	static final private Type box;

	public static Type getBox() {
		return box;
	}

	static private IValueFactory vf;
	static {
		tf = TypeFactory.getInstance();
		vf = ValueFactoryFactory.getValueFactory();
		ts = new TypeStore();
		box = tf.abstractDataType(ts, "Box");
		ts.declareAbstractDataType(box);
		boxlst = tf.listType(box);
		str = tf.stringType();
	}

	enum TAG {
		H(boxlst), V(boxlst), HOV(boxlst), HV(boxlst), I(boxlst), WD(boxlst), R(
				boxlst), A(boxlst), L(str), KW(box), VAR(box), NM(box), STRING(box),
				COMM(box), ESC(box);
		Type tag;

		TAG(Type tp) {
			tag = tf.constructor(ts, box, this.name(), tp, this.name()
					.toLowerCase());
			ts.declareConstructor(tag);
		}

		IConstructor create(IValue... t) {
			IValue r = t[0];
			// if (this== VAR) System.err.println("QQ:"+t[0]);
			if (!r.getType().isList() && this.ordinal() < L.ordinal()) {
				r = vf.list(t);
			}
			if (DEBUG)
				System.err.println("create:" + tag + " " + r);
			return vf.constructor(tag, r);
		}

		IConstructor create(String s) {
			if (DEBUG)
				System.err.println("create:" + tag + " " + s);
			return vf.constructor(tag, vf.string(s));
		}
	}

	static final IConstructor EMPTY = TAG.L.create("");
	static final IConstructor PLUS = TAG.L.create("+");
	static final IConstructor MINUS = TAG.L.create("-");
	static final IConstructor MULT = TAG.L.create("*");
	static final IConstructor DIVIDE = TAG.L.create("/");
	static final IConstructor MODULO = TAG.L.create("%");
	static final IConstructor MOD = TAG.L.create("/");
	static final IConstructor EQUALS = TAG.L.create("==");
	static final IConstructor EQUIVALENCE = TAG.L.create("<==>");
	static final IConstructor NOTEQUALS = TAG.L.create("!=");
	static final IConstructor NEGATION = TAG.L.create("!");
	static final IConstructor LT = TAG.L.create("< ");
	static final IConstructor LE = TAG.L.create("<=");
	static final IConstructor GT = TAG.L.create(" >");
	static final IConstructor GE = TAG.L.create(">=");
	static final IConstructor OR = TAG.L.create("||");
	static final IConstructor AMPERSAND = TAG.L.create("&");
	static final IConstructor AND = TAG.L.create("&&");
	static final IConstructor NOT = TAG.L.create("!");
	static final IConstructor LPAR = TAG.L.create("(");
	static final IConstructor RPAR = TAG.L.create(")");
	static final IConstructor LBRACK = TAG.L.create("[");
	static final IConstructor RBRACK = TAG.L.create("]");
	static final IConstructor LBLOCK = TAG.L.create("{");
	static final IConstructor RBLOCK = TAG.L.create("}");
	static final IConstructor ASSIGN = TAG.L.create("=");
	static final IConstructor COMMA = TAG.L.create(",");
	static final IConstructor SEMICOLON = TAG.L.create(";");
	static final IConstructor DOT = TAG.L.create(".");
	static final IConstructor AT = TAG.L.create("@");
	static final IConstructor HASH = TAG.L.create("#");
	static final IConstructor QUESTIONMARK = TAG.L.create("?");
	static final IConstructor COLON = TAG.L.create(":");
	static final IConstructor COLONCOLON = TAG.L.create("::");
	static final IConstructor VBAR = TAG.L.create("|");
	static final IConstructor ELOF = TAG.L.create("<-");
	static final IConstructor INTERSECTION = TAG.L.create("&");
	static final IConstructor RANGE = TAG.L.create("..");
	static final IConstructor CONGR = TAG.L.create("~");
	static final IConstructor SPACE = TAG.L.create(" ");

	// Type tagar[] = new Type[TAG.values().length];
	//
	// BoxADT() {
	// ts.declareAbstractDataType(box);
	// // label = tf.constructor(ts, box, "L", tf.stringType());
	// label = tf.constructor(ts, box, "L", tf.stringType(), "s");
	// ts.declareConstructor(label);
	// for (TAG tag : TAG.values()) {
	// tagar[tag.ordinal()] = tf.constructor(ts, box, tag.name(), tf
	// .valueType(), "b");
	// ts.declareConstructor(tagar[tag.ordinal()]);
	// }
	// }
	//
	// IConstructor createLabel(String s) {
	// IConstructor r = vf.constructor(label, vf.string(s));
	// return r;
	// }
	//
	// Type getType(TAG tag) {
	// return tagar[tag.ordinal()];
	// }
	//
	// IConstructor create(TAG tag, IConstructor t) {
	// IConstructor r = vf.constructor(getType(tag), t);
	// return r;
	// };

	static TypeStore getTypeStore() {
		return ts;
	}

	static IValueFactory getValueFactory() {
		return vf;
	}

	static public Type getTypeL() {
		return ts.lookupConstructor(box, "L", tf.stringType());
	}

	static IConstructor comma() {
		return BoxADT.TAG.L.create(",");
	}

	static IConstructor semicolumn() {
		return BoxADT.TAG.L.create(";");
	}

	static IList getEmptyList() {
		return vf.list(box);
	}

	static IList getList(IValue... t) {
		IList q = BoxADT.getEmptyList();
		for (IValue a : t) {
			if (a == null)
				continue;
			if (a.getType().isList()) {
				q = q.concat((IList) a);
			} else
				q = q.append(a);
		}
		return q;
	}

	static IConstructor KW(String s) {
		return BoxADT.TAG.KW.create(BoxADT.TAG.L.create(s));
	}

	static IConstructor NM(String s) {
		return BoxADT.TAG.NM.create(BoxADT.TAG.L.create(s));
	}
	
	static IConstructor STRING(String s) {
		return BoxADT.TAG.NM.create(BoxADT.TAG.L.create(s));
	}

	static IConstructor VAR(String s) {
		return BoxADT.TAG.VAR.create(BoxADT.TAG.L.create(s));
	}

	static IConstructor L(String s) {
		return BoxADT.TAG.L.create(s);
	}

	static IConstructor H(IValue... t) {
		return H(-1, t);
	}

	static IConstructor H(int hspace, IValue... t) {
		IList q = BoxADT.getEmptyList();
		for (IValue a : t) {
			if (a == null)
				continue;
			if (a.getType().isList()) {
				q = q.concat((IList) a);
			} else
				q = q.append(a);
		}
		IConstructor r = BoxADT.TAG.H.create(q);
		if (hspace >= 0)
			r = r.asAnnotatable().setAnnotation("hs", vf.integer(hspace));
		return r;
	}

	static IConstructor V(IValue... t) {
		return V(-1, t);
	}

	static IConstructor V(boolean indent, IValue... t) {
		return V(-1, indent, t);
	}

	static IConstructor V(int vspace, IValue... t) {
		return V(vspace, false, t);
	}

	static IList makeIndent(IList a, boolean first) {
		IList q = BoxADT.getEmptyList();
		for (IValue b : a) {
			q = q.append(first && q.isEmpty() ? b : I((IConstructor) b));
		}	
		return q;
	}

	static IConstructor V(int vspace, boolean indent, IValue... t) {
		IList q = BoxADT.getEmptyList();
		for (IValue a : t) {
			if (a == null)
				continue;
			if (a.getType().isList()) {

				q = q.concat(indent ? makeIndent((IList) a, q.isEmpty())
						: (IList) a);
			} else
				q = q.append(indent && !q.isEmpty() ? I(a) : a);
		}
		// int last = q.length()-1;
		// IConstructor c = ((IConstructor) q.get(last));
		// if (c.getConstructorType().getName()=="I") {
		// q=q.put(last, c.get(0));
		// }
		IConstructor r =  BoxADT.TAG.V.create(q);
		if (vspace >= 0)
			r = r.asAnnotatable().setAnnotation("vs", vf.integer(vspace));
		return r;
	}

	static IConstructor I(IValue... t) {
		return BoxADT.TAG.I.create(t);
	}

	static IConstructor HV(IValue... t) {
		return HV(-1, t);
	}

	static IConstructor HV(int hspace, IValue... t) {
		return HV(hspace, false , t);
		
	}

	static IConstructor HOV(IValue... t) {
		return HOV(-1, t);
	}

	static IConstructor HOV(boolean indent, IValue... t) {
		return HOV(-1, indent, t);
	}

	static IConstructor HOV(int hspace, IValue... t) {
		return HOV(hspace, false, t);
	}

	static IConstructor HOV(int hspace, boolean indent, IValue... t) {
		IList q = BoxADT.getEmptyList();
		for (IValue a : t) {
			if (a == null)
				continue;
			if (a.getType().isList()) {
				q = q.concat(indent ? makeIndent((IList) a, q.isEmpty())
						: (IList) a);
			} else
				q = q.append(indent && !q.isEmpty() ? I(a) : a);
		}
		IConstructor r = BoxADT.TAG.HOV.create(q);
		if (hspace >= 0)
			r = r.asAnnotatable().setAnnotation("hs", vf.integer(hspace));
		r = r.asAnnotatable().setAnnotation("vs", vf.integer(0));
		return r;
	}

	static IConstructor HV(int hspace, boolean indent, IValue... t) {
		IList q = BoxADT.getEmptyList();
		for (IValue a : t) {
			if (a == null)
				continue;
			if (a.getType().isList()) {
				q = q.concat(indent ? makeIndent((IList) a, q.isEmpty())
						: (IList) a);
			} else
				q = q.append(indent && !q.isEmpty() ? I(a) : a);
		}
		IConstructor r = BoxADT.TAG.HV.create(q);
		if (hspace >= 0)
			r = r.asAnnotatable().setAnnotation("hs", vf.integer(hspace));
		r = r.asAnnotatable().setAnnotation("vs", vf.integer(0));
		return r;
	}

}
