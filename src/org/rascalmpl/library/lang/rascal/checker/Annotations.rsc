@bootstrapParser
module lang::rascal::checker::Annotations

import lang::rascal::types::Types;
import lang::rascal::scoping::SymbolTable;
import ParseTree;

//
// Annotation for linking symbol table items to trees.
//
anno set[ItemId] Tree@nameIds;

