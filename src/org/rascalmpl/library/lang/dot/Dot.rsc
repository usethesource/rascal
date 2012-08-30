@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl}

module lang::dot::Dot
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

|E(Id from, Id to, Attrs attrs) |   E(Id from, Id to) 
|E(NodeId nfrom, Id to, Attrs attrs) |   E(NodeId nfrom, Id to) 
|E(Stm sfrom, Id to, Attrs attrs) |   E(Stm sfrom, Id to) 

|E(Id from, NodeId nto, Attrs attrs) |   E(Id from, NodeId nto) 
|E(NodeId nfrom, NodeId nto, Attrs attrs) |   E(NodeId nfrom, NodeId nto) 
|E(Stm sfrom, NodeId nto, Attrs attrs) |   E(Stm sfrom, NodeId nto) 

|E(Id from, Stm sto, Attrs attrs) |   E(Id from, Stm sto) 
|E(NodeId nfrom, Stm sto, Attrs attrs) |   E(NodeId nfrom, Stm sto) 
|E(Stm sfrom, Stm sto, Attrs attrs) |   E(Stm sfrom, Stm sto)
 
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

@doc{Translates DotGraph to String input for dot}
public str toString(DotGraph g) {
       if (digraph(Id id,Stms stms):=g) {
            str r= "digraph <id> {<for (x<-stms) {>\n<oStm(x)>;<}>}\n"; 
            return r;
            }
    return "error";
    }

public str toString(Dotline g) {
    return toString(g.graph);
    }
    
        
public list[value] getChildren(value key) {
    if (int k:=key) {
       if (k==-1) return toList(domain(currentOutline));
       return currentOutline[k];
       }     
    return [];
    }
/*    
public str getParent(Dotline g, str v) {
    Outline m = g.outline;
    list[str] parents =  [d|str d<-domain(m), v in m[d]];
    if (isEmpty(parents)) return "";
    return parents[0];
    }
*/ 
/*--------------------------------------------------------------------------------------------------------------------------*/

DotGraph g1(int n) {
    Stms ts = [E(i, "<i>", (i+1) mod n) |int i<-[0,1..n-1]];
    Stm t = S([A("rank","source"), N(5)]);
    return digraph("g1", ts+t);
    }

str reLabel(str prop, str val) {
  println(prop);
  if (prop=="label") {
      return "\"<replaceAll(val,"\"","\\\"")>\"";
      }
  return val;
  }

str oAttrs(Attrs attrs) {
    return "[<for (y<-attrs) {> <y.prop>=<reLabel(y.prop, y.val)>,<}>]";
    }

str oCompassPt(CompassPt id) {
    switch (id) {
    case N():return "n";
    case NE():return "ne";
    case E(): return "e"; 
    case SE(): return "se";
    case S(): return "s";
    case SW(): return "sw";
    case W(): return "w";
    case NW(): return "nw";
    case C(): return "c";
    case _(): return "_";
    }
    return "_";
    }
    
str oPortId(PortId id) {
    if (isEmpty(id[0])) return ":<oCompassPt(id[1])>";
    return ":<id[0]>:<oCompassPt(id[1])>";
    }
 
str oNodeId(NodeId id) {
    return "<id[0]><oPortId(id[1])>";
    }
    
str oStms(Stms stms, str sep) {
    return "<for (y<-stms) {> <oStm(y)><sep><}>";
    }

str oStm(Stm x) {
    switch (x) {
        case N(Id id): return "<id>";
        case N(Id id, Attrs attrs): return "<id><oAttrs(attrs)>";
        
        case E(Id from, Id to): return "<from>-\><to>";
        case E(Id from, Id to, Attrs attrs): return "<from>-\><to><oAttrs(attrs)>";
        
        case E(NodeId from, Id to): return "<oNodeId(from)>-\><to>";
        case E(Id from, Id to, Attrs attrs): return "<oNodeId(from)>-\><to><oAttrs(attrs)>";
        
        case E(Stm from, Id to): return "<oStm(from)>-\><to>";
        case E(Stm from, Id to, Attrs attrs): return "<oStm(from)>-\><to><oAttrs(attrs)>";
        
        case E(Id from, NodeId to): return "<from>-\><oNodeId(to)>";
        case E(Id from, NodeId to, Attrs attrs): return "<from>-\><oNodeId(to)><oAttrs(attrs)>";
        
        case E(NodeId from, NodeId to): return "<oNodeId(from)>-\><oNodeId(to)>";
        case E(Id from, NodeId to, Attrs attrs): return "<oNodeId(from)>-\><oNodeId(to)><oAttr(attrs)>";
        
        case E(Stm from, NodeId to): return "<oStm(from)>-\><oNodeId(to)>";
        case E(Stm from, NodeId to, Attrs attrs): return "<oStm(from)>-\><oNodeId(to)><oAttrs(attrs)>";
        
        case E(Id from, Stm to): return "<from>-\><oStm(to)>";
        case E(Id from, Stm to, Attrs attrs): return "<from>-\><oStm(to)><oAttrs(attrs)>";
        
        case E(NodeId from, Stm to): return "<oNodeId(from)>-\><oStm(to)>";
        case E(Id from, Stm to, Attrs attrs): return "<oNodeId(from)>-\><oStm(to)><oAttrs(attrs)>";
        
        case E(Stm from, Stm to): return "<oStm(from)>-\><oStmt(to)>";
        case E(Stm from, Stm to, Attrs attrs): return "<oStm(from)>-\><oStm(to)><oAttrs(attrs)>";
        
        case S(Stms stms): return "subgraph {<oStms(stms,";")>} ";
        case S(Id id, Stms stms): return "subgraph <id> {<oStms(stms,";")>} ";
        case A(Id prop, Id val): return "<prop> = <val>";
        case GRAPH(Attrs attrs): return "graph <oAttrs(attrs)>";
        case EDGE(Attrs attrs): return "edge <oAttrs(attrs)>";
        case NODE(Attrs attrs): return "node <oAttrs(attrs)>";
        }
        return "ERROR";
    }



