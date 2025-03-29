package org.rascalmpl.shell;

public class RascalPackage extends AbstractCommandlineTool {
    public static void main(String[] args) {
        main("lang::rascalcore::package::Packager", new String[] { }, args);
    }
}