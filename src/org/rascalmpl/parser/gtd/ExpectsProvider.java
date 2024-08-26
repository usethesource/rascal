package org.rascalmpl.parser.gtd;

import org.rascalmpl.parser.gtd.stack.AbstractStackNode;

public interface ExpectsProvider<P> {
    AbstractStackNode<P>[] getExpects(String nonTerminal);
}
