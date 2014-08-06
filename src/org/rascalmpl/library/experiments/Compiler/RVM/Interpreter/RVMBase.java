package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

public class RVMBase {

	public int sp;
	public Object[] stack;
	public IValueFactory vf;

	public IBool Rascal_TRUE;
	public IBool Rascal_FALSE;

	private static HashSet<IValue> emptyMset = new HashSet<IValue>(0);

	public RVMBase() {
		//Rascal_TRUE = vf.bool(true);
		//Rascal_FALSE = vf.bool(false);
	}

	// / Direct MUPRIMITIVES

	public void exnsnRINT() {
		stack[sp - 1] = vf.integer((Integer) stack[sp - 1]);
	};

	public void exnsnMINT() {
		stack[sp - 1] = ((IInteger) stack[sp - 1]).intValue();
	};

	public void exnsnEQUAL() {
		sp--;
		if (stack[sp - 1] instanceof IValue && (stack[sp] instanceof IValue)) {
			stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).isEqual(((IValue) stack[sp])));
		} else if (stack[sp - 1] instanceof Type && (stack[sp] instanceof Type)) {
			stack[sp - 1] = vf.bool(((Type) stack[sp - 1]) == ((Type) stack[sp]));
		} else
			throw new CompilerError("MuPrimitive equal -- not defined on " + stack[sp - 2].getClass() + " and " + stack[sp - 2].getClass());
	}

	public void exnsnGREATER_MINT_MINT() {
		sp--;
		stack[sp - 1] = vf.bool( ((Integer) stack[sp - 1]) > ((Integer) stack[sp]) );
	}

	public void exnsnEQUAL_MINT_MINT() {
		sp--;
		stack[sp - 1] = vf.bool(((Integer) stack[sp - 1]) == ((Integer) stack[sp]));
	}

	public void exnsnADDITION_MINT_MINT() {
		sp--;
		stack[sp - 1] = ((Integer) stack[sp - 1]) + ((Integer) stack[sp]);

	}

	public void exnsnAND_MBOOL_MBOOL() {
		// assert arity == 2;
		stack[sp - 2] = ((IBool) stack[sp - 2]).and((IBool) stack[sp - 1]);
		sp--;

	}

	public void exnsnASSIGN_PAIR() {
		// assert arity == 3;
		int v1 = ((Integer) stack[sp - 3]);
		int v2 = ((Integer) stack[sp - 2]);
		Object[] pair = (Object[]) stack[sp - 1];
		stack[v1] = pair[0];
		stack[v2] = pair[1];
		stack[sp - 3] = pair;
		sp -= 2; // TODO:???

	}

	public void exnsnASSIGN_SUBSCRIPT_ARRAY_MINT() {
		// assert arity == 3;
		Object[] ar = (Object[]) stack[sp - 3];
		Integer index = ((Integer) stack[sp - 2]);
		ar[index] = stack[sp - 1];
		stack[sp - 3] = stack[sp - 1];
		sp -= 2;
	}

	public void exnsnCHECK_ARG_TYPE() {
		// assert arity == 2;
		Type argType = ((IValue) stack[sp - 2]).getType();
		Type paramType = ((Type) stack[sp - 1]);
		stack[sp - 2] = vf.bool(argType.isSubtypeOf(paramType));
		sp--;
	}

	public void exnsnDIVISION_MINT_MINT() {
		// assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) / ((Integer) stack[sp - 1]);
		sp--;
	}

	public void exnsnEQUAL_SET_MSET() {
		// assert arity == 2;
		ISet set = (ISet) stack[sp - 2];
		HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
		stack[sp - 2] = Rascal_FALSE;
		if (set.size() != mset.size()) {
			sp--;
			return;
		}
		for (IValue v : set) {
			if (!mset.contains(v)) {
				sp--;
				return;
			}
		}
		stack[sp - 2] = Rascal_TRUE;
		sp--;

	}

	public void exnsneQUIVALENT_MBOOL_MBOOL() {
		// assert arity == 2;
		stack[sp - 2] = ((IBool) stack[sp - 2]).equivalent((IBool) stack[sp - 1]);
		sp--;

	}

	public void exnsnGET_CHILDREN() {
		// assert arity == 1;
		INode nd = (INode) stack[sp - 1];
		Object[] elems = new Object[nd.arity()];
		for (int i = 0; i < nd.arity(); i++) {
			elems[i] = nd.get(i);
		}
		stack[sp - 1] = elems;

	}

	public void exnsnGET_CHILDREN_AND_KEYWORD_PARAMS_AS_VALUES() {
		// assert arity == 1;
		IValue v = (IValue) stack[sp - 1];
		Object[] elems;
		if (v.getType().isAbstractData()) {
			IConstructor cons = (IConstructor) v;
			Type tp = cons.getConstructorType();

			int cons_arity = tp.getArity();
			int pos_arity = tp.getPositionalArity();

			elems = new Object[cons_arity];
			for (int i = 0; i < pos_arity; i++) {
				elems[i] = cons.get(i);
			}

			for (int i = pos_arity; i < cons_arity; i++) {
				String key = tp.getFieldName(i);
				IValue val = cons.get(key);
				elems[i] = val;
			}
			stack[sp - 1] = elems;
			return;
		}

		INode nd = (INode) stack[sp - 1];
		int nd_arity = nd.arity();
		if (nd_arity > 0) {
			IValue last = nd.get(nd_arity - 1);

			if (last.getType().isMap()) {
				IMap kwmap = (IMap) last;
				int kw_arity = kwmap.size();
				elems = new Object[nd_arity - 1 + kw_arity];
				int j = nd_arity - 1;
				for (int i = 0; i < nd_arity - 1; i++) {
					elems[i] = nd.get(i);
				}
				for (IValue elm : kwmap) {
					elems[j++] = kwmap.get(elm);
				}
			} else {
				elems = new Object[nd_arity];
				for (int i = 0; i < nd_arity; i++) {
					elems[i] = nd.get(i);
				}
			}
		} else {
			elems = new Object[0];
		}
		stack[sp - 1] = elems;
		return;

	}

	public void exnsnGET_CHILDREN_AND_KEYWORD_PARAMS_AS_MAP() {
		// assert arity == 1;
		IValue v = (IValue) stack[sp - 1];
		if (v.getType().isAbstractData()) {
			IConstructor cons = (IConstructor) v;
			Type tp = cons.getConstructorType();

			int cons_arity = tp.getArity();
			int pos_arity = tp.getPositionalArity();
			IMapWriter writer = vf.mapWriter();
			for (int i = pos_arity; i < cons_arity; i++) {
				String key = tp.getFieldName(i);
				IValue val = cons.get(key);
				writer.put(vf.string(key), val);
			}
			Object[] elems = new Object[pos_arity + 1];
			for (int i = 0; i < pos_arity; i++) {
				elems[i] = cons.get(i);
			}
			elems[pos_arity] = writer.done();
			stack[sp - 1] = elems;
			return;
		}
		INode nd = (INode) v;
		String name = nd.getName();
		int nd_arity = nd.arity();
		Object[] elems;
		if (nd_arity > 0) {
			IValue last = nd.get(nd_arity - 1);
			IMap map;

			if (last.getType().isMap()) {
				elems = new Object[nd_arity];
				for (int i = 0; i < nd_arity; i++) {
					elems[i] = nd.get(i);
				}
			} else {
				TypeFactory tf = TypeFactory.getInstance();
				map = vf.map(tf.voidType(), tf.voidType());
				elems = new Object[nd_arity + 1]; // account for keyword map

				for (int i = 0; i < nd_arity; i++) {
					elems[i] = nd.get(i);
				}
				elems[nd_arity] = map;
			}
		} else {
			elems = new Object[1];
			TypeFactory tf = TypeFactory.getInstance();
			elems[0] = vf.map(tf.voidType(), tf.voidType());
		}
		stack[sp - 1] = elems;
		return;

	}

	public void exnsnGET_CHILDREN_WITHOUT_LAYOUT_OR_SEPARATORS() {
		// assert arity == 1;
		IConstructor cons = (IConstructor) stack[sp - 1];
		IConstructor prod = (IConstructor) cons.get(0);
		IList args = (IList) cons.get(1);
		IConstructor symbol = (IConstructor) prod.get(0);

		int step;

		switch (symbol.getName()) {

		case "iter":
		case "iter-star":
			step = 2;
			break;
		case "iter-seps":
		case "iter-seps-star":
			step = 4;
			break;
		default:
			step = 2;
		}

		int len = (args.length() / step) + 1;
		int non_lit_len = 0;

		for (int i = 0; i < len; i += step) {
			if (!$is_literal(args.get(i * step))) {
				non_lit_len++;
			}
		}
		IValue[] elems = new IValue[non_lit_len + 1];
		int j = 0;
		for (int i = 0; i < len; i += step) {
			if (!$is_literal(args.get(i * step))) {
				elems[j++] = args.get(i * step);
			}
		}
		TypeFactory tf = TypeFactory.getInstance();
		elems[non_lit_len] = vf.map(tf.voidType(), tf.voidType());
		stack[sp - 1] = elems;

	}

	public void exnsnGET_NAME() {
		// assert arity == 1;
		INode nd = (INode) stack[sp - 1];
		stack[sp - 1] = vf.string(nd.getName());

	}

	public void exnsnGET_NAME_AND_CHILDREN_AND_KEYWORD_PARAMS_AS_MAP() {
		// assert arity == 1;
		IValue v = (IValue) stack[sp - 1];
		if (v.getType().isAbstractData()) {
			IConstructor cons = (IConstructor) v;
			Type tp = cons.getConstructorType();

			int cons_arity = tp.getArity();
			int pos_arity = tp.getPositionalArity();
			IMapWriter writer = vf.mapWriter();
			for (int i = pos_arity; i < cons_arity; i++) {
				String key = tp.getFieldName(i);
				IValue val = cons.get(key);
				writer.put(vf.string(key), val);
			}
			Object[] elems = new Object[pos_arity + 2];
			elems[0] = vf.string(cons.getName());
			for (int i = 0; i < pos_arity; i++) {
				elems[i + 1] = cons.get(i);
			}
			elems[pos_arity + 1] = writer.done();
			stack[sp - 1] = elems;
			return;
		}
		INode nd = (INode) v;
		String name = nd.getName();
		int nd_arity = nd.arity();
		Object[] elems;
		if (nd_arity > 0) {
			IValue last = nd.get(nd_arity - 1);
			IMap map;

			if (last.getType().isMap()) {
				elems = new Object[nd_arity + 1]; // account for function name
				elems[0] = vf.string(name);
				for (int i = 0; i < nd_arity; i++) {
					elems[i + 1] = nd.get(i);
				}
			} else {
				TypeFactory tf = TypeFactory.getInstance();
				map = vf.map(tf.voidType(), tf.voidType());
				elems = new Object[nd_arity + 2]; // account for function name and keyword map

				elems[0] = vf.string(name);
				for (int i = 0; i < nd_arity; i++) {
					elems[i + 1] = nd.get(i);
				}
				elems[1 + nd_arity] = map;
			}
		} else {
			elems = new Object[2];
			TypeFactory tf = TypeFactory.getInstance();
			elems[0] = vf.string(name);
			elems[1] = vf.map(tf.voidType(), tf.voidType());
		}
		stack[sp - 1] = elems;
	}

	public void exnsnGET_TUPLE_ELEMENTS() {
		// assert arity == 1;
		ITuple tup = (ITuple) stack[sp - 1];
		int nelem = tup.arity();
		Object[] elems = new Object[nelem];
		for (int i = 0; i < nelem; i++) {
			elems[i] = tup.get(i);
		}
		stack[sp - 1] = elems;

	}

	public void exnsnGREATER_EQUAL_MINT_MINT() {
		// assert arity == 2;
		stack[sp - 2] = vf.bool(((Integer) stack[sp - 2]) >= ((Integer) stack[sp - 1]));
		sp--;

	}

	// Has a concrete term a given label?
	public void exnsnHAS_LABEL() {
		// assert arity == 2;
		IValue v = (IValue) stack[sp - 2];
		if (v.getType().isAbstractData()) {
			IConstructor cons = (IConstructor) v;
			if (cons.getName().equals("appl")) {
				IConstructor prod = (IConstructor) cons.get(0);
				IConstructor symbol = (IConstructor) prod.get(0);

				if (symbol.getName().equals("label")) {
					IString label_name = (IString) stack[sp - 1];
					if (((IString) symbol.get(0)).equals(label_name)) {
						stack[sp - 2] = Rascal_TRUE;
						sp--;
						return;
					}
				}

			}
		}
		stack[sp - 2] = Rascal_FALSE;
		sp--;
	}

	public void exnsnIMPLIES_MBOOL_MBOOL() {
		// assert arity == 2;
		stack[sp - 2] = ((IBool) stack[sp - 2]).implies((IBool) stack[sp - 1]);
		sp--;

	}

	public void exnsnIS_DEFINED() {
		// assert arity == 1;
		Reference ref = (Reference) stack[sp - 1];
		stack[sp - 1] = vf.bool(ref.isDefined());
	}

	public void exnsnIS_ELEMENT_MSET() {
		// assert arity == 2;
		stack[sp - 2] = vf.bool(((HashSet<IValue>) stack[sp - 1]).contains((IValue) stack[sp - 2]));
		sp--;
	}

	public void exnsnIS_BOOL() {
		// assert arity == 1;
		stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isBool());
	}

	public void exnsnIS_CONSTRUCTOR() {
		// assert arity == 1;
		stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isAbstractData());
	}

	public void exnsnIS_DATETIME() {
		// assert arity == 1;
		stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isDateTime());
	}

	public void exnsnIS_INT() {
		// assert arity == 1;
		stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isInteger());
	}

	public void exnsnIS_LIST() {
		// assert arity == 1;
		stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isList());
	}

	public void exnsnIS_LREL() {
		// assert arity == 1;
		stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isListRelation());
	}

	public void exnsnIS_LOC() {
		// assert arity == 1;
		stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isSourceLocation());
	}

	public void exnsnIS_MAP() {
		// assert arity == 1;
		stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isMap());
	}

	public void exnsnIS_NODE() {
		// assert arity == 1;
		stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isNode());
	}

	public void exnsnIS_NUM() {
		// assert arity == 1;
		stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isNumber());
	}

	public void exnsnIS_REAL() {
		// assert arity == 1;
		stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isReal());
	}

	public void exnsnIS_RAT() {
		// assert arity == 1;
		stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isRational());
	}

	public void exnsnIS_REL() {
		// assert arity == 1;
		stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isRelation());
	}

	public void exnsnIS_SET() {
		// assert arity == 1;
		stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isSet());
	}

	public void exnsnIS_STR() {
		// assert arity == 1;
		stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isString());
	}

	public void exnsnIS_TUPLE() {
		// assert arity == 1;
		stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isTuple());
	}

	public void exnsnKEYS_MAP() {
		// assert arity == 1;
		IMap map = ((IMap) stack[sp - 1]);
		IListWriter writer = vf.listWriter();
		for (IValue key : map) {
			writer.append(key);
		}
		stack[sp - 1] = writer.done();
	}

	public void exnsnVALUES_MAP() {
		// assert arity == 1;
		IMap map = ((IMap) stack[sp - 1]);
		IListWriter writer = vf.listWriter();
		for (IValue key : map) {
			writer.append(map.get(key));
		}
		stack[sp - 1] = writer.done();
	}

	public void exnsnLESS_EQUAL_MINT_MINT() {
		// assert arity == 2;
		sp-- ;
		stack[sp - 1] = vf.bool(((Integer) stack[sp - 1]) <= ((Integer) stack[sp]));
	}

	public void exnsnLESS_MINT_MINT() {
		// assert arity == 2;
		sp--;
		stack[sp - 1] = vf.bool(((Integer) stack[sp - 1]) < ((Integer) stack[sp]));
	}

	public void exnsnMAKE_IARRAY(int arity) {
		assert arity >= 0;

		IValue[] ar = new IValue[arity];
		for (int i = arity - 1; i >= 0; i--) {
			ar[i] = (IValue) stack[sp - arity + i];
		}
		sp = sp - arity + 1;
		stack[sp - 1] = ar;
	}

	public void exnsnMAKE_IARRAY_OF_SIZE() {
		// assert arity == 1;
		int len = ((Integer) stack[sp - 1]);
		stack[sp - 1] = new IValue[len];
	}

	public void exnsnMAKE_ARRAY(int arity) {
		 assert arity >= 0;

		Object[] ar = new Object[arity];

		for (int i = arity - 1; i >= 0; i--) {
			ar[i] = stack[sp - arity + i];
		}
		sp = sp - arity + 1;
		stack[sp - 1] = ar;
	}

	public void exnsnMAKE_ARRAY_OF_SIZE() {
		// assert arity == 1;
		int len = ((Integer) stack[sp - 1]);
		stack[sp - 1] = new Object[len];
	}

	public void exnsnMAKE_MSET() {
		// assert arity == 0;
		HashSet<IValue> mset = new HashSet<IValue>();
		stack[sp++] = mset;
	}

	public void exnsnMAKE_MAP_STR_ENTRY() {
		// assert arity == 0;
		stack[sp++] = new HashMap<String, Map.Entry<Type, IValue>>();
	}

	public void exnsnMAKE_MAP_STR_IVALUE() {
		// assert arity == 0;
		stack[sp++] = new HashMap<String, IValue>();
	}

	public void exnsnMAKE_ENTRY_TYPE_IVALUE() {
		// assert arity == 2;
		stack[sp - 2] = new AbstractMap.SimpleEntry<Type, IValue>((Type) stack[sp - 2], (IValue) stack[sp - 1]);
		sp--;
	} // kwp

	public void exnsnMAP_CONTAINS_KEY() {
		// assert arity == 2;
		IMap m = ((IMap) stack[sp - 2]);
		IString key = ((IString) stack[sp - 1]);
		stack[sp - 2] = vf.bool(m.containsKey(key));
		sp--;
	}

	public void exnsnMIN_MINT_MINT() {
		// assert arity == 2;
		Integer x = ((Integer) stack[sp - 2]);
		Integer y = ((Integer) stack[sp - 1]);
		stack[sp - 2] = x < y ? x : y;
		sp--;
	}

	public void exnsnMAX_MINT_MINT() {
		// assert arity == 2;
		Integer x = ((Integer) stack[sp - 2]);
		Integer y = ((Integer) stack[sp - 1]);
		stack[sp - 2] = x > y ? x : y;
		sp--;
	}

	public void exnsnMODULO_MINT_MINT() {
		// assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) % ((Integer) stack[sp - 1]);
		sp--;
	}

	public void exnsnMSET() {
		// assert arity == 1;
		ISet set = ((ISet) stack[sp - 1]);
		int n = set.size();
		HashSet<IValue> mset = n > 0 ? new HashSet<IValue>(set.size()) : emptyMset;
		for (IValue v : set) {
			mset.add(v);
		}
		stack[sp - 1] = mset;
	}

	public void exnsnMSET_EMPTY() {
		// assert arity == 0;
		stack[sp++] = emptyMset;
	}

	public void exnsnMSET2LIST() {
		// assert arity == 1;
		HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
		IListWriter writer = vf.listWriter();
		for (IValue elem : mset) {
			writer.append(elem);
		}
		stack[sp - 1] = writer.done();
	}

	public void exnsnMSET_DESTRUCTIVE_ADD_ELM() {
		// assert arity == 2;
		HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 2];
		if (mset == emptyMset) {
			mset = (HashSet<IValue>) emptyMset.clone();
		}
		IValue elm = ((IValue) stack[sp - 1]);
		mset.add(elm);
		stack[sp - 2] = mset;
		sp--;
	}

	public void exnsnMSET_DESTRUCTIVE_ADD_MSET() {
		// assert arity == 2;
		HashSet<IValue> lhs = (HashSet<IValue>) stack[sp - 2];
		if (lhs == emptyMset) {
			lhs = (HashSet<IValue>) emptyMset.clone();
		}
		// lhs = (HashSet<IValue>) lhs.clone();
		HashSet<IValue> rhs = (HashSet<IValue>) stack[sp - 1];
		lhs.addAll(rhs);
		stack[sp - 2] = lhs;
		sp--;
	}

	public void exnsNMAP_STR_ENTRY_ADD_ENTRY_TYPE_IVALUE() {
		// assert arity == 3;
		stack[sp - 3] = ((Map<String, Map.Entry<Type, IValue>>) stack[sp - 3]).put(((IString) stack[sp - 2]).getValue(), (Map.Entry<Type, IValue>) stack[sp - 1]);
		sp -= 2;
	} // kwp

	public void exnsNMAP_STR_IVALUE_ADD_IVALUE() {
		// assert arity == 3;
		stack[sp - 3] = ((Map<String, IValue>) stack[sp - 3]).put(((IString) stack[sp - 2]).getValue(), (IValue) stack[sp - 1]);
		sp -= 2;
	} // kwp

	public void exnsnMULTIPLICATION_MINT_MINT() {
		// assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) * ((Integer) stack[sp - 1]);
		sp--;
	}

	public void exnsnMSET_DESTRUCTIVE_SUBTRACT_MSET() {
		// assert arity == 2;
		HashSet<IValue> lhs = (HashSet<IValue>) stack[sp - 2];
		// lhs = (HashSet<IValue>) lhs.clone();
		HashSet<IValue> rhs = (HashSet<IValue>) stack[sp - 1];

		lhs.removeAll(rhs);
		stack[sp - 2] = lhs;
		sp--;
	}

	public void exnsNMSET_SUBTRACT_MSET() {
		// assert arity == 2;
		HashSet<IValue> lhs = (HashSet<IValue>) stack[sp - 2];
		lhs = (HashSet<IValue>) lhs.clone();
		HashSet<IValue> rhs = (HashSet<IValue>) stack[sp - 1];
		lhs.removeAll(rhs);
		stack[sp - 2] = lhs;
		sp--;
	}

	public void exnsnMSET_DESTRUCTIVE_SUBTRACT_SET() {
		// assert arity == 2;
		HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 2];
		mset = (HashSet<IValue>) mset.clone();
		ISet set = ((ISet) stack[sp - 1]);
		for (IValue v : set) {
			mset.remove(v);
		}
		stack[sp - 2] = mset;
		sp--;
	}

	public void exnsNMSET_SUBTRACT_SET() {
		// assert arity == 2;
		HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 2];
		mset = (HashSet<IValue>) mset.clone();
		ISet set = ((ISet) stack[sp - 1]);
		for (IValue v : set) {
			mset.remove(v);
		}
		stack[sp - 2] = mset;
		sp--;
	}

	public void exnsNMSET_DESTRUCTIVE_SUBTRACT_ELM() {
		// assert arity == 2;
		HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 2];
		// mset = (HashSet<IValue>) mset.clone();
		IValue elm = ((IValue) stack[sp - 1]);
		mset.remove(elm);
		stack[sp - 2] = mset;
		sp--;
	}

	public void exnsNMSET_SUBTRACT_ELM() {
		// assert arity == 2;
		HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 2];
		mset = (HashSet<IValue>) mset.clone();
		IValue elm = ((IValue) stack[sp - 1]);
		mset.remove(elm);
		stack[sp - 2] = mset;
		sp--;
	}

	public void exnsnNOT_EQUAL_MINT_MINT() {
		// assert arity == 2;
		stack[sp - 2] = vf.bool(((Integer) stack[sp - 2]) != ((Integer) stack[sp - 1]));
		sp--;
	}

	public void exnsnNOT_MBOOL() {
		// assert arity == 1;
		stack[sp - 1] = ((IBool) stack[sp - 1]).not();
	}

	public void exnsnOCCURS_LIST_LIST_MINT() {
		// assert arity == 3;
		IList sublist = ((IList) stack[sp - 3]);
		int nsub = sublist.length();
		IList list = ((IList) stack[sp - 2]);
		Integer start = (Integer) stack[sp - 1];
		int nlist = list.length();
		stack[sp - 3] = Rascal_FALSE;
		int newsp = sp - 2;
		if (start + nsub <= nlist) {
			for (int i = 0; i < nsub; i++) {
				if (!sublist.get(i).isEqual(list.get(start + i))) {
					sp = newsp;
					return;
				}
			}
		} else {
			sp = newsp ;
			return;
		}

		stack[sp - 3] = Rascal_TRUE;
		sp = newsp;
	}

	public void exnsNOR_MBOOL_MBOOL() {
		// assert arity == 2;
		stack[sp - 2] = ((IBool) stack[sp - 2]).or((IBool) stack[sp - 1]);
		sp--;
	}

	public void exnsnPOWER_MINT_MINT() {
		// assert arity == 2;
		int n1 = ((Integer) stack[sp - 2]);
		int n2 = ((Integer) stack[sp - 1]);
		int pow = 1;
		for (int i = 0; i < n2; i++) {
			pow *= n1;
		}
		stack[sp - 2] = pow;
		sp--;
	}

	public void exnsnRBOOL() {
		// assert arity == 1;
	}

	public void exnsnregexp_compile() {
		// assert arity == 2;
		String RegExpAsString = ((IString) stack[sp - 2]).getValue();
		String subject = ((IString) stack[sp - 1]).getValue();
		try {
			Pattern pat = Pattern.compile(RegExpAsString);
			stack[sp - 2] = pat.matcher(subject);
			sp--;
			return;
		} catch (PatternSyntaxException e) {
			// throw new CompilerError("Syntax error in regular expression: " + RegExpAsString);
			// TODO: change to something like:
			throw RascalRuntimeException.RegExpSyntaxError(RegExpAsString, null);
		}
	}

	public void exnsnREGEXP_FIND() {
		// assert arity == 1;
		Matcher matcher = (Matcher) stack[sp - 1];
		stack[sp - 1] = vf.bool(matcher.find());
	}

	public void exnsnREGEXP_GROUP() {
		// assert arity == 2;
		Matcher matcher = (Matcher) stack[sp - 2];
		int idx = (Integer) stack[sp - 1];
		stack[sp - 2] = vf.string(matcher.group(idx));
		sp--;
	}

	public void exnsNSET() {
		// assert arity == 1;
		HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
		ISetWriter w = vf.setWriter();
		for (IValue v : mset) {
			w.insert(v);
		}
		stack[sp - 1] = w.done();
	}

	public void exnsnSET2LIST() {
		// assert arity == 1;
		ISet set = (ISet) stack[sp - 1];
		IListWriter writer = vf.listWriter();
		for (IValue elem : set) {
			writer.append(elem);
		}
		stack[sp - 1] = writer.done();
	}

	public void exnsnSET_IS_SUBSET_OF_MSET() {
		// assert arity == 2;
		ISet subset = ((ISet) stack[sp - 2]);
		HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
		for (IValue v : subset) {
			if (!mset.contains(v)) {
				stack[sp - 2] = Rascal_FALSE;
				sp--;
				return;
			}
		}
		stack[sp - 2] = Rascal_TRUE;
		sp--;
	}

	public void exnsnSIZE_ARRAY() {
		// assert arity == 1;
		stack[sp - 1] = ((Object[]) stack[sp - 1]).length;
	}

	public void exnsnSIZE_LIST() {
		// assert arity == 1;
		stack[sp - 1] = ((IList) stack[sp - 1]).length();
	}

	public void exnsnSIZE_SET() {
		// assert arity == 1;
		stack[sp - 1] = ((ISet) stack[sp - 1]).size();
	}

	public void exnsnSIZE_MSET() {
		// assert arity == 1;
		stack[sp - 1] = ((HashSet<?>) stack[sp - 1]).size();
	}

	public void exnsnSIZE_MAP() {
		// assert arity == 1;
		stack[sp - 1] = ((IMap) stack[sp - 1]).size();
	}

	public void exnsnSIZE_TUPLE() {
		// assert arity == 1;
		stack[sp - 1] = ((ITuple) stack[sp - 1]).arity();
	}

	public void exnsnSIZE() {
		// assert arity == 1;
		if (stack[sp - 1] instanceof IConstructor) {
			stack[sp - 1] = ((IConstructor) stack[sp - 1]).arity();
		} else if (stack[sp - 1] instanceof INode) {
			stack[sp - 1] = ((INode) stack[sp - 1]).arity();
		} else if (stack[sp - 1] instanceof IList) {
			stack[sp - 1] = ((IList) stack[sp - 1]).length();
		} else if (stack[sp - 1] instanceof ISet) {
			stack[sp - 1] = ((ISet) stack[sp - 1]).size();
		} else if (stack[sp - 1] instanceof IMap) {
			stack[sp - 1] = ((IMap) stack[sp - 1]).size();
		} else if (stack[sp - 1] instanceof ITuple) {
			stack[sp - 1] = ((ITuple) stack[sp - 1]).arity();
		}
	}

	public void exnsnSTARTS_WITH() {
		// assert arity == 3;
		IList sublist = (IList) stack[sp - 3];
		IList list = (IList) stack[sp - 2];
		int start = (Integer) stack[sp - 1];
		IBool eq = Rascal_TRUE;

		if (start + sublist.length() <= list.length()) {
			for (int i = 0; i < sublist.length() && eq.getValue(); i++) {
				if (!sublist.get(i).equals(list.get(start + i))) {
					eq = Rascal_FALSE;
				}
			}
		}
		stack[sp - 3] = eq;
	}

	public void exnsnSUBLIST_LIST_MINT_MINT() {
		// assert arity == 3;
		IList lst = (IList) stack[sp - 3];
		int offset = ((Integer) stack[sp - 2]);
		int length = ((Integer) stack[sp - 1]);
		stack[sp - 3] = lst.sublist(offset, length);
		sp -= 2;
	}

	public void exnsnSUBSCRIPT_ARRAY_MINT() {
		// assert arity == 2;
		stack[sp - 2] = ((Object[]) stack[sp - 2])[((Integer) stack[sp - 1])];
	}

	public void exnsnSUBSCRIPT_LIST_MINT() {
		// assert arity == 2;
		stack[sp - 2] = ((IList) stack[sp - 2]).get((Integer) stack[sp - 1]);
		sp--;
	}

	public void exnsnSUBSCRIPT_TUPLE_MINT() {
		// assert arity == 2;
		stack[sp - 2] = ((ITuple) stack[sp - 2]).get((Integer) stack[sp - 1]);
		sp--;
	}

	public void exnsnSUBTRACTION_MINT_MINT() {
		// assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) - ((Integer) stack[sp - 1]);
		sp--;
	}

	public void exnsnSUBTYPE() {
		// assert arity == 2;
		stack[sp - 2] = vf.bool(((Type) stack[sp - 2]).isSubtypeOf((Type) stack[sp - 1]));
		sp--;
	}

	public void exnsnTYPEOF() {
		// assert arity == 1;
		if (stack[sp - 1] instanceof Integer) {
			stack[sp - 1] = TypeFactory.getInstance().integerType();
		} else {
			stack[sp - 1] = ((IValue) stack[sp - 1]).getType();
		}
	}

	public void exnsnTYPEOFMSET() {
		// assert arity == 1;
		if (stack[sp - 1] instanceof HashSet) {
			HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
			Type elmType = TypeFactory.getInstance().voidType();
			for (IValue elm : mset) {
				elmType = elm.getType().lub(elmType);
			}
			stack[sp - 1] = TypeFactory.getInstance().setType(elmType);
		}
	}

	public void exnsnUNDEFINE() {
		// assert arity == 1;
		Reference ref = (Reference) stack[sp - 1];
		stack[sp - 1] = ref.getValue();
		ref.undefine();
	}

	public void exnsnPRODUCT_MINT_MINT() {
		// assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) * ((Integer) stack[sp - 1]);
		sp--;
	}

	public void exnsnTYPEoF_CONSTRUCTOR() {
		// assert arity == 1;
		if (stack[sp - 1] instanceof Integer) {
			stack[sp - 1] = TypeFactory.getInstance().integerType();
		} else if (stack[sp - 1] instanceof IConstructor) {
			stack[sp - 1] = ((IConstructor) stack[sp - 1]).getConstructorType();
		} else {
			stack[sp - 1] = ((IValue) stack[sp - 1]).getType();
		}
	}

	private static boolean $is_literal(IValue v) {
		if (v.getType().isAbstractData()) {
			IConstructor appl = (IConstructor) v;
			if (appl.getName().equals("appl")) {
				IConstructor prod = (IConstructor) appl.get(0);
				IConstructor symbol = (IConstructor) prod.get(0);
				return symbol.getName().equals("lit");
			}
		}
		return false;
	}

}
