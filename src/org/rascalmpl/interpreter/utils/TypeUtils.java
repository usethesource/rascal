/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.utils;

import java.util.List;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.TypeArg;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.PartiallyLabeledFields;
import org.rascalmpl.interpreter.staticErrors.RedeclaredField;

public final class TypeUtils {
	private static TypeFactory TF = TypeFactory.getInstance();
	
	public static Type typeOf(List<TypeArg> args, Environment env, boolean instantiateTypeParameters) {
		Type[] fieldTypes = new Type[args.size()];
		String[] fieldLabels = new String[args.size()];

		int i = 0;
		boolean allLabeled = true;
		boolean someLabeled = false;

		for (TypeArg arg : args) {
			fieldTypes[i] = arg.getType().typeOf(env, instantiateTypeParameters, null);

			if (arg.isNamed()) {
				fieldLabels[i] = Names.name(arg.getName());
				someLabeled = true;
			} else {
				fieldLabels[i] = null;
				allLabeled = false;
			}
			i++;
		}

		if (someLabeled && !allLabeled) {
			// TODO: this ast is not the root of the cause
			throw new PartiallyLabeledFields(args.get(0));
		}

		if (!allLabeled) {
			return TF.tupleType(fieldTypes);
		}
		for(int j = 0; j < fieldLabels.length - 1; j++){
			for(int k = j + 1; k < fieldLabels.length; k++){
				if(fieldLabels[j].equals(fieldLabels[k])){
					throw new RedeclaredField(fieldLabels[j], args.get(k));
				}
			}
		}

		return TF.tupleType(fieldTypes, fieldLabels);
	}

}
