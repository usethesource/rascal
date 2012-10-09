module lang::hotdrink::Adam

extend lang::hotdrink::Decls;

start syntax TranslationUnit = SheetSpecifier*;

syntax SheetSpecifier
  = "sheet" Identifier "{" QualifiedCellDecl* "}"
  ;



