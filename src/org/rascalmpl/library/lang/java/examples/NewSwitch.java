package org.rascalmpl.library.lang.java.examples;

public class NewSwitch {
    int testFunction(int i) {
        return switch(i) {
            case 0,1,2 -> 1;
            case 3,4,5 -> 2;
            default: yield 3;
        };
    }
}
