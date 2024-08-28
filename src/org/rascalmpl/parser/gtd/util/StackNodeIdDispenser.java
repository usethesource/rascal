package org.rascalmpl.parser.gtd.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.rascalmpl.parser.gtd.IGTD;
import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;

/**
 * To offer backwards compatibility for generated parsers that do not yet have the "getFreeStackNodeId" method yet,
 * this class uses reflection to find that method and otherwise improvises by just using a ridiculously high starting number.
 */
public class StackNodeIdDispenser implements IdDispenser {
    private IGTD<IConstructor, ITree, ISourceLocation> parser;
    private Method dispenseMethod;
    private int nextNodeIdBackup = (Integer.MAX_VALUE/4)*3;

    public StackNodeIdDispenser(IGTD<IConstructor, ITree, ISourceLocation> parser) {
        try {
            dispenseMethod = parser.getClass().getMethod("getFreeStackNodeId");
        } catch (NoSuchMethodException e) {
            // Custom IGTB implementation without "getFreeStackNodeId" method. No biggy, we just use nextNodeIdBackup.
        }
    }

    @Override
    public int dispenseId() {
        if (dispenseMethod != null) {
            try {
                return (Integer)dispenseMethod.invoke(parser);
            }
            catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof UnsupportedOperationException) {
                    // We are dealing with a parser class that has no generated "getFreeStackNodeId" method (yet),
                    // for backwards compatibility we fall back on "nextNodeIdBackup".
                    dispenseMethod = null; // No reason to try again.
                } else {
                    throw new RuntimeException(e);
                }
            }
            catch (IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
        }

        return nextNodeIdBackup++;
    }

}

