module experiments::m3::JavaM3

extend experiments::m3::Core;

data M3 = java(loc project,
             rel[loc name, loc src] source,
             rel[loc from, loc to] containment
             rel[loc from, loc to] inheritance = {},
             rel[loc from, loc to] invocation = {},
             rel[loc from, loc to] access = {},
             rel[loc from, loc to] reference = {},
             rel[loc from, loc to] imports = {},
             map[loc definition, Type typ] types = (),
             map[loc definition, loc comments] documentation = (),
             rel[loc definition, Modifier modifiers] modifiers = {}
          );