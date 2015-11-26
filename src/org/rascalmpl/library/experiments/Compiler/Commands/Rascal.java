package org.rascalmpl.library.experiments.Compiler.Commands;

import org.rascalmpl.value.type.TypeFactory;

public class Rascal {

    public static void main(String[] args) {
        Command.main(args, "rascal", TypeFactory.getInstance().valueType(), false);
    }

}
