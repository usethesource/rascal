package org.rascalmpl.debug;

import java.util.Set;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.interpreter.result.IRascalResult;

public interface IRascalFrame {
    /**
     * @return which other modules does this frame depend on
     */
    Set<String> getImports();
    
    /**
     * @return which names of variables are in scope
     */
    Set<String> getFrameVariables();
    
    /**
     * @return the current value and (static) type of the given variable in this frame
     */
    IRascalResult getFrameVariable(String name);
    
    /**
     * @return a human readable name for this stack frame (name of the module or function)
     */
    String getName();
    
    /**
     * @return the current point of execution when this frame was created or `null` if this is a root frame
     */
    ISourceLocation getCallerLocation();
}
