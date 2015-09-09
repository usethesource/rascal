package org.rascalmpl.debug;

import java.util.Stack;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public interface IRascalRuntimeInspection {
    /**
     * @return a reflection of the currently paused stack
     */
    Stack<IRascalFrame> getCurrentStack();
    
    /**
     * @return the top of the currently paused stack
     */
    IRascalFrame getTopFrame();
    
    /**
     * @return which statement or expression is now being executed (source location)
     */
    ISourceLocation getCurrentPointOfExecution();
    
    /**
     * @return a reflection of the singleton instance of the given module
     */
    IRascalFrame getModule(String name);
}
