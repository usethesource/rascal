module lang::hotdrink::Decls

extend lang::hotdrink::Expressions;

// curlies are not in the spec, but in the examples...
syntax QualifiedCellDecl 
  = @Foldable "interface" ":" "{" InterfaceCellDecl* "}"
  | @Foldable "input" ":" "{" InputCellDecl* "}"
  | @Foldable "output" ":" "{" OutputCellDecl* "}"
  | @Foldable "constant" ":" "{" ConstantCellDecl* "}"
  | @Foldable "logic" ":" "{" LogicCellDecl* "}"
  | @Foldable "invariant" ":" "{" InvariantCellDecl* "}"
  | @Foldable "external" ":" "{" Identifier ";" "}"
// Allow without curlies too (like in the paper and spec)
  | @Foldable "interface" ":" InterfaceCellDecl*
  | @Foldable "input" ":" InputCellDecl*
  | @Foldable "output" ":" OutputCellDecl*
  | @Foldable "constant" ":" ConstantCellDecl*
  | @Foldable "logic" ":" LogicCellDecl* 
  | @Foldable "invariant" ":" InvariantCellDecl*
  | @Foldable "external" ":" Identifier ";" 
  ;

syntax InterfaceCellDecl
  = "unlink"? Identifier Initializer? DefineExpression? ";"
  ;

syntax InputCellDecl
  = Identifier Initializer? ";"
  ;
  
syntax OutputCellDecl
  = Identifier DefineExpression ";"
  ;
  
syntax ConstantCellDecl
  = Identifier Initializer ";"
  ;
  
syntax LogicCellDecl
  = Identifier DefineExpression ";"
  | Conditional? "relate" "{" RelateExpression+ "}"
  ;
  
syntax InvariantCellDecl
  = Identifier DefineExpression ";"
  ;

syntax RelateExpression
  = {Identifier ","}+ DefineExpression ";"
  ;
  
syntax Initializer
  = ":" Expression
  ;
  
syntax Conditional
  = "when" "(" Expression ")"
  ;
  
syntax DefineExpression
  = "\<==" Expression
  ;
  
keyword Keywords
  = "sheet"
  | "interface"
  | "input"
  | "output"
  | "constant"
  | "logic"
  | "invariant"
  | "unlink"
  | "when"
  | "relate"
  | "external"
  ;