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
	static final private Type box, boxlst, str;
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
				boxlst), A(boxlst), L(str), KW(box), VAR(box), NUM(box);
		Type tag;

		TAG(Type tp) {
			tag = tf.constructor(ts, box, this.name(), tp, this.name()
					.toLowerCase());
			ts.declareConstructor(tag);
		}
		IConstructor create(IValue ... t) {
			IValue r = t[0];
			if (DEBUG) System.err.println("create:"+tag+" "+r);
			if (!r.getType().isListType() && this.ordinal() < L.ordinal()) {
				 r =vf.list(t);			
			}		
			return vf.constructor(tag, r);
		}

		IConstructor create(String s) {
			if (DEBUG) System.err.println("create:"+tag+" "+s);
			return vf.constructor(tag, vf.string(s));
		}
	};

	static IValue PLUS = TAG.L.create("+");
	static IValue MINUS = TAG.L.create("-");
	static IValue MULT = TAG.L.create("*");
	static IValue DIVIDE = TAG.L.create("/");
	static IValue MOD = TAG.L.create("/");
	static IValue EQUAL = TAG.L.create("==");
	static IValue NOTEQUAL = TAG.L.create("!=");
	static IValue LT = TAG.L.create("<");
	static IValue LE = TAG.L.create("<=");
	static IValue GT = TAG.L.create(">");
	static IValue GE = TAG.L.create(">=");
	static IValue OR = TAG.L.create("||");
	static IValue AND = TAG.L.create("&&");
	static IValue NOT = TAG.L.create("!");
	static IValue LPAR = TAG.L.create("(");
	static IValue RPAR = TAG.L.create(")");
	static IValue LBRACK = TAG.L.create("[");
	static IValue RBRACK = TAG.L.create("]");
	static IValue ASSIGN = TAG.L.create("=");

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

	static IValue comma() {
		return BoxADT.TAG.L.create(",");
	}

	static IValue semicolumn() {
		return BoxADT.TAG.L.create(";");
	}

	static IList getEmptyList() {
		return vf.list();
	}

	static IList getList(IValue t) {
		return vf.list(t);
	}

	static IValue KW(String s) {
		return BoxADT.TAG.KW.create(BoxADT.TAG.L.create(s));
	}

	static IValue NUM(String s) {
		return BoxADT.TAG.NUM.create(BoxADT.TAG.L.create(s));
	}

	static IValue VAR(String s) {
		return BoxADT.TAG.VAR.create(BoxADT.TAG.L.create(s));
	}

	static IValue L(String s) {
		return BoxADT.TAG.L.create(s);
	}

	static IValue H(IValue ... t) {
		return BoxADT.TAG.H.create(t);
	}
	
	static IValue H(int hspace, IValue ... t) {
		IConstructor r = BoxADT.TAG.H.create(t);
		r= r.setAnnotation("hs", vf.integer(hspace));
		return r;
	}

	static IValue V(IValue ...t) {
		return BoxADT.TAG.V.create(t);
	}
	
	static IValue I(IValue ...t) {
		return BoxADT.TAG.I.create(t);
	}
	
	static IValue HV(IValue ... t) {
		return BoxADT.TAG.HV.create(t);
	}
	
	static IValue HOV(IValue ... t) {
		return BoxADT.TAG.HOV.create(t);
	}

}
