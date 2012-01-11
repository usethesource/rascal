@contributor{Bert Lisser - Bert.Lisser@cwi.nl}
module lang::dot::syntax::Dot

start syntax DOT  = LAYOUT* Graph  Id "{" StatementList "}" "\n"?;

syntax Graph = "graph"|"digraph";

syntax AttrTag = "graph"|"node"|"edge";

syntax Nod = NodeId|Subgraph;

lexical Id = [a-zA-Z0-9_]+ !>> [a-zA-Z0-9_] \ Keyword 
           | [\"] (![\"] | "\\\"")* [\"]
             ;
                    
syntax StatementList = StatementOptional*;
                    
syntax Keyword = "graph"|"digraph"|"subgraph"|"node"|"edge";

syntax Statement = NodeStatement
                  |EdgeStatement
                  |AttrStatement
                  |Id "=" Id
                  |Subgraph
                  ;
  
syntax StatementOptional = Statement ";"?;              
                                   
syntax NodeStatement = NodeId AttrList;

syntax EdgeStatement = Nod EdgeRhs AttrList ;

syntax Edg =  EdgeOp Nod; 

syntax EdgeOp = "-\>" | "--";

syntax EdgeRhs = Edg*;

syntax NodeId = Id 
                | Id Port
                ;

syntax Port = ":" Id Compass_pt
            | ":" Id
            | ":" Compass_pt
            ;

syntax CompassPt = "n" | "ne" | "e" | "se" | "s" | "sw" | "w"| "nw" | "c" |"_";

syntax AttrList =   AttrList0*;

syntax AttrList0 =  "[" DotAttr* "]";

syntax DotAttr = Id "=" Id | Id "=" Id "," ;

syntax AttrStatement = AttrTag AttrList;

syntax Subgraph = ("subgraph" Id ","?)? "{" StatementList "}";

lexical Comment = "/*" (![*] | [*] !>> "/")* "*/"
                | "//" ![\n]* $
                ;

layout LAYOUTLIST = LAYOUT* !>> [\ \t\n\r] !>> "//" !>> "/*"
                    ;
                   

lexical LAYOUT = Whitespace: [\ \t\n\r] 
               | @category="Comment" Comment
               ;

 
/*                  
graph 	: 	[ strict ] (graph | digraph) [ ID ] '{' stmt_list '}'


stmt_list 	: 	[ stmt [ ';' ] [ stmt_list ] ]
stmt 	: 	node_stmt
	| 	edge_stmt
	| 	attr_stmt
	| 	ID '=' ID
	| 	subgraph
	
	
attr_stmt 	: 	(graph | node | edge) attr_list
attr_list 	: 	'[' [ a_list ] ']' [ attr_list ]
a_list 	: 	ID [ '=' ID ] [ ',' ] [ a_list ]
edge_stmt 	: 	(node_id | subgraph) edgeRHS [ attr_list ]
edgeRHS 	: 	edgeop (node_id | subgraph) [ edgeRHS ]
node_stmt 	: 	node_id [ attr_list ]
node_id 	: 	ID [ port ]
port 	: 	':' ID [ ':' compass_pt ]
	| 	':' compass_pt
subgraph 	: 	[ subgraph [ ID ] ] '{' stmt_list '}'
compass_pt 	: 	(n | ne | e | se | s | sw | w | nw | c | _)
*/
