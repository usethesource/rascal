@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl}

module lang::dot::\syntax::Dot

start syntax DOT = "strict"? strict Graph graph Id id "{" StatementList stats "}" "\n"?;

keyword Reserved 
  = "graph" 
  | "digraph" 
  | "node" 
  | "edge" 
  | "subgraph"
  | "strict"
  ;

syntax Graph 
  = graph: "graph"
  | digraph: "digraph"
  ;

syntax StatementList = Statement*;

syntax Statement = Stat stat ";"?;

syntax Stat
  = nodeStat: NodeStatement
  | edgeStat: EdgeStatement
  | attrStat: AttrStatement  
  | attr: Id name "=" Id value
  | subgraph: Subgraph
  ;

syntax NodeStatement = NodeId nodeId AttrList? attrs;

syntax NodeId = Id id Port? port;

lexical Id
  = ([A-Z a-z 0-9 _] !<< [a-z A-Z 0-9 _][a-z A-Z 0-9 _]* !>> [0-9 A-Z _ a-z]) \ Reserved 
  | [\"] (![\"] | "\\\"")* [\"]
  | [\-]? "." [0-9]+
  | [\-]? [0-9]+ "." [0-9]*
  ;

syntax Port 
  = idPort: ":" Id id PortCompass? compass
  | compassPort: PortCompass;

syntax PortCompass = ":" CompassPoint compassPoint;

syntax CompassPoint
  = north: "n"
  | north_east: "ne"
  | east: "e"
  | south_east: "se"
  | south: "s"
  | south_west: "sw"
  | west: "w"
  | north_west: "nw"
  | center: "c"
  | unknown: "_";

syntax AttrList = "[" Attribute* attrs "]";

syntax Attribute = Attr [;,]?;

syntax Attr = Id name "=" Id value;

syntax EdgeStatement
  = edge: NodeId nodeId EdgeRhs rhs AttrList? attrs
  | subgraph: Subgraph sub EdgeRhs rhs AttrList? attrs;

syntax EdgeRhs
  = EdgeOp NodeId nodeId EdgeRhs? rhs
  | EdgeOp Subgraph subgraph EdgeRhs? rhs;
  
syntax EdgeOp = "-\>" | "--";

syntax Subgraph = SubgraphId? id "{" StatementList stats "}";

syntax SubgraphId = "subgraph" Id? id;

syntax AttrStatement = AttrTag tag AttrList attrs;

syntax AttrTag 
  = "node" 
  | "edge" 
  | "graph"
  ;

lexical Comment
  = "/*" (![*] | [*] !>> "/")* "*/"
  | "//" ![\n]* $
  ;

layout LAYOUTLIST = LAYOUT* !>> [\ \t\n\r] !>> "//" !>> "/*";                   

lexical LAYOUT
  = Whitespace: [\ \t\n\r] 
  | @category="Comment" Comment
  ;
