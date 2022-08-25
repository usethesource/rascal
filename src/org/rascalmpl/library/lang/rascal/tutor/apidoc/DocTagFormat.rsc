@synopsis{Describes the syntax of what is inside of the @doc{} tag format in Rascal.}
module lang::rascal::tutor::apidoc::DocTagFormat

start syntax Document 
= 
Paragraph*;

syntax Paragraph = ParagraphType Word+;

lexical Word = ![\t\n\ ]+ !>> ![\t\n\ ] \ ParagraphType;

keyword ParagraphType 
  = ".Synopsis"
  | ".Description"
  | ".Benefits"
  | ".Pitfalls"
  | ".Examples"
  | ".Details"
  | ".Syntax"
  | ".Types"
  | ".Name"
  ;

layout WS = [\t\n\ ]* !>> [\t\n\ ];
