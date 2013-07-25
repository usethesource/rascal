module experiments::m3::Core

import experiments::m3::AST;

data M3 = m3();
             
anno rel[loc name, loc src] M3@source;
anno rel[loc from, loc to] M3@containment;
anno list[str errorMessage] M3@projectErrors;
anno rel[loc from, loc to] M3@libraryContainment;
anno map[str simpleName, set[loc] qualifiedName] M3@resolveNames;