module Visuals

import AST;
import Resolver;

import vis::Figure;
import vis::Render;
import IO;
import Relation;
import Set;

void renderFeatureDiagram(Included diagrams, Defs defs, ResolvedRefs resolvedRefs) = render("Feature Diagram", visualize(diagrams, defs, resolvedRefs));
void saveRenderedFeatureDiagram(loc file, Included diagrams, Defs defs, ResolvedRefs resolvedRefs) = renderSave(visualize(diagrams, defs, resolvedRefs), file);

Figure visualize(Included diagrams, Defs defs, ResolvedRefs resolvedRefs) =
	renderTree(diagrams, defs, resolvedRefs);

Figure renderTree(Included diagrams, Defs defs, ResolvedRefs resolvedRefs) {
	nodes = [];
  	edges = [];
  
  	for (/def:definition(_, expression) <- diagrams) {
   		<n, es> = showEdges(resolveName(def@location, defs), expression, defs, resolvedRefs);
    	nodes += n;
    	edges += es;
  	}
  
  	return graph(nodes + renderLegend() + [buildNodeWithId(resolveName(def@location, defs)) | /def:atomic(_) <- diagrams], edges, gap(50), hint("layered"));
}

Figure renderLegend() {
	requiredExpl = hcat([box(ellipse(fillColor("black"), size(10)), width(60), lineColor("white")), text("required feature", left())], grow(1.1), left());
	optionalExpl = hcat([box(ellipse(fillColor("white"), size(10)), width(60), lineColor("white")), text("optional feature", left())], grow(1.1), left());
	oneOfExpl = hcat([text("\<one-of\>", width(60)), text("only one of the features" ,left())], grow(1.1), left());
	moreOfExpl = hcat([text("\<more-of\>", width(60)), text("one or more of the features", left())], grow(1.1), left());


	return box(vcat([text("Legend", grow(1.1), fontBold(true)),  requiredExpl, optionalExpl, oneOfExpl, moreOfExpl]), grow(1.2), id("legendShouldNotBeConnected"));
}
       
tuple[list[Figure], list[Edge]] showEdges(QualifiedName from, requireAll(list[FeatureExpression] fs), Defs defs, ResolvedRefs resolvedRefs) 
	=	<[buildNodeWithId(from)], 
		buildEdges(fullyQN(from), fs, defs, resolvedRefs)>;
    
tuple[list[Figure], list[Edge]] showEdges(QualifiedName from, moreOf(list[FeatureExpression] fs), Defs defs, ResolvedRefs resolvedRefs) 
	=	<[buildNodeWithId(from), buildLabeledNode("\<more-of\>", "<fullyQN(from)>-moreof")], 
		buildEdges("<fullyQN(from)>-moreof", fs, defs, resolvedRefs) + [edge(fullyQN(from), "<fullyQN(from)>-moreof")]>;
    
tuple[list[Figure], list[Edge]] showEdges(QualifiedName from, oneOf(list[FeatureExpression] fs), Defs defs, ResolvedRefs resolvedRefs)
  	= 	<[buildNodeWithId(from), buildLabeledNode("\<one-of\>", "<fullyQN(from)>-oneof")], 
		buildEdges("<fullyQN(from)>-oneof", fs, defs, resolvedRefs) + [edge(fullyQN(from), "<fullyQN(from)>-oneof")]>;
    
default tuple[list[Figure], list[Edge]] showEdges(QualifiedName from, FeatureExpression fe, Defs defs, ResolvedRefs resolvedRefs) 
	= 	<[buildNodeWithId(from)], buildEdges(fullyQN(from), [fe], defs, resolvedRefs)>;

private QualifiedName resolveName(loc location, Defs allDefs) =
	getOneFrom(invert(allDefs)[location]);

private Figure buildLabeledNode(str label, str idStr) =
	box(text(label), fillColor("white"), lineColor("white"), id(idStr));

private Figure buildNodeWithId(QualifiedName name) =
	box(text(name.name), id(fullyQN(name)), grow(1.5));
	
private str fullyQN(QualifiedName name) = "<name.namespace>.<name.name>";

private list[Edge] buildEdges(str from, list[FeatureExpression] fs, Defs defs, ResolvedRefs resolvedRefs) =
	[buildEdge(from, fullyQN(resolveRef(ref, defs, resolvedRefs)), "black") | ref:ref(_) <- fs] +
	[buildEdge(from, fullyQN(resolveDef(def, defs)), "black") | def:atomic(_) <- fs] +
	[buildEdge(from, fullyQN(resolveRef(ref, defs, resolvedRefs)), "white") | optional(ref:ref(_)) <- fs] +
	[buildEdge(from, fullyQN(resolveDef(def, defs)), "white") | optional(def:atomic(_)) <- fs];
	
private Edge buildEdge(str from, str to, str color) =
	edge(from, to, ellipse(size(10), fillColor(color)));
	
private QualifiedName resolveRef(FeatureExpression refExp, Defs defs, ResolvedRefs resolvedRefs) =
	getOneFrom(invert(defs)[resolvedRefs[refExp@location]]);
	
private QualifiedName resolveDef(FeatureExpression defExp, Defs defs) =
	getOneFrom(invert(defs)[defExp@location]);
	 	