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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.AmbiguousFunctionReference;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModule;
import org.rascalmpl.interpreter.staticErrors.UndeclaredType;

public abstract class DataTypeSelector extends
		org.rascalmpl.ast.DataTypeSelector {

	static public class Selector extends
			org.rascalmpl.ast.DataTypeSelector.Selector {

		public Selector(IConstructor __param1, QualifiedName __param2, Name __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			Type adt;
			QualifiedName sort = this.getSort();
			String name = org.rascalmpl.interpreter.utils.Names.typeName(sort);

			if (org.rascalmpl.interpreter.utils.Names.isQualified(sort)) {
				GlobalEnvironment heap = env.getHeap();
				ModuleEnvironment mod = heap
						.getModule(org.rascalmpl.interpreter.utils.Names
								.moduleName(sort));

				if (mod == null) {
					throw new UndeclaredModule(
							org.rascalmpl.interpreter.utils.Names
									.moduleName(sort), sort);
				}

				adt = mod.lookupAbstractDataType(name);
			} else {
				adt = env.lookupAbstractDataType(name);
			}

			if (adt == null) {
				throw new UndeclaredType(name, this);
			}

			String constructor = org.rascalmpl.interpreter.utils.Names
					.name(this.getProduction());
			Set<Type> constructors = env.lookupConstructor(adt, constructor);

			if (constructors.size() == 0) {
				throw new UndeclaredType(name + "." + constructor, this);
			} else if (constructors.size() > 1) {
				throw new AmbiguousFunctionReference(name + "."
						+ constructor, this);
			} else {
				return constructors.iterator().next();
			}

		}

	}

	public DataTypeSelector(IConstructor __param1) {
		super(__param1);
	}
}
