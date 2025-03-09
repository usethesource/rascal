package org.rascalmpl.shell;

public class RascalCompile extends AbstractCommandlineTool {
    public static void main(String[] args) {
        main("lang::rascalcore::compile::Compile", new String[] {"org/rascalmpl/compiler", "org/rascalmpl/typepal"}, args);
    }
}