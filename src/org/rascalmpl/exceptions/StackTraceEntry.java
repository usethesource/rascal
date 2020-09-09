/*******************************************************************************
 * Copyright (c) 2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Anya Helene Bagge - UiB
 *******************************************************************************/
package org.rascalmpl.exceptions;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.io.StandardTextWriter;

class StackTraceEntry {
	private final ISourceLocation loc;
	private final String name;

	StackTraceEntry(ISourceLocation loc, String funName) {
		this.loc = loc;
		this.name = funName;
	}

	/**
	 * Produce a formatted string with this stack entry
	 * 
	 * @param b a writer to which the string should be added
	 * @param prettyPrinter how to print source locations
	 * @throws IOException 
	 */
    public void format(Writer b, StandardTextWriter prettyPrinter) throws IOException {
		b.append("\tat ");
		if (loc != null) {
			if(name != null) {
				b.append(name);
			}
			else {
				b.append("*** somewhere ***");
			}
			b.append("(");
			prettyPrinter.write(loc, b);
			b.append(")");
		}
		else if (name != null) {
			b.append(name);
			b.append("(");
			b.append("*** somewhere ***");
			b.append(")");
		}
		else {
			b.append("*** unknown ***");
		}
		b.append("\n");
	}


	@Override
	public String toString() {
	    try (StringWriter b = new StringWriter(128)) {
	        format(b, new StandardTextWriter(false));
	        return b.toString();
	    }
	    catch (IOException e) {
	        return e.toString();
	    }
	}

	public ISourceLocation getLocation() {
		return loc;
	}

	public String getScopeName() {
		return name;
	}

}