package org.rascalmpl.library.experiments.Compiler.Commands;

import org.rascalmpl.value.type.TypeFactory;

public class RascalTests {

    public static void main(String[] args) {
        Command.main(args, "rascalTests", TypeFactory.getInstance().integerType(), true);
    }

}
