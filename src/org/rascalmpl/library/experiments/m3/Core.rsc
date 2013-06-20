module experiments::m3::Core

import experiments::m3::AST;

data M3 = m3();
             
anno rel[loc name, loc src] M3@source;
anno rel[loc from, loc to] M3@containment;