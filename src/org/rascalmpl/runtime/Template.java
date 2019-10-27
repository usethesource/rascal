package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.util.ArrayList;

import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class Template {
	final ArrayList<IString> templates = new ArrayList<IString>();
	final IValueFactory VF;

	public Template(final IValueFactory vf, final String s){
		VF = vf;
		templates.add(vf.string(s));
	}

	public final void addVal(final IValue v) {
	    if(v instanceof IString) {
	        addStr(((IString)v).getValue());	
	    } else {
	        templates.set(0, templates.get(0).concat(VF.string(v.toString())));
	    }
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

