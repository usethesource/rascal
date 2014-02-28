/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import java.util.Iterator;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.Name;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.Names;

public abstract class QualifiedName extends org.rascalmpl.ast.QualifiedName {

	static public class Default extends org.rascalmpl.ast.QualifiedName.Default {
		private static final TypeFactory TF = TypeFactory.getInstance();
		private final String lastName;
		private String fullName;
		private String moduleName;


		public Default(IConstructor __param1, List<Name> __param2) {
			super(__param1, __param2);
			lastName = ((Name.Lexical) __param2.get(__param2.size() - 1)).getString();
		}

		public boolean isQualified() {
			return getNames().size() > 1;
		}
		
		public String lastName() {
			return lastName;
		}
		
		public String moduleName() {
			if (moduleName == null) {
				List<Name> names = getNames();
				java.util.List<Name> prefix = names.subList(0, names.size() - 1);

				if (prefix.size() == 0) {
					return null;
				}

				StringBuilder tmp = new StringBuilder(names.size() * 20);
				Iterator<Name> iter = prefix.iterator();

				while (iter.hasNext()) {
					tmp.append(((Name.Lexical) iter.next()).getString());
					if (iter.hasNext()) {
						tmp.append("::");
					}
				}
			
				moduleName = tmp.toString();
			}
			return moduleName;
		}
		public String fullName() {
			if (fullName == null) {
				List<Name> names = getNames();
				java.util.List<Name> prefix = names.subList(0, names.size() - 1);

				if (prefix.size() == 0) {
					return ((Name.Lexical) names.get(0)).getString();
				}
				
				StringBuilder tmp = new StringBuilder(names.size() * 20);
				Iterator<Name> iter = prefix.iterator();

				while (iter.hasNext()) {
					String part = Names.name(iter.next());
          tmp.append(part);
					if (iter.hasNext()) {
						tmp.append("::");
					}
				}
				
				tmp.append("::");
				tmp.append(((Name.Lexical) names.get(names.size() - 1)).getString());
				fullName = tmp.toString();
			}
			return fullName;
		}
		
		@Override
		public Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			if (getNames().size() == 1
					&& Names.name(getNames().get(0)).equals("_")) {
				return TF.valueType();
			} else {
				Result<IValue> varRes = env.getVariable(this);
				if (varRes == null || varRes.getType() == null) {
					return TF.valueType();
				} else {
					return varRes.getType();
				}
			}
		}
	}

	public QualifiedName(IConstructor __param1) {
		super(__param1);
	}
}
