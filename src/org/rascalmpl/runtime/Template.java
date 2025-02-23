/*
 * Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
		else if (v.getType().isString()) {
			b.append(((IString) v).getValue());
		} 
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

