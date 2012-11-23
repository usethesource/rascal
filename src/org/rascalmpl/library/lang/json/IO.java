/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Bert Lisser    - Bert.Lisser@cwi.nl
 *******************************************************************************/
package org.rascalmpl.library.lang.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.util.JSonReader;
import org.rascalmpl.library.util.JSonWriter;
import org.rascalmpl.unicode.UnicodeInputStreamReader;

public class IO {
	private final IValueFactory values;

	public IO(IValueFactory values) {
		super();

		this.values = values;
	}

	public void writeTextJSonFile(ISourceLocation loc, IValue value,
			IEvaluatorContext ctx) {
		OutputStream out = null;
		try {
			out = ctx.getResolverRegistry()
					.getOutputStream(loc.getURI(), false);
			new JSonWriter().write(value, new OutputStreamWriter(out, "UTF8"));
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()),
					null, null);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException ioex) {
					throw RuntimeExceptionFactory.io(
							values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}

	public IValue readTextJSonFile(IValue type, ISourceLocation loc,
			IEvaluatorContext ctx) {
		// TypeStore store = new TypeStore();
		TypeStore store = ctx.getCurrentEnvt().getStore();
		Type start = new TypeReifier(ctx.getValueFactory()).valueToType(
				(IConstructor) type, store);
		InputStream in = null;
		Reader read = null;
		try {
			in = ctx.getResolverRegistry().getInputStream(loc.getURI());
			read = new UnicodeInputStreamReader(in, ctx.getResolverRegistry().getCharset(loc.getURI()));
			return new JSonReader().read(values, store, start, read);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()),
					null, null);
		} finally {
			if (in != null) {
				try {
					in.close();
					if (read != null) {
						read.close();
					}
				} catch (IOException ioex) {
					throw RuntimeExceptionFactory.io(
							values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}
}
