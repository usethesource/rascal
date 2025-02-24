@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl}

module lang::dot::\syntax::Dot

start syntax DOT = Graph  Id "{" StatementList "}" "\n"?;

keyword Reserved 
  = "graph" 
  | "digraph" 
  | "node" 
  | "edge" 
  | "subgraph"
  ;

syntax Graph 
  = "graph" 
  | "digraph" 
  | AttrTag
  ;

syntax AttrTag 
  = "node" 
  | "edge" 
  | "graph"
  ;

syntax Nod 
  = NodeId
  | Subgraph
  ;

lexical Id 
  = ([A-Z a-z 0-9 _] !<< [a-z A-Z 0-9 _][a-z A-Z 0-9 _]* !>> [0-9 A-Z _ a-z]) \ Reserved 
  | [\"] (![\"] | "\\\"")* [\"]
  | [\-]? "." [0-9]+
  | [\-]? [0-9]+ "." [0-9]*
  ;
                    
syntax StatementList = StatementOptional*;
syntax Statement 
  = NodeStatement
  | EdgeStatement
  | AttrStatement  
  | Id "=" Id
  ;
  
syntax StatementOptional = Statement ";"?;              
                                   
syntax NodeStatement = Nod AttrList;

syntax EdgeStatement = Nod EdgeRhs AttrList ;

syntax Edg =  EdgeOp Nod; 

syntax EdgeOp = "-\>" | "--";

syntax EdgeRhs = Edg+;

syntax NodeId 
  = Id 
  | Id Port
  ;

syntax Port = ":" Id (":" Id)?
//          | ":" Id
//          | ":" CompassPt
            ;

// syntax CompassPt = "n" | "ne" | "e" | "se" | "s" | "sw" | "w"| "nw" | "c" |"_";

syntax AttrList =   AttrList0*;

syntax AttrList0 =  "[" DotAttr* "]";

syntax DotAttr = Id "=" Id | Id "=" Id "," ;

syntax AttrStatement = AttrTag AttrList;

syntax Subgraph = ("subgraph" Id? )?  "{" StatementList "}";

lexical Comment = "/*" (![*] | [*] !>> "/")* "*/"
                | "//" ![\n]* $
                ;

layout LAYOUTLIST = LAYOUT* !>> [\ \t\n\r] !>> "//" !>> "/*"
                    ;
                   

lexical LAYOUT = Whitespace: [\ \t\n\r] 
               | @category="Comment" Comment
               ;
