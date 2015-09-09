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
package org.rascalmpl.interpreter;

import org.eclipse.imp.pdb.facts.ISourceLocation;

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
	 * @param b A string builder to which the string should be added
	 * @param withLink true if Rascal console links are desired in the output
	 */
	void format(StringBuilder b, boolean withLink) {
		b.append("\tat ");
		if (loc != null) {
			if(name != null) {
				b.append(name);
			}
			else {
				b.append("*** somewhere ***");
			}
			b.append("(");
			if(withLink) {
				// b.append("\uE007[");
				b.append(loc.toString());
			}
			else {
				b.append(loc.getPath());
				// if(withLink) {
				//	b.append("](");
				//	b.append(uri);
				//	b.append(")");
				// }
				b.append(":");
				if(loc.hasLineColumn()) {
					b.append(loc.getBeginLine());
					b.append(",");
					b.append(loc.getBeginColumn());
				}
			}
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
		StringBuilder b = new StringBuilder(128);
		format(b, false);
		return b.toString();
	}

	public ISourceLocation getLocation() {
		return loc;
	}

	public String getScopeName() {
		return name;
	}
}