@bootstrapParser
module rascal::checker::Annotations

import rascal::types::Types;
import rascal::scoping::SymbolTable;
import ParseTree;

//
// Annotation for linking symbol table items to trees.
//
anno set[ItemId] Tree@nameIds;

