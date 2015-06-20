@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jouke Stoel - Jouke.Stoel@cwi.nl}
module Plugin

import AST;
import Parse;
import Load;
import Resolver;
import Checker;
import Proposer;
import Visuals;

import util::IDE;
import util::Prompt;
import util::ContentCompletion;
import vis::Figure;
import vis::Render;
import IO;
import ValueIO;
import Message;

import ParseTree;
import AST;
import Node;
import Map;

anno rel[loc, loc] Tree@hyperlinks;

private alias Information = tuple[FeatureDiagram current, Included included, Defs defs, Refs refs, 	RefsInDef refsInDef, ResolvedRefs resolvedRefs];

public void main() {
	str LANGUAGE_NAME = "Feature Diagram Language";
	str LANGUAGE_EXTENTION  = "fdl";


	registerLanguage(LANGUAGE_NAME, LANGUAGE_EXTENTION, Tree(str src, loc l) {
    	return parseFeatureDiagram(src, l);
  	});
  	
	contribs = {
    	annotator(Tree (Tree fd) {
      		<ast, diagrams, defs, refs, refsInDef, resolvedRefs> = gatherInfo(fd);
    		     		
      		msgs = check(defs, refs, refsInDef, resolvedRefs);
      		return fd[@messages=msgs][@hyperlinks=resolvedRefs];
    	}),
    	
    	proposer(list[CompletionProposal] (Tree fd, str prefix, int requestOffset) {
    		<ast, diagrams, defs, _, _, resolvedRefs> = gatherInfo(fd);
    		
    		return propose(ast, diagrams, resolvedRefs, defs, prefix, requestOffset);    		
    	}, alphaNumeric + "."),
 
    	popup(
	      menu("Feature Diagram", [
	        action("Visualise", void (Tree fd, loc selection) {
	      		<_, diagrams, defs, refs, refsInDef, resolvedRefs> = gatherInfo(fd);
	        	
	        	check(defs, refs, refsInDef, resolvedRefs) == {}
	        		? renderFeatureDiagram(diagrams, defs, resolvedRefs)
	        		: alert("The diagram definition contains errors. They need to be fixed before the graph can be created");
	        	
	        }),
	        action("Save visualisation", void (Tree fd, loc selection) {
	      		<_, diagrams, defs, refs, refsInDef, resolvedRefs> = gatherInfo(fd);
	        	
	        	if (check(defs, refs, refsInDef, resolvedRefs) != {}) {
	        		alert("The diagram definition contains errors. They need to be fixed before the graph can be created");
	        	}
	        	
	        	name = prompt("Enter the file name for the visualisation: ");
	        	
	        	saveRenderedFeatureDiagram(|project://feature-diagram-language/bin/<name>.png|, diagrams, defs, resolvedRefs);
	        })
	      ])
	    )    	
    };
    
    Information gatherInfo(Tree fd) {
  						ast = implodeFDL(fd);
		diagrams = resolveIncludes(ast);
		defs = resolveDefinitions(diagrams);
		refs = resolveReferences(diagrams);
		refsInDef = resolveReferencesInDefinitions(diagrams);	
		resolved = resolveFeatures(defs, refs);
		
		return <ast, diagrams, defs, refs, refsInDef, resolved>;    	
    }
    
   	registerContributions(LANGUAGE_NAME, contribs);
}
