/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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
package org.rascalmpl.core.utils;

import java.util.LinkedList;
import java.util.List;

import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.semantics.dynamic.QualifiedName.Default;
import io.usethesource.vallang.ISourceLocation;


public class Names {
	
	static public Name lastName(QualifiedName qname) {
		List<Name> names = qname.getNames();
		return names.get(names.size() - 1);
	}
	
	static public boolean isQualified(QualifiedName name) {
		return ((Default) name).isQualified();
	}

	static public String fullName(QualifiedName qname) {
		return ((Default) qname).fullName();
	}

	/**
	 * Get the module name part of a qualified name
	 * @return a string containing all but the last part of the given qualified name
	 */
	static public String moduleName(QualifiedName qname) {
		return ((Default) qname).moduleName();
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
	
	static public Name toName(String name, ISourceLocation loc) {
		return ASTBuilder.make("Name", "Lexical", loc, name);
	}
	
	static public QualifiedName toQualifiedName(String name, ISourceLocation loc) {
		List<Name> list = new LinkedList<Name>();
		list.add(toName(name, loc));
		return ASTBuilder.make("QualifiedName", loc, list);
	}

	static public QualifiedName toQualifiedName(String returnType, String name, ISourceLocation loc) {
    List<Name> list = new LinkedList<Name>();
    list.add(toName(returnType, loc));
    list.add(toName(name, loc));
    return ASTBuilder.make("QualifiedName", loc, list);
  }
}
