module experiments::m3::Core

import experiments::m3::AST;

data M3 = m3(loc project,
             rel[loc name, loc src] source,
             rel[loc from, loc to] containment
          );