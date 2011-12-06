/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.interpreter.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.parser.ASTBuilder;


public class Names {
	
	static public Name lastName(QualifiedName qname) {
		List<Name> names = qname.getNames();
		return names.get(names.size() - 1);
	}
	
	static public boolean isQualified(QualifiedName name) {
		return name.getNames().size() > 1;
	}

	static public String fullName(QualifiedName qname) {
		List<Name> names = qname.getNames();
		java.util.List<Name> prefix = names.subList(0, names.size() - 1);

		if (prefix.size() == 0) {
			return name(names.get(0));
		}
		
		StringBuilder tmp = new StringBuilder(names.size() * 20);
		Iterator<Name> iter = prefix.iterator();

		while (iter.hasNext()) {
			tmp.append(name(iter.next()));
			if (iter.hasNext()) {
				tmp.append("::");
			}
		}
		
		tmp.append("::");
		tmp.append(name(names.get(names.size() - 1)));
		
		return tmp.toString();
	}

	/**
	 * Get the module name part of a qualified name
	 * @return a string containing all but the last part of the given qualified name
	 */
	static public String moduleName(QualifiedName qname) {
		List<Name> names = qname.getNames();
		java.util.List<Name> prefix = names.subList(0, names.size() - 1);

		if (prefix.size() == 0) {
			return null;
		}
		
		StringBuilder tmp = new StringBuilder(names.size() * 20);
		Iterator<Name> iter = prefix.iterator();

		while (iter.hasNext()) {
			tmp.append(name(iter.next()));
			if (iter.hasNext()) {
				tmp.append("::");
			}
		}
		
		return tmp.toString();
	}
	
	static public String name(Name name) {
		return ((Name.Lexical) name).getString();
	}
	
	static public String consName(QualifiedName qname) {
		return name(lastName(qname));
	}
	
	static public String typeName(QualifiedName qname) {
		return name(lastName(qname));
	}
	
	static public String sortName(QualifiedName qname) {
		List<Name> names = qname.getNames();
		if (names.size() >= 2) {
			return name(names.get(names.size() - 2));
		}
		return null;
	}
	
	static public Name toName(String name) {
		return ASTBuilder.make("Name", "Lexical", null, name);
	}
	
	static public QualifiedName toQualifiedName(String name) {
		List<Name> list = new LinkedList<Name>();
		list.add(toName(name));
		return ASTBuilder.make("QualifiedName", null, list);
	}
}
