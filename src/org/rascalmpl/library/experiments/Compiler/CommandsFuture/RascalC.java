package org.rascalmpl.library.experiments.Compiler.CommandsFuture;

import org.rascalmpl.value.type.TypeFactory;

public class RascalC {

    public static void main(String[] args) {
        Command.main(args, "rascalc", TypeFactory.getInstance().integerType(), true);
    }

}
