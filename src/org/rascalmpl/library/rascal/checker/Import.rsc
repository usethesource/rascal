module rascal::checker::Import

import rascal::\old-syntax::RascalForImportExtraction;

public set[str] importedModules(Module m) {
  return { "<i>" | /QualifiedName i := m };
}

public Module linkImportedModules(Module m, map[str, loc] links) {
  return visit(m) {
    case QualifiedName i:
       if ("<i>" in links)
         insert i[@link=links["<i>"]];
  }
} 