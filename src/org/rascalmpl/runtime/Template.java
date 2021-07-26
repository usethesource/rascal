package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.util.ArrayList;

import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.SymbolAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class Template {
	final ArrayList<IString> templates = new ArrayList<IString>(); // TODO: replace by StringBuilders
	final IValueFactory VF;

	public Template(final IValueFactory vf, final String s){
		VF = vf;
		templates.add(vf.string(s));
	}

	public final void addVal(IValue v) {
		StringBuilder b = new StringBuilder();
		if (v.getType().isSubtypeOf(RascalValueFactory.Tree)) {
		    // TODO: could this be replaced by a lazy IString::ITree.asString?
			b.append(org.rascalmpl.values.parsetrees.TreeAdapter.yield((IConstructor) v));
		}
		else if (v.getType().isSubtypeOf(RascalValueFactory.Type)) {
			b.append(SymbolAdapter.toString((IConstructor) ((IConstructor) v).get("symbol"), false));
		}
//		else if (v.getType().isString()) {
//			b.append((IString) v);
//		} 
		else {
			b.append(v.toString());
		}
		templates.set(0, templates.get(0).concat(VF.string(b.toString())));
	}

	public final void addStr(final String s) {
		templates.set(0, templates.get(0).concat(VF.string(s)));
	}

	public final IString close() {
		if(templates.size() != 1) throw new RuntimeException("Incorrect template stack");
		return templates.get(0);
	}

	@SuppressWarnings("unused")
	public final void beginIndent(final String whitespace) {
		templates.add(0, VF.string(""));
	}

	public final void endIndent(final String whitespace) {
		if(templates.isEmpty()) throw new RuntimeException("Empty template stack");   
		IString s = templates.remove(0);
		s = s.indent(VF.string(whitespace), false);
		templates.set(0, templates.get(0).concat(s));
	}
}

