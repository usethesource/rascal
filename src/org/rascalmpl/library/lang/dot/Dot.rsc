@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl}

@synopsis{AST and pretty printer for the Dot language.}
@description{
This model of the DOT language is for generative purposes.
If you need to parse DOT files, have a look at ((lang::dot::syntax::Dot)) instead.
}
module lang::dot::Dot
import String;

@synopsis{Abstract Syntax for the Dot language}
data DotGraph =  graph(str id, list[Stm] stmts) | digraph(str id, list[Stm] stmts);

alias NodeId = tuple[str, PortId];

alias PortId = tuple[str, CompassPt];

data CompassPt = N() | NE() | E() | SE() | S() | SW() | W() | NW() | C() | \_();

data Stm 
  = N(str id, Attrs attrs) 
  | N(str id) 
  | N(NodeId nid, Attrs attrs) 
  | N(NodeId nid)
  | E(str from, str to, Attrs attrs) 
  | E(str from, str to) 
  | E(NodeId nfrom, Id to, Attrs attrs) 
  | E(NodeId nfrom, str to) 
  | E(Stm sfrom, str to, Attrs attrs) 
  | E(Stm sfrom, str to) 
  | E(str from, NodeId nto, Attrs attrs) 
  | E(Id from, NodeId nto) 
  | E(NodeId nfrom, NodeId nto, Attrs attrs) 
  | E(NodeId nfrom, NodeId nto) 
  | E(Stm sfrom, NodeId nto, Attrs attrs) 
  | E(Stm sfrom, NodeId nto) 
  | E(str from, Stm sto, Attrs attrs) 
  | E(str from, Stm sto) 
  | E(NodeId nfrom, Stm sto, Attrs attrs) 
  | E(NodeId nfrom, Stm sto) 
  | E(Stm sfrom, Stm sto, Attrs attrs) 
  | E(Stm sfrom, Stm sto)
  | S(str id, list[Stm] stms)
  | S(list[Stm] stms)
  | A(str prop, str val)
  | GRAPH(Attrs attrs)
  | NODE(Attrs attrs)
  | EDGE(Attrs attrs)
  ;

alias Attrs = lrel[str prop, str val];

@synopsis{Translates DotGraph to String input for dot}
str toString(digraph(str id,list[Stm] stms)) 
  = "digraph <id> {<for (x<-stms) {>
    '<oStm(x)>;<}>
    '}
    '"; 


private str oAttrs(lrel[str prop, str val] attrs) = "[<for (<prop,val> <- attrs) {> <prop>=<reLabel(prop, val)>,<}>]";

private str oCompassPt(N())  = "n";
private str oCompassPt(NE())  = "ne";
private str oCompassPt(E())  = "e";
private str oCompassPt(SE())  = "se";
private str oCompassPt(S())  = "s";
private str oCompassPt(SW())  = "sw";
private str oCompassPt(E())  = "w";
private str oCompassPt(NW())  = "nw";
private default str oCompassPt(CompassPt _) = "_";
    
str oPortId(PortId id) {
  if (isEmpty(id[0])) return ":<oCompassPt(id[1])>";
  return ":<id[0]>:<oCompassPt(id[1])>";
}
 
str oNodeId(NodeId id) = "<id[0]><oPortId(id[1])>";
    
str oStms(list[Stm] stms, str sep) = "<for (y <- stms) {> <oStm(y)><sep><}>";

str oStm( N(str id)) = "<id>";
str oStm( N(str id, Attrs attrs)) = "<id><oAttrs(attrs)>";
        
str oStm( E(str from, str to)) = "<from>-\><to>";
str oStm( E(str from, str to, Attrs attrs)) = "<from>-\><to><oAttrs(attrs)>";
        
str oStm( E(NodeId from, str to)) = "<oNodeId(from)>-\><to>";
str oStm( E(str from, str to, Attrs attrs)) = "<from>-\><to><oAttrs(attrs)>";
        
str oStm( E(Stm from, str to)) = "<oStm(from)>-\><to>";
str oStm( E(Stm from, str to, Attrs attrs)) = "<oStm(from)>-\><to><oAttrs(attrs)>";
        
str oStm( E(str from, NodeId to)) = "<from>-\><oNodeId(to)>";
str oStm( E(str from, NodeId to, Attrs attrs)) = "<from>-\><oNodeId(to)><oAttrs(attrs)>";
        
str oStm( E(NodeId from, NodeId to)) = "<oNodeId(from)>-\><oNodeId(to)>";
str oStm( E(str from, NodeId to, Attrs attrs)) = "<from>-\><oNodeId(to)><oAttrs(attrs)>";
        
str oStm( E(Stm from, NodeId to)) = "<oStm(from)>-\><oNodeId(to)>";
str oStm( E(Stm from, NodeId to, Attrs attrs)) = "<oStm(from)>-\><oNodeId(to)><oAttrs(attrs)>";
        
str oStm( E(str from, Stm to)) = "<from>-\><oStm(to)>";
str oStm( E(str from, Stm to, Attrs attrs)) = "<from>-\><oStm(to)><oAttrs(attrs)>";
        
str oStm( E(NodeId from, Stm to)) = "<oNodeId(from)>-\><oStm(to)>";
str oStm( E(str from, Stm to, Attrs attrs)) = "<from>-\><oStm(to)><oAttrs(attrs)>";
        
str oStm( E(Stm from, Stm to)) = "<oStm(from)>-\><oStm(to)>";
str oStm( E(Stm from, Stm to, Attrs attrs)) = "<oStm(from)>-\><oStm(to)><oAttrs(attrs)>";
        
str oStm( S(list[Stm] stms)) = "subgraph {<oStms(stms,";")>} ";
str oStm( S(str id, list[Stm] stms)) = "subgraph <id> {<oStms(stms,";")>} ";
str oStm( A(str prop, str val)) = "<prop> = <val>";
str oStm( GRAPH(Attrs attrs)) = "graph <oAttrs(attrs)>";
str oStm( EDGE(Attrs attrs)) = "edge <oAttrs(attrs)>";
str oStm( NODE(Attrs attrs)) = "node <oAttrs(attrs)>";


