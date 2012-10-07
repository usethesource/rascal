module lang::hotdrink::Eve

extend lang::hotdrink::Decls;

start syntax LayoutSpecifier 
  = "layout" Identifier "{" QualifiedCellDecl* View  "}"
  | View
  ;
  
syntax View
  = "view" "{" Widget* "}"
  ;

syntax Widget
  = Identifier Options? ";"
  | @Foldable Identifier Options? Block
  ;
  


syntax NamedParameter
  = Identifier
  | Identifier ":" Expression
  ;

syntax Options
  = "(" {NamedParameter ","}* ")"
  ;

  
syntax Block
  = "{" Widget* "}"
  ;
 
  
keyword Keywords = "layout" | "view";
