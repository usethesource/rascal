package org.rascalmpl.interpreter.utils;

import java.util.List;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.TypeArg;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.PartiallyLabeledFieldsError;
import org.rascalmpl.semantics.dynamic.Statement.Expression;

public final class TypeUtils {
	private static TypeFactory TF = TypeFactory.getInstance();
	
	public static Type typeOf(Expression pattern, Environment env) {
		return pattern.typeOf(env);
	}
	
	public static Type typeOf(org.rascalmpl.ast.Type t, Environment env) {
		return t.typeOf(env);
	}
	
	public static Type typeOf(List<TypeArg> args, Environment env) {
		Type[] fieldTypes = new Type[args.size()];
		String[] fieldLabels = new String[args.size()];

		int i = 0;
		boolean allLabeled = true;
		boolean someLabeled = false;

		for (TypeArg arg : args) {
			fieldTypes[i] = arg.getType().typeOf(env);

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
			throw new PartiallyLabeledFieldsError(args.get(0));
		}

		if (!allLabeled) {
			return TF.tupleType(fieldTypes);
		}

		return TF.tupleType(fieldTypes, fieldLabels);
	}

}
