package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.util.ArrayList;

import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class Template {
	ArrayList<IString> templates = new ArrayList<IString>();
	IValueFactory VF;

	Template(IValueFactory vf, String s){
		VF = vf;
		templates.add(vf.string(s));
	}

	public void addVal(IValue v) {
		templates.set(0, templates.get(0).concat(VF.string(v.toString())));
	}

	public void addStr(String s) {
		templates.set(0, templates.get(0).concat(VF.string(s)));
	}

	public IString close() {
		if(templates.size() != 1) throw new RuntimeException("Incorrect template stack");
		return templates.get(0);
	}

	@SuppressWarnings("unused")
	public void beginIndent(String whitespace) {
		templates.add(0, VF.string(""));
	}

	public void endIndent(String whitespace) {
		if(templates.isEmpty()) throw new RuntimeException("Empty template stack");   
		IString s = templates.remove(0);
		s = s.indent(VF.string(whitespace), false);
		templates.set(0, templates.get(0).concat(s));
	}
}

