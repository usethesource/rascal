package org.rascalmpl.library.experiments.tutor3.commonmark.ext;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.PostProcessor;

public class ConceptValidator implements PostProcessor {
	
	static final HashMap<String, Integer> aspects = new HashMap<>();
	static final Set<String> aspectNames;
	static {
			aspects.put("Name", 0);
			aspects.put("Usage", 1);
			aspects.put("Syntax", 1);
			aspects.put("Types", 1);
			aspects.put("Function", 1);
			aspects.put("Synopsis", 1);	
			aspects.put("Details", 2);
			aspects.put("Description", 3);
			aspects.put("Examples", 4);
			aspects.put("Benefits", 5);
			aspects.put("Pitfalls", 6);
			aspects.put("Questions", 7);
			
			aspectNames = aspects.keySet();
	}

	private PrintWriter err;
	
	String conceptName = "";
	
	ArrayList<String> usedAspectList;
	Set<String> usedAspectSet;
	
	public ConceptValidator(PrintWriter err){
		this.err = err;
		usedAspectList = new ArrayList<>();
		usedAspectSet = new HashSet<>();
	}
	
	public void setConceptName(String name){
		if(!conceptName.isEmpty()){
			checkOrder();
		}
		conceptName = name;
		usedAspectList = new ArrayList<>();
		usedAspectSet = new HashSet<>();
	}
	
	private void checkOrder(){
		int lastIndex = -1;
		for(String aspect : usedAspectList){
			int n = aspects.get(aspect);
			if(n < lastIndex){
				err.println("* " + conceptName + ": illegal order " + aspect);
			}
			lastIndex = n;
		}
	}
	
	@Override
	public Node process(Node node) {
		Heading1Visitor heading1Visitor = new Heading1Visitor();
		node.accept(heading1Visitor);
		return node;
	}
	
	private void doHeading1(Heading h){
		String text = ((Text) h.getFirstChild()).getLiteral();
		if(!aspectNames.contains(text)){
			err.println("* " + conceptName + ": illegal heading " + text);
		} else {
			if(usedAspectSet.contains(text)){
				err.println("* " + conceptName + ": duplicate heading " + text);
			} else {
				usedAspectList.add(text);
				usedAspectSet.add(text);
			}
		}
		if((h.getNext() != null && h.getNext() instanceof Text  && ((Text)h.getNext().getFirstChild()).getLiteral().length() == 0)){
			h.unlink();
		}
		
	}
	
	private class Heading1Visitor extends AbstractVisitor {

		@Override public void visit(Heading h){
			if(h.getLevel() == 1){
				doHeading1(h);
			}
		}
	}

}
