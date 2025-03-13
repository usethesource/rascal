package org.rascalmpl.shell;

public class RascalCheck extends AbstractCommandlineTool {
    public static void main(String[] args) {
        main("lang::rascalcore::check::Checker", new String[] {"org/rascalmpl/compiler","org/rascalmpl/typepal"}, args);
    }
}