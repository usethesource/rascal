package org.rascalmpl.shell;

public class RascalTutorCompile extends AbstractCommandlineTool {
    public static void main(String[] args) {
        main("lang::rascal::tutor::Compiler", new String[] {"org/rascalmpl/tutor"}, args);
    }
}