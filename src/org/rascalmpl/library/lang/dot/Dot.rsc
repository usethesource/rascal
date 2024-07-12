@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl}

module lang::dot::Dot
import String;
import Set;
import Map;

alias Id = str;

@synopsis{Abstract Data Type of Dot language}
data DotGraph =  graph(Id id, Stms stmts) | digraph(Id id, Stms stmts);

alias Stms = list[Stm];

alias NodeId = tuple[Id, PortId];

alias PortId = tuple[Id, CompassPt];

data CompassPt = N()|NE()|E()|SE()|S()|SW()|W()|NW()|C()|_();

data Stm 
  = N(Id id, Attrs attrs) 
  | N(Id id) 
  | N(NodeId nid, Attrs attrs) 
  | N(NodeId nid)
  | E(Id from, Id to, Attrs attrs) 
  | E(Id from, Id to) 
  | E(NodeId nfrom, Id to, Attrs attrs) 
  | E(NodeId nfrom, Id to) 
  | E(Stm sfrom, Id to, Attrs attrs) 
  | E(Stm sfrom, Id to) 
  | E(Id from, NodeId nto, Attrs attrs) 
  | E(Id from, NodeId nto) 
  | E(NodeId nfrom, NodeId nto, Attrs attrs) 
  | E(NodeId nfrom, NodeId nto) 
  | E(Stm sfrom, NodeId nto, Attrs attrs) 
  | E(Stm sfrom, NodeId nto) 
  | E(Id from, Stm sto, Attrs attrs) 
  | E(Id from, Stm sto) 
  | E(NodeId nfrom, Stm sto, Attrs attrs) 
  | E(NodeId nfrom, Stm sto) 
  | E(Stm sfrom, Stm sto, Attrs attrs) 
  | E(Stm sfrom, Stm sto)
  | S(Id id, Stms stms)
  | S(Stms stms)
  | A(Id prop, Id val)
  | GRAPH(Attrs attrs)
  | NODE(Attrs attrs)
  | EDGE(Attrs attrs)
  ;

alias Attr =  tuple[str prop,  Id val];

alias Attrs = list[Attr];

alias Outline = map[int key, list[str] args];

alias Dotline = tuple[DotGraph graph, Outline outline];


@synopsis{Dummy function call needed to tag initialized global variables of type DotGraph.
It is possible to select that variable on the outline menu of the Rascal Editor.
An application is for example to display dotgraphs.}  
 
DotGraph export(DotGraph g) {return g;}

Dotline export(Dotline g) {return g;}

bool hasOutline(Dotline _) {return true;}

bool hasOutline(DotGraph _) {return false;}

Outline currentOutline;

void setCurrentOutline(Dotline current) {
   currentOutline = current.outline;
}

@synopsis{Translates DotGraph to String input for dot}
str toString(digraph(Id id,Stms stms)) 
  = "digraph <id> {<for (x<-stms) {>
    '<oStm(x)>;<}>
    '}
    '"; 

str toString(Dotline g) = toString(g.graph);
    
list[value] getChildren(value key) {
  if (int k:=key) {
    if (k==-1) 
      return toList(domain(currentOutline));
    return currentOutline[k];
  }     
  return [];
}

private str reLabel(str prop, str val) {
  if (prop=="label") {
      return "\"<replaceAll(val,"\"","\\\"")>\"";
  }
  
  return val;
}

private str oAttrs(Attrs attrs) = "[<for (y<-attrs) {> <y.prop>=<reLabel(y.prop, y.val)>,<}>]";

private str oCompassPt(N())  = "n";
private str oCompassPt(NE())  = "ne";
private str oCompassPt(E())  = "e";
private str oCompassPt(SE())  = "se";
private str oCompassPt(S())  = "s";
private str oCompassPt(SW())  = "sw";
private str oCompassPt(W())  = "w";
private str oCompassPt(NW())  = "nw";
private default str oCompassPt(CompassPt _) = "_";
    
str oPortId(PortId id) {
  if (isEmpty(id[0])) return ":<oCompassPt(id[1])>";
  return ":<id[0]>:<oCompassPt(id[1])>";
}
 
str oNodeId(NodeId id) = "<id[0]><oPortId(id[1])>";
    
str oStms(Stms stms, str sep) = "<for (y <- stms) {> <oStm(y)><sep><}>";

str oStm( N(Id id)) = "<id>";
str oStm( N(Id id, Attrs attrs)) = "<id><oAttrs(attrs)>";
        
str oStm( E(Id from, Id to)) = "<from>-\><to>";
str oStm( E(Id from, Id to, Attrs attrs)) = "<from>-\><to><oAttrs(attrs)>";
        
str oStm( E(NodeId from, Id to)) = "<oNodeId(from)>-\><to>";
//str oStm( E(Id from, Id to, Attrs attrs)) = "<from>-\><to><oAttrs(attrs)>";
        
str oStm( E(Stm from, Id to)) = "<oStm(from)>-\><to>";
str oStm( E(Stm from, Id to, Attrs attrs)) = "<oStm(from)>-\><to><oAttrs(attrs)>";
        
str oStm( E(Id from, NodeId to)) = "<from>-\><oNodeId(to)>";
str oStm( E(Id from, NodeId to, Attrs attrs)) = "<from>-\><oNodeId(to)><oAttrs(attrs)>";
        
str oStm( E(NodeId from, NodeId to)) = "<oNodeId(from)>-\><oNodeId(to)>";
//str oStm( E(Id from, NodeId to, Attrs attrs)) = "<from>-\><oNodeId(to)><oAttrs(attrs)>";
        
str oStm( E(Stm from, NodeId to)) = "<oStm(from)>-\><oNodeId(to)>";
str oStm( E(Stm from, NodeId to, Attrs attrs)) = "<oStm(from)>-\><oNodeId(to)><oAttrs(attrs)>";
        
str oStm( E(Id from, Stm to)) = "<from>-\><oStm(to)>";
str oStm( E(Id from, Stm to, Attrs attrs)) = "<from>-\><oStm(to)><oAttrs(attrs)>";
        
str oStm( E(NodeId from, Stm to)) = "<oNodeId(from)>-\><oStm(to)>";
//str oStm( E(Id from, Stm to, Attrs attrs)) = "<from>-\><oStm(to)><oAttrs(attrs)>";
        
str oStm( E(Stm from, Stm to)) = "<oStm(from)>-\><oStm(to)>";
str oStm( E(Stm from, Stm to, Attrs attrs)) = "<oStm(from)>-\><oStm(to)><oAttrs(attrs)>";
        
str oStm( S(Stms stms)) = "subgraph {<oStms(stms,";")>} ";
str oStm( S(Id id, Stms stms)) = "subgraph <id> {<oStms(stms,";")>} ";
str oStm( A(Id prop, Id val)) = "<prop> = <val>";
str oStm( GRAPH(Attrs attrs)) = "graph <oAttrs(attrs)>";
str oStm( EDGE(Attrs attrs)) = "edge <oAttrs(attrs)>";
str oStm( NODE(Attrs attrs)) = "node <oAttrs(attrs)>";


