@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::check::DependencyViewer

import analysis::typepal::TModel;
import analysis::typepal::Collector;
import vis::Graphs;
import IO;
import util::IDEServices;
import ListRelation;
import Map;
import Relation;
import Set;

// Management of node identities
alias NodeId = str;
int nodeCounter = 0;

map[NodeId, value] id2node = ();
map[value, NodeId] node2id = ();

map[NodeId,loc] node2src = ();
map[NodeId,str] node2label = ();
map[loc,NodeId] src2calc = ();

void initNodes(){
    nodeCounter = 0;
    id2node = ();
    node2id = ();
    node2src = ();
    node2label = ();
    src2calc = ();
}

NodeId getNodeId(value v){
    assert Calculator _ := v || Requirement _ := v || loc _ := v: "Illegal <v>";
    if(v notin node2id) {
        node2id[v] = "<nodeCounter>";
        id2node["<nodeCounter>"] = v;
        nodeCounter += 1;
    }
    return node2id[v];
}

NodeId getNodeIdViaCalculator(loc l){
    if(l in src2calc){
        return src2calc[l];
    }
    return getNodeId(l);
}

loc getNodeSource(NodeId id) {
    if(id in node2src) return node2src[id];
    v = id2node[id];
    if(loc l := v) return l;
    return |unknown:///|;
}

str getNodeLabel(NodeId id) {
    if(id in node2label) return node2label[id];
    v = id2node[id];
    if(loc l := v) return "<id>: <getText(l)>";
    return "<id>: ???:<v>";
}

str getText(loc l) {
    try return readFile(l);
    catch _: return "???<l>";
}

lrel[NodeId,NodeId] srcDependsOn(NodeId src, list[loc] dependsOn)
    = [<src, getNodeIdViaCalculator(d)> | d <- dependsOn];

lrel[NodeId,NodeId] srcDependsOn(NodeId l, NodeId r, list[loc] dependsOn)
    = srcDependsOn(l, dependsOn) + srcDependsOn(r, dependsOn);

lrel[NodeId,NodeId] calcEdges(Calculator c:calcType(loc src, AType atype)) {
    NodeId id = getNodeId(c);
    node2src[id] = src;
    node2label[id] = "<id>: <getText(src)>";
    src2calc[src] = id;
    return [<id, getNodeId(|nothing:///|)>];
}

lrel[NodeId,NodeId] calcEdges(c:calcLoc(loc src, list[loc] dependsOn)){
    NodeId id = getNodeId(c);
    node2src[id] = src;
    node2label[id] = "<id>: <getText(src)>";
    src2calc[src] = id;
    return srcDependsOn(id, dependsOn);
}

lrel[NodeId,NodeId] calcEdges(c:calc(str cname, loc src, list[loc] dependsOn, AType(Solver s) getAType)){
    NodeId id = getNodeId(c);
    node2src[id] = src;
    node2label[id] = "C <id>, <cname>: <getText(src)>";
    src2calc[src] = id;
    return srcDependsOn(id, dependsOn);
}

lrel[NodeId,NodeId] calcEdges(c:calcLub(str cname, list[loc] srcs, list[loc] dependsOn, list[AType(Solver s)] getATypes)){
    NodeId id = getNodeId(c);
    node2label[id] = "C <id>, <cname>: <for(src<-srcs){><getText(src)>/<}>";
    for(src <- srcs) src2calc[src] = id;
    return srcDependsOn(id, dependsOn);
}

lrel[NodeId,NodeId] reqEdges(rq:req(str rname, loc src,  list[loc] dependsOn, void(Solver s) preds)){
    NodeId id = getNodeId(rq);
    node2src[id] = src;
    node2label[id] = "R <rname>: <getText(src)>";
    return srcDependsOn(id, dependsOn);
}

lrel[NodeId,NodeId] reqEdges(rq:reqEqual(str rname, value l, value r, list[loc] dependsOn, FailMessage fm)){
    NodeId id = getNodeId(rq);
    node2label[id] = "R <rname>";
    return srcDependsOn(getNodeId(l), getNodeId(r), dependsOn);
}

lrel[NodeId,NodeId] reqEdges(rq:reqComparable(str rname, value l, value r, list[loc] dependsOn, FailMessage fm)){
    NodeId id = getNodeId(rq);
    node2label[id] = "R <rname>";
    return srcDependsOn(getNodeId(l), getNodeId(r), dependsOn);
}

lrel[NodeId,NodeId] reqEdges(rq:reqSubtype(str rname, value l, value r, list[loc] dependsOn, FailMessage fm)){
    NodeId id = getNodeId(rq);
    node2label[id] = "R <rname>";
    return srcDependsOn(getNodeId(l), getNodeId(r), dependsOn);
}

lrel[NodeId,NodeId] reqEdges(rq:reqUnify(str rname, value l, value r, list[loc] dependsOn, FailMessage fm)){
    NodeId id = getNodeId(rq);
    node2label[id] = "R <rname>";
    return srcDependsOn(getNodeId(l), getNodeId(r), dependsOn);
}

lrel[NodeId,NodeId] reqEdges(rq:reqError (loc src, list[loc] dependsOn, FailMessage fm)){
    NodeId id = getNodeId(rq);
    node2src[id] = src;
    node2label[id] = "R error: <getText(src)>";
    return srcDependsOn(id, dependsOn);
}

lrel[NodeId,NodeId] reqEdges(rq:reqErrors(loc src, list[loc] dependsOn, list[FailMessage] fms)){
    NodeId id = getNodeId(rq);
    node2src[id] = src;
    node2label[id] = "R errors: <getText(src)>";
    return srcDependsOn(id, dependsOn);
}

NodeId replaceByCalc(NodeId id){
    if(id in node2src){
        src = node2src[id];
        if(src in src2calc) return src2calc[src];
    }
    return id;
}

void viewDependencies(TModel tm){
    initNodes();
    for(Calculator c <- tm.calculators){
        NodeId id = getNodeId(c);
        if(c has src){
            src2calc[c.src] = id;
        } else if(c has srsc){
            for(src <- c.srcs){
                src2calc[src] = id;
            }
        }
    }

    cedges = {*calcEdges(c) | Calculator c <- tm.calculators};
    redges = {*reqEdges(r) | Requirement r <- tm.requirements};
    edges = toList(cedges + redges);
    println("edges: <edges>");
    for(NodeId id <- sort(domain(id2node))) { println("<id>"); iprintln(id2node[id]); }

    list[str] nodeClassifier(NodeId id){
        res = [];
        switch(id2node[id]){
            case Calculator _: res = ["calc"];
            case Requirement _: res = ["req"];
            case loc _: res = ["text"];
        }
        return res;
    }

    styles = [
        cytoStyleOf( 
            selector=\node(className("text")),
            style=defaultNodeStyle()[shape=CytoNodeShape::rectangle()][\background-color="grey"]),
        cytoStyleOf( 
            selector=\node(className("calc")),
            style=defaultNodeStyle()[shape=CytoNodeShape::ellipse()][\background-color="green"]),
        cytoStyleOf( 
            selector=\node(className("req")),
            style=defaultNodeStyle()[shape=CytoNodeShape::diamond()][\background-color="blue"])
    ];
    cfg = cytoGraphConfig(
            title="Graph: <tm.modelName>",
            nodeClassifier=nodeClassifier, 
            nodeLinker=getNodeSource,
            nodeLabeler=getNodeLabel, 
            styles=styles,
            \layout=defaultDagreLayout()[ranker=\tight-tree()]);

    showInteractiveContent(graph(edges, cfg=cfg));
}