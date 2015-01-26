@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl}

module experiments::Compiler::Benchmarks::dot::Dot

import Exception;
import IO;
import String;
import Set;
import Map;

public alias Id = str;

@doc{Abstract Data Type of Dot language}

public data DotGraph =  graph(Id id, Stms stmts) | digraph(Id id, Stms stmts);

public alias Stms = list[Stm];

public alias NodeId = tuple[Id, PortId];

public alias PortId = tuple[Id, CompassPt];

public data CompassPt = N()|NE()|E()|SE()|S()|SW()|W()|NW()|C()|_();



public data Stm = 
  N(Id id, Attrs attrs) | N(Id id) 
| N(NodeId nid, Attrs attrs) | N(NodeId nid)

|   E(Id from, Id to) 
|   E(NodeId nfrom, Id to) 
|   E(Stm sfrom, Id to) 

|   E(Id from, NodeId nto) 
|   E(NodeId nfrom, NodeId nto) 
|   E(Stm sfrom, NodeId nto) 

|   E(Id from, Stm sto) 
|   E(NodeId nfrom, Stm sto) 
|   E(Stm sfrom, Stm sto)
 
|S(Id id, Stms stms)| S(Stms stms)| A(Id prop, Id val)|
GRAPH(Attrs attrs)|NODE(Attrs attrs)|EDGE(Attrs attrs);

public alias Attr =  tuple[str prop,  Id val];

public alias Attrs = list[Attr];

public alias Outline = map[int key, list[str] args];

public alias Dotline = tuple[DotGraph graph, Outline outline];


@doc{Dummy function call needed to tag initialized global variables of type DotGraph.
It is possible to select that variable on the outline menu of the Rascal Editor.
An application is for example to display dotgraphs.}  
 
public DotGraph export(DotGraph g) {return g;}

public Dotline export(Dotline g) {return g;}

public bool hasOutline(Dotline g) {return true;}

public bool hasOutline(DotGraph g) {return false;}

public Outline currentOutline;

public void setCurrentOutline(Dotline current) {
   currentOutline = current.outline;
}
