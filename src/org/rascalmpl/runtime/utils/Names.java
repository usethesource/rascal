/*
 * Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
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
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.runtime.utils;

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
