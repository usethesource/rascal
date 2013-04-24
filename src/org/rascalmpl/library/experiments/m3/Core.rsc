module experiments::m3::Core

import experiments::m3::AST;

data M3 = m3(loc project,
             map[loc name, loc src] source,
             rel[loc from, loc to] containment
             rel[loc from, loc to] inheritance = {},
             rel[loc from, loc to] access = {},
             rel[loc from, loc to] reference = {},
             rel[loc from, loc to] imports = {},
             map[loc definition, Type typ] types = (),
             map[loc definition, loc comments] documentation = (),
             rel[loc definition, Modifier modifiers] modifiers = {}
          );