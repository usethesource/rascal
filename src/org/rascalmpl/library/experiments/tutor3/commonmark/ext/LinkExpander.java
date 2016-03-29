package org.rascalmpl.library.experiments.tutor3.commonmark.ext;

import java.io.PrintWriter;
import java.util.ArrayList;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.PostProcessor;
import org.rascalmpl.library.experiments.tutor3.Concept;
import org.rascalmpl.library.experiments.tutor3.Onthology;

public class LinkExpander implements PostProcessor{

	private Onthology onthology;
	private PrintWriter err;
	String conceptName = "";

	public LinkExpander(Onthology onthology, PrintWriter err){
		this.onthology = onthology;
		this.err = err;
	}
	
	public void setConceptName(String name){
		conceptName = name;
	}
	
	@Override
	public Node process(Node node) {
		LinkVisitor linkVisitor = new LinkVisitor();
		node.accept(linkVisitor);
		return node;
	}
	
	private boolean isConceptReference(String dest){
		return dest.matches("(\\w|\\/)+");
	}
	
	private void resolveLink(Link link, String conceptRef){
		ArrayList<Concept> referencedConcepts = onthology.resolve(conceptRef);
		int nsolutions = referencedConcepts.size();
		if(nsolutions == 0){
			err.println("* " + conceptName + ": reference to unknown concept " + conceptRef);
		} else if(nsolutions > 1){
			err.println("* " + conceptName + ": ambiguous concept " + conceptRef);
		} else {
			link.setDestination("file://" + referencedConcepts.get(0).getHtmlFileName());
		}
	}
	
	private void expandLink(Link link){
		String linkText = ((Text)link.getFirstChild()).getLiteral().toString();
		String destination = link.getDestination();
		if(destination.isEmpty()){
			resolveLink(link, linkText);
		} else if(isConceptReference(destination)){
			resolveLink(link, destination);
		}
	}

	private class LinkVisitor extends AbstractVisitor {
		@Override public void visit(Link link){
			expandLink(link);
		}
	}
}
