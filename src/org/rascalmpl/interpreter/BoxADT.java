package org.rascalmpl.interpreter;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;

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
		vf = ValueFactory.getInstance();
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
			if (!r.getType().isListType() && this.ordinal() < L.ordinal()) {
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
	static final IValue PLUS = TAG.L.create("+");
	static final IValue MINUS = TAG.L.create("-");
	static final IValue MULT = TAG.L.create("*");
	static final IValue DIVIDE = TAG.L.create("/");
	static final IValue MODULO = TAG.L.create("%");
	static final IValue MOD = TAG.L.create("/");
	static final IValue EQUALS = TAG.L.create("==");
	static final IValue EQUIVALENCE = TAG.L.create("<==>");
	static final IValue NOTEQUALS = TAG.L.create("!=");
	static final IValue NEGATION = TAG.L.create("!");
	static final IValue LT = TAG.L.create("< ");
	static final IValue LE = TAG.L.create("<=");
	static final IValue GT = TAG.L.create(" >");
	static final IValue GE = TAG.L.create(">=");
	static final IValue OR = TAG.L.create("||");
	static final IValue AMPERSAND = TAG.L.create("&");
	static final IValue AND = TAG.L.create("&&");
	static final IValue NOT = TAG.L.create("!");
	static final IValue LPAR = TAG.L.create("(");
	static final IValue RPAR = TAG.L.create(")");
	static final IValue LBRACK = TAG.L.create("[");
	static final IValue RBRACK = TAG.L.create("]");
	static final IValue LBLOCK = TAG.L.create("{");
	static final IValue RBLOCK = TAG.L.create("}");
	static final IValue ASSIGN = TAG.L.create("=");
	static final IValue COMMA = TAG.L.create(",");
	static final IValue SEMICOLON = TAG.L.create(";");
	static final IValue DOT = TAG.L.create(".");
	static final IValue AT = TAG.L.create("@");
	static final IValue HASH = TAG.L.create("#");
	static final IValue QUESTIONMARK = TAG.L.create("?");
	static final IValue COLON = TAG.L.create(":");
	static final IValue COLONCOLON = TAG.L.create("::");
	static final IValue VBAR = TAG.L.create("|");
	static final IValue ELOF = TAG.L.create("<-");
	static final IValue INTERSECTION = TAG.L.create("&");
	static final IValue RANGE = TAG.L.create("..");
	static final IValue CONGR = TAG.L.create("~");
	static final IValue SPACE = TAG.L.create(" ");

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
	// IValue createLabel(String s) {
	// IValue r = vf.constructor(label, vf.string(s));
	// return r;
	// }
	//
	// Type getType(TAG tag) {
	// return tagar[tag.ordinal()];
	// }
	//
	// IValue create(TAG tag, IValue t) {
	// IValue r = vf.constructor(getType(tag), t);
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

	static IValue comma() {
		return BoxADT.TAG.L.create(",");
	}

	static IValue semicolumn() {
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
			if (a.getType().isListType()) {
				q = q.concat((IList) a);
			} else
				q = q.append(a);
		}
		return q;
	}

	static IValue KW(String s) {
		return BoxADT.TAG.KW.create(BoxADT.TAG.L.create(s));
	}

	static IValue NM(String s) {
		return BoxADT.TAG.NM.create(BoxADT.TAG.L.create(s));
	}
	
	static IValue STRING(String s) {
		return BoxADT.TAG.NM.create(BoxADT.TAG.L.create(s));
	}

	static IValue VAR(String s) {
		return BoxADT.TAG.VAR.create(BoxADT.TAG.L.create(s));
	}

	static IValue L(String s) {
		return BoxADT.TAG.L.create(s);
	}

	static IValue H(IValue... t) {
		return H(-1, t);
	}

	static IValue H(int hspace, IValue... t) {
		IList q = BoxADT.getEmptyList();
		for (IValue a : t) {
			if (a == null)
				continue;
			if (a.getType().isListType()) {
				q = q.concat((IList) a);
			} else
				q = q.append(a);
		}
		IConstructor r = BoxADT.TAG.H.create(q);
		if (hspace >= 0)
			r = r.setAnnotation("hs", vf.integer(hspace));
		return r;
	}

	static IValue V(IValue... t) {
		return V(-1, t);
	}

	static IValue V(boolean indent, IValue... t) {
		return V(-1, indent, t);
	}

	static IValue V(int vspace, IValue... t) {
		return V(vspace, false, t);
	}

	static IList makeIndent(IList a, boolean first) {
		IList q = BoxADT.getEmptyList();
		for (IValue b : a) {
			q = q.append(first && q.isEmpty() ? b : I(b));
		}
		
		return q;
	}

	static IValue V(int vspace, boolean indent, IValue... t) {
		IList q = BoxADT.getEmptyList();
		for (IValue a : t) {
			if (a == null)
				continue;
			if (a.getType().isListType()) {

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
			r = r.setAnnotation("vs", vf.integer(vspace));
		return r;
	}

	static IValue I(IValue... t) {
		return BoxADT.TAG.I.create(t);
	}

	static IValue HV(IValue... t) {
		return HV(-1, t);
	}

	static IValue HV(int hspace, IValue... t) {
		return HV(hspace, false , t);
		
	}

	static IValue HOV(IValue... t) {
		return HOV(-1, t);
	}

	static IValue HOV(boolean indent, IValue... t) {
		return HOV(-1, indent, t);
	}

	static IValue HOV(int hspace, IValue... t) {
		return HOV(hspace, false, t);
	}

	static IValue HOV(int hspace, boolean indent, IValue... t) {
		IList q = BoxADT.getEmptyList();
		for (IValue a : t) {
			if (a == null)
				continue;
			if (a.getType().isListType()) {
				q = q.concat(indent ? makeIndent((IList) a, q.isEmpty())
						: (IList) a);
			} else
				q = q.append(indent && !q.isEmpty() ? I(a) : a);
		}
		IConstructor r = BoxADT.TAG.HOV.create(q);
		if (hspace >= 0)
			r = r.setAnnotation("hs", vf.integer(hspace));
		r = r.setAnnotation("vs", vf.integer(0));
		return r;
	}

	static IValue HV(int hspace, boolean indent, IValue... t) {
		IList q = BoxADT.getEmptyList();
		for (IValue a : t) {
			if (a == null)
				continue;
			if (a.getType().isListType()) {
				q = q.concat(indent ? makeIndent((IList) a, q.isEmpty())
						: (IList) a);
			} else
				q = q.append(indent && !q.isEmpty() ? I(a) : a);
		}
		IConstructor r = BoxADT.TAG.HV.create(q);
		if (hspace >= 0)
			r = r.setAnnotation("hs", vf.integer(hspace));
		r = r.setAnnotation("vs", vf.integer(0));
		return r;
	}

}
