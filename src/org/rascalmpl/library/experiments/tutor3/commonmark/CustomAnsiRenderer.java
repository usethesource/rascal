package org.rascalmpl.library.experiments.tutor3.commonmark;

import org.commonmark.node.Node;
import org.commonmark.node.Visitor;

public interface CustomAnsiRenderer {
	 boolean render(Node node, AnsiWriter ansiWriter, Visitor visitor);
}
