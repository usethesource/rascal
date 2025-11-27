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

import org.rascalmpl.ast.Name;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.Names;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public abstract class QualifiedName extends org.rascalmpl.ast.QualifiedName {

	static public class Default extends org.rascalmpl.ast.QualifiedName.Default {
		private static final TypeFactory TF = TypeFactory.getInstance();
		private final String lastName;
		private String fullName;
		private String moduleName;


		public Default(ISourceLocation __param1, IConstructor tree, List<Name> __param2) {
			super(__param1, tree, __param2);
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
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			if (getNames().size() == 1
					&& Names.name(getNames().get(0)).equals("_")) {
				return TF.valueType();
			} else {
				Result<IValue> varRes = env.getVariable(this);
				if (varRes == null || varRes.getStaticType() == null) {
					return TF.valueType();
				} else {
					return varRes.getStaticType();
				}
			}
		}
	}

	public QualifiedName(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
